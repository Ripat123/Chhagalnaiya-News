package com.sbitbd.chhagalnaiyaNews.ui.viewsection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sbitbd.chhagalnaiyaNews.config.sectionModel;
import com.sbitbd.chhagalnaiyaNews.config.tab_adapter;
import com.sbitbd.chhagalnaiyaNews.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class section_view extends AppCompatActivity {

    private TextView actionT,top_tab_t;
    private RecyclerView recyclerView,news_rec;
    private sectionModel sectionModel;
    private tab_adapter tab_adapter;
    private Button back;
    private newsAdapter newsAdapter;
    private news_model news_model;
    private DoConfig config = new DoConfig();
    private HomeViewModel homeViewModel = new HomeViewModel();
    private List<sectionModel> sectionModels = new ArrayList<>();
    private String subID,subTitle;
    private int limit=0;
    private ProgressDialog progressDialog;

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_view);
        initView();
    }

    private void initView(){
        actionT = findViewById(R.id.top_logo);
        back = findViewById(R.id.pro_des_back);
        recyclerView = findViewById(R.id.tab_rec);
        top_tab_t = findViewById(R.id.top_tab_t);
        news_rec = findViewById(R.id.Snews_rec);
        String title = getIntent().getStringExtra("section");
        String id = getIntent().getStringExtra("id");
        actionT.setText(title);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GridLayoutManager sectionlayoutManager = new GridLayoutManager(section_view.this,1,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(sectionlayoutManager);
        tab_adapter = new tab_adapter(section_view.this,top_tab_t,news_rec);

        getSubmenu(id);
        recyclerView.setAdapter(tab_adapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    private void getSubmenu(String id){
        try {
            String sql = "SELECT sub_menu_name AS 'menu_name',id FROM submenu WHERE menu_id = '"+id+"' AND `status` = '1'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.MENU_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.equals("1")){
                                sectionModels = homeViewModel.showSubMenuJSON(response, sectionModel, tab_adapter, section_view.this);
                                sectionModel = sectionModels.get(0);
                                subID = sectionModel.getId();
                                subTitle = sectionModel.getName();
                                top_tab_t.setText(subTitle);
                                String sql = "SELECT publish_news.`id`,publish_news.`title`,publish_news.`ext` AS 'image' FROM `publish_news_submenu_piority` " +
                                        "INNER JOIN publish_news ON `publish_news_submenu_piority`.`news_id` = publish_news.`id` WHERE publish_news_submenu_piority.`submenu_id` = '"+subID+"' ORDER BY publish_news.id DESC LIMIT 0, 10";
                                getNews(0,sql);
                            }else {
                                String sqld = "SELECT publish_news.`id`,publish_news.`title`,publish_news.`ext` AS 'image' FROM `publish_news_menu_piority`  " +
                                        " INNER JOIN publish_news ON `publish_news_menu_piority`.`news_id` = publish_news.`id` " +
                                        "WHERE publish_news_menu_piority.`menu_id` = '"+id+"' ORDER BY publish_news.id DESC LIMIT 0, 10 ";
                                getNews(0,sqld);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(section_view.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(section_view.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }

    private void getNews(int limit,String sql){
        GridLayoutManager newslayoutManager = new GridLayoutManager(section_view.this,1);
        news_rec.setLayoutManager(newslayoutManager);
        newsAdapter = new newsAdapter(section_view.this);
        progressDialog = ProgressDialog.show(section_view.this,"","Loading...",false,false);
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.NEWS_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if(!response.equals("1")){
                                homeViewModel.showProJSON(response, news_model, newsAdapter, section_view.this);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(section_view.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(section_view.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
        news_rec.setAdapter(newsAdapter);
    }


}