package com.sbitbd.chhagalnaiyaNews.config;

import android.content.Context;
import android.content.Intent;
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
import com.sbitbd.chhagalnaiyaNews.R;
import com.sbitbd.chhagalnaiyaNews.ui.home.HomeViewModel;
import com.sbitbd.chhagalnaiyaNews.ui.viewsection.section_view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class multinews_adapter extends RecyclerView.Adapter<multinews_adapter.viewHolder>{
    private Context context;
    private List<sectionModel> sectionModels;

    public multinews_adapter(Context context) {
        this.context = context;
        sectionModels = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflat = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_news,null);
        viewHolder veiwHolder = new viewHolder(inflat);
        return veiwHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        sectionModel sectionModel = sectionModels.get(position);
        holder.bind(sectionModel);
    }

    @Override
    public int getItemCount() {
        return sectionModels.size();
    }
    public void ClearNews(){
        sectionModels.clear();
        notifyDataSetChanged();
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
        RecyclerView recyclerView;
        Button button;
        final Context context1;
        newsAdapter newsAdapter;
        news_model news_model;
        DoConfig config = new DoConfig();
        HomeViewModel homeViewModel = new HomeViewModel();
        final View view;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title_txt);
            recyclerView = itemView.findViewById(R.id.multi_rec);
            button = itemView.findViewById(R.id.item_more_btn);
            context1 = itemView.getContext();
            this.view = itemView;
        }
        public void bind(sectionModel sectionModel){
            textView.setText(sectionModel.getName());
            GridLayoutManager newslayoutManager = new GridLayoutManager(context1,1);
            recyclerView.setLayoutManager(newslayoutManager);
            recyclerView.setNestedScrollingEnabled(false);
            newsAdapter = new newsAdapter(context1);
            getNews(view,sectionModel.getId());
            recyclerView.setAdapter(newsAdapter);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent;
                    intent = new Intent(context1,section_view.class);
                    intent.putExtra("section",sectionModel.getName());
                    intent.putExtra("id",sectionModel.getId());
                    context1.startActivity(intent);
                }
            });
        }

        private void getNews(View root,String subID){
            try {
                String sqld = "SELECT publish_news.`id`,publish_news.`title`,publish_news.`ext` AS 'image' FROM `publish_news_menu_piority`  " +
                        " INNER JOIN publish_news ON `publish_news_menu_piority`.`news_id` = publish_news.`id` " +
                        "WHERE publish_news_menu_piority.`menu_id` = '"+subID+"' ORDER BY publish_news.id DESC LIMIT 0, 3 ";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.NEWS_DATA,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (!response.equals("1")) {
                                    homeViewModel.showProJSON(response, news_model, newsAdapter, root.getContext().getApplicationContext());
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
