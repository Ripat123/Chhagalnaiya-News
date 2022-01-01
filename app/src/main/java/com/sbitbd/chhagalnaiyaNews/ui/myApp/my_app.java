package com.sbitbd.chhagalnaiyaNews.ui.myApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.chhagalnaiyaNews.R;
import com.sbitbd.chhagalnaiyaNews.config.DoConfig;
import com.sbitbd.chhagalnaiyaNews.ui.privacy.privacy_policy;

import java.util.HashMap;
import java.util.Map;

public class my_app extends Fragment {

    private MaterialCardView cardViewP,cardViewD;
    private Button power;
    private DoConfig config = new DoConfig();
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_app, container, false);
        cardViewP = root.findViewById(R.id.privacy);
        cardViewD = root.findViewById(R.id.Dev);
        power = root.findViewById(R.id.power);
        webView = root.findViewById(R.id.con_webview);
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
        loadData(root);

        cardViewD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.sbit.com.bd"));
                startActivity(browserIntent);
            }
        });
        cardViewP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext().getApplicationContext(), privacy_policy.class));
            }
        });
        return root;
    }

    private void loadData(View root){
        try {

            String sql = "SELECT `contact_us`.`description` AS 'id' FROM `contact_us`";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.equals("1")){
                                webView.loadData("<html><body>"+response+"</body></html>",
                                        "text/html; charset=utf-8", "UTF-8");
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(root.getContext().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(root.getContext().getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }

}