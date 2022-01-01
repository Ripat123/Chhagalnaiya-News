package com.sbitbd.chhagalnaiyaNews.ui.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.chhagalnaiyaNews.R;
import com.sbitbd.chhagalnaiyaNews.config.DoConfig;
import com.sbitbd.chhagalnaiyaNews.config.multiSectionAdapter;
import com.sbitbd.chhagalnaiyaNews.config.multinews_adapter;
import com.sbitbd.chhagalnaiyaNews.config.multisection_model;
import com.sbitbd.chhagalnaiyaNews.config.newsAdapter;
import com.sbitbd.chhagalnaiyaNews.config.news_model;
import com.sbitbd.chhagalnaiyaNews.config.sectionMenuAdapter;
import com.sbitbd.chhagalnaiyaNews.config.sectionModel;
import com.sbitbd.chhagalnaiyaNews.ui.details.news_details;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ImageView top_Img;
    private TextView top_txt;
    private RecyclerView top_rec,multisec_rec,multinews_rec;
    private news_model news_model;
    private newsAdapter newsAdapter;
    private multiSectionAdapter multiSectionAdapter;
    private multisection_model multisection_model;
    private MaterialCardView top_cardView;
    private multinews_adapter multinews_adapter;
    private sectionModel sectionModel;
    private DoConfig config;
    private List<news_model> news_models;
    private SwipeRefreshLayout swipeRefreshLayout;
    private sectionMenuAdapter sectionMenuAdapter;
    private Button power;
    private Typeface typeface;
    private ConstraintLayout progressBar;
    private WebView webView;
    private String news,menu,multisecN,data;
    private View root1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        config = new DoConfig();
        news_models = new ArrayList<>();
        root1 = root;
        initView(root,savedInstanceState);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
    private void initView(View root,Bundle savedInstanceState){
        top_Img = root.findViewById(R.id.top_img);
        top_txt = root.findViewById(R.id.top_t);
        swipeRefreshLayout = root.findViewById(R.id.home_refresh);
        progressBar = root.findViewById(R.id.splash);
        webView = root.findViewById(R.id.mu_webview);

        top_rec = root.findViewById(R.id.top_rec);
        multisec_rec = root.findViewById(R.id.multi_section_rec);
        multinews_rec = root.findViewById(R.id.multi_news_rec);
        top_cardView = root.findViewById(R.id.top_n);

        top_rec.setNestedScrollingEnabled(false);
        multinews_rec.setNestedScrollingEnabled(false);
        multisec_rec.setNestedScrollingEnabled(false);

        if (savedInstanceState != null){
            restore(savedInstanceState);
        }else {
            initData(root);
        }

        power = root.findViewById(R.id.power);
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


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsAdapter.ClearNews();
                multinews_adapter.ClearNews();
                multiSectionAdapter.ClearNews();
                initData(root);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initData(View root){
        GridLayoutManager newslayoutManager = new GridLayoutManager(root.getContext().getApplicationContext(),1);
        top_rec.setLayoutManager(newslayoutManager);
        newsAdapter = new newsAdapter(root.getContext().getApplicationContext());
        getNews(root);
        top_rec.setAdapter(newsAdapter);

        GridLayoutManager sectionlayoutManager = new GridLayoutManager(root.getContext().getApplicationContext(),1,RecyclerView.HORIZONTAL,false);
        multisec_rec.setLayoutManager(sectionlayoutManager);
        multiSectionAdapter = new multiSectionAdapter(root.getContext().getApplicationContext());
        getMultiSecNews(root);
        multisec_rec.setAdapter(multiSectionAdapter);

        GridLayoutManager multinewslayoutManager = new GridLayoutManager(root.getContext().getApplicationContext(),1);
        multinews_rec.setLayoutManager(multinewslayoutManager);
        multinews_adapter = new multinews_adapter(root.getContext().getApplicationContext());

        getMenu(root);
        multinews_rec.setAdapter(multinews_adapter);
        loadData(root);
    }

    private void restore(Bundle bundle){
        news = bundle.getString("news");
        menu = bundle.getString("menu");
        multisecN = bundle.getString("multisecN");
        data = bundle.getString("data");
        GridLayoutManager newslayoutManager = new GridLayoutManager(root1.getContext().getApplicationContext(),1);
        top_rec.setLayoutManager(newslayoutManager);
        newsAdapter = new newsAdapter(root1.getContext().getApplicationContext());
        news_models = homeViewModel.showProJSON(news, news_model, newsAdapter, root1.getContext().getApplicationContext(),top_Img,top_txt);
        news_model = news_models.get(0);


        typeface = Typeface.createFromAsset(root1.getContext().getAssets(),"font/hindi.ttf");
        top_txt.setTypeface(typeface);
        top_txt.setText(news_model.getTitle());
        Glide.with(root1)
                .load(config.IMAGE_URL+news_model.getId()+"."+news_model.getImg())
                .fitCenter().error(R.drawable.logo_water).placeholder(R.drawable.logo_water)
                .into(top_Img);
        top_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent = new Intent(root1.getContext().getApplicationContext(), news_details.class);
                intent.putExtra("id",news_model.getId());
                intent.putExtra("title",news_model.getTitle());
                intent.putExtra("img",news_model.getImg());
                startActivity(intent);
            }
        });
        top_rec.setAdapter(newsAdapter);

        GridLayoutManager sectionlayoutManager = new GridLayoutManager(root1.getContext().getApplicationContext(),1,RecyclerView.HORIZONTAL,false);
        multisec_rec.setLayoutManager(sectionlayoutManager);
        multiSectionAdapter = new multiSectionAdapter(root1.getContext().getApplicationContext());
        homeViewModel.showSecNewsJSON(multisecN, multisection_model, multiSectionAdapter, root1.getContext().getApplicationContext());
        multisec_rec.setAdapter(multiSectionAdapter);

        GridLayoutManager multinewslayoutManager = new GridLayoutManager(root1.getContext().getApplicationContext(),1);
        multinews_rec.setLayoutManager(multinewslayoutManager);
        multinews_adapter = new multinews_adapter(root1.getContext().getApplicationContext());

        homeViewModel.showMultiMenuJSON(menu, sectionModel, multinews_adapter, root1.getContext().getApplicationContext());
        multinews_rec.setAdapter(multinews_adapter);
        progressBar.setVisibility(View.GONE);
        webView.loadData("<html><body>"+data+"</body></html>",
                "text/html; charset=utf-8", "UTF-8");
    }

    private void getNews(View root){
        try {
            String sqld = "SELECT `id`,`title`,`ext` AS 'image' FROM `publish_news` ORDER BY id DESC LIMIT 0, 10 ";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.NEWS_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            if (check == 1)
//                                swipeRefreshLayout.setRefreshing(false);
                            if (!response.equals("1") && response != null) {
                                news_models = homeViewModel.showProJSON(response, news_model, newsAdapter, root.getContext().getApplicationContext(),top_Img,top_txt);
                                news_model = news_models.get(0);
                                typeface = Typeface.createFromAsset(root.getContext().getAssets(),"font/hindi.ttf");
                                news = response;
                                top_txt.setTypeface(typeface);
                                top_txt.setText(news_model.getTitle());
                                Glide.with(root)
                                        .load(config.IMAGE_URL+news_model.getId()+"."+news_model.getImg())
                                        .fitCenter().error(R.drawable.logo_water).placeholder(R.drawable.logo_water)
                                        .thumbnail(0.05f)
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(top_Img);

                                top_cardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final Intent intent;
                                        intent = new Intent(root.getContext().getApplicationContext(), news_details.class);
                                        intent.putExtra("id",news_model.getId());
                                        intent.putExtra("title",news_model.getTitle());
                                        intent.putExtra("img",news_model.getImg());
                                        startActivity(intent);
                                    }
                                });
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

    private void getMultiSecNews(View root){
        try {
            String sqld = "SELECT `id`,`title`,`ext` AS 'image' FROM `publish_news` ORDER BY id DESC LIMIT 10, 10 ";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.NEWS_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            if (check == 1)
//                                swipeRefreshLayout.setRefreshing(false);
                            if (!response.equals("1")) {
                                homeViewModel.showSecNewsJSON(response, multisection_model, multiSectionAdapter, root.getContext().getApplicationContext());
                                multisecN = response;
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

    private void getMenu(View root){
        try {
            String sqld = "SELECT `menu_name`,id FROM `menu` WHERE `status` = '1'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.MENU_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            if (check == 1)
//                                swipeRefreshLayout.setRefreshing(false);\
                            progressBar.setVisibility(View.GONE);
                            if (!response.equals("1")) {
                                menu = response;
                                homeViewModel.showMultiMenuJSON(response, sectionModel, multinews_adapter, root.getContext().getApplicationContext());
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
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
    private void loadData(View root){
        try {

            String sql = "SELECT `contact_us`.`description` AS 'id' FROM `contact_us`";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.equals("1") && response != null){
                                data = response;
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("news",news);
        outState.putString("menu",menu);
        outState.putString("multisecN",multisecN);
        outState.putString("data",data);
    }

}