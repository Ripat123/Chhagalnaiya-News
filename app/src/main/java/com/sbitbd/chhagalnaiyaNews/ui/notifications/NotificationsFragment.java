package com.sbitbd.chhagalnaiyaNews.ui.notifications;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.sbitbd.chhagalnaiyaNews.R;
import com.sbitbd.chhagalnaiyaNews.config.DoConfig;
import com.sbitbd.chhagalnaiyaNews.config.newsAdapter;
import com.sbitbd.chhagalnaiyaNews.config.news_model;
import com.sbitbd.chhagalnaiyaNews.ui.home.HomeViewModel;

import java.util.HashMap;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private RecyclerView recyclerView;
    private EditText editText;
    private news_model news_model;
    private newsAdapter newsAdapter;
    private DoConfig config = new DoConfig();
    private HomeViewModel homeViewModel = new HomeViewModel();
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        initview(root);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    private void initview(View root) {
        recyclerView = root.findViewById(R.id.search_rec);
        editText = root.findViewById(R.id.search);
        progressBar = root.findViewById(R.id.Sprogress);
        GridLayoutManager newslayoutManager = new GridLayoutManager(root.getContext().getApplicationContext(),1);
        recyclerView.setLayoutManager(newslayoutManager);
        newsAdapter = new newsAdapter(root.getContext().getApplicationContext());
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    progressBar.setVisibility(View.VISIBLE);
                    View view = root.getRootView();
                    hideKeyboardFrom(root.getContext().getApplicationContext(),view);
                    newsAdapter.ClearNews();
                    getNews(root,editText.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
        recyclerView.setAdapter(newsAdapter);
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getNews(View root,String data) {
        try {
            String sqld = "SELECT publish_news.`id`,publish_news.`title`,publish_news.`ext` AS 'image'" +
                    " FROM `publish_news` WHERE `title` LIKE '%"+data+"%'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.NEWS_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            if (check == 1)
//                                swipeRefreshLayout.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                            if (!response.equals("1")) {
                                homeViewModel.showProJSON(response, news_model, newsAdapter, root.getContext().getApplicationContext());

                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
//                    if (check == 1)
//                        swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
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
}
