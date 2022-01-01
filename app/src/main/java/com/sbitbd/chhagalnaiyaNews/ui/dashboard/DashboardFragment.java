package com.sbitbd.chhagalnaiyaNews.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.chhagalnaiyaNews.R;
import com.sbitbd.chhagalnaiyaNews.config.DoConfig;
import com.sbitbd.chhagalnaiyaNews.config.sectionMenuAdapter;
import com.sbitbd.chhagalnaiyaNews.config.sectionModel;
import com.sbitbd.chhagalnaiyaNews.ui.home.HomeViewModel;

import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private sectionMenuAdapter sectionMenuAdapter;
    private sectionModel sectionModel;
    private RecyclerView recyclerView;
    private DoConfig config = new DoConfig();
    private HomeViewModel homeViewModel = new HomeViewModel();
    private Button power;
    private String Result = null;
    private View root1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = root.findViewById(R.id.sec_rec);
        power = root.findViewById(R.id.power);
        root1 = root;
        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(root.getContext());
                dialogBuilder.setTitle("Confirmation");
                dialogBuilder.setMessage("Are you sure you want to Exit?");
                dialogBuilder.setNegativeButton("Cancel",(dialog, which) -> {
                    dialog.cancel();
                });
                dialogBuilder.setPositiveButton("OK",(dialog, which) -> {
                    System.exit(1);
                });
                dialogBuilder.show();
            }
        });
        if (savedInstanceState != null) {
            Result = savedInstanceState.getString("result");
            GridLayoutManager layoutManager = new GridLayoutManager(root.getContext().getApplicationContext(),1);
            recyclerView.setLayoutManager(layoutManager);
            sectionMenuAdapter = new sectionMenuAdapter(root.getContext().getApplicationContext());
            homeViewModel.showMenuJSON(Result, sectionModel, sectionMenuAdapter, root.getContext().getApplicationContext());
            recyclerView.setAdapter(sectionMenuAdapter);
        } else {
            initView(root);
        }

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
    private void initView(View root){
        GridLayoutManager layoutManager = new GridLayoutManager(root.getContext().getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        sectionMenuAdapter = new sectionMenuAdapter(root.getContext().getApplicationContext());
        getMenu(root);
        recyclerView.setAdapter(sectionMenuAdapter);
    }
    private void getMenu(View root){
        try {
            String sqld = "SELECT `menu_name`,id FROM `menu` WHERE `status` = '1'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.MENU_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            if (check == 1)
//                                swipeRefreshLayout.setRefreshing(false);
                            if (!response.equals("1")) {
                                Result = response;
                                homeViewModel.showMenuJSON(response, sectionModel, sectionMenuAdapter, root.getContext().getApplicationContext());
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
//                    if (check == 1)
//                        swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(root.getContext().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sqld);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(root.getContext().getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("result",Result);
    }


}