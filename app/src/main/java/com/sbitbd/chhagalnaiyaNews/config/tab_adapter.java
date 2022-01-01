package com.sbitbd.chhagalnaiyaNews.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.sbitbd.chhagalnaiyaNews.R;
import com.sbitbd.chhagalnaiyaNews.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tab_adapter extends RecyclerView.Adapter<tab_adapter.viewHolder>{
    private Context context;
    private List<sectionModel> sectionModels;
    private RecyclerView web;
    private TextView text;
    int limit = 0;

    public tab_adapter(Context context, TextView text, RecyclerView web) {
        this.context = context;
        this.sectionModels = new ArrayList<>();
        this.web = web;
        this.text = text;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflat = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_item,null);
        viewHolder veiwHolder = new viewHolder(inflat);
        return veiwHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        sectionModel cart_model = sectionModels.get(position);
        holder.bind(cart_model);
    }

    @Override
    public int getItemCount() {
        return sectionModels.size();
    }
    public void adduser(sectionModel cart_model){
        try {
            sectionModels.add(cart_model);
            int position = sectionModels.indexOf(cart_model);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        MaterialCardView cardView;
        final Context context1;
        newsAdapter newsAdapter;
        news_model news_model;
        DoConfig config = new DoConfig();
        HomeViewModel homeViewModel = new HomeViewModel();
        final View view;
        Button btn;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tab_txt);
            cardView = itemView.findViewById(R.id.tab_card);
            context1 =itemView.getContext();
            view = itemView;
//            btn = itemView.findViewById(R.id.s_more_btn);
        }
        public void bind(sectionModel sectionModel){
            textView.setText(sectionModel.getName());
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    text.setText(sectionModel.getName());

                    GridLayoutManager newslayoutManager = new GridLayoutManager(context,1);
                    web.setLayoutManager(newslayoutManager);
                    newsAdapter = new newsAdapter(context);
                    getNews(view,sectionModel.getId(),0);
                    web.setAdapter(newsAdapter);
                }
            });
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    limit+= 10;
//                    getNews(view,sectionModel.getId(),limit);
//                }
//            });
        }
        private void getNews(View root,String subID,int limit){
            try {
                String sqld = "SELECT publish_news.`id`,publish_news.`title`,publish_news.`ext` AS 'image' FROM `publish_news_submenu_piority` " +
                        " INNER JOIN publish_news ON `publish_news_submenu_piority`.`news_id` = publish_news.`id` WHERE publish_news_submenu_piority.`submenu_id` = '"+subID+"' ORDER BY publish_news.`id` DESC LIMIT "+limit+", 10 ";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.NEWS_DATA,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (!response.equals("1")) {
                                    homeViewModel.showProJSON(response, news_model, newsAdapter, root.getContext().getApplicationContext());
                                }else
                                    Toast.makeText(root.getContext().getApplicationContext(),"not found",Toast.LENGTH_SHORT).show();
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
}
