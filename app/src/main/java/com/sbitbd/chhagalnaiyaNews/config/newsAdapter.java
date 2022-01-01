package com.sbitbd.chhagalnaiyaNews.config;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.card.MaterialCardView;
import com.sbitbd.chhagalnaiyaNews.R;
import com.sbitbd.chhagalnaiyaNews.ui.details.news_details;

import java.util.ArrayList;
import java.util.List;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.viewHolder>{
    private Context context;
    private List<news_model> news_models;
    private DoConfig config = new DoConfig();

    public newsAdapter(Context context) {
        this.context = context;
        news_models = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflat = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,null);
        viewHolder veiwHolder = new viewHolder(inflat);
        return veiwHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        news_model cart_model = news_models.get(position);
        holder.bind(cart_model);
    }

    @Override
    public int getItemCount() {
        return news_models.size();
    }
    public void ClearNews(){
        news_models.clear();
        notifyDataSetChanged();
    }
    public void adduser(news_model cart_model){
        try {
            news_models.add(cart_model);
            int position = news_models.indexOf(cart_model);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView title,subtitle;
        ImageView imageView;
        MaterialCardView cardView;
        final Context context1;
        final View view;
        Typeface typeface;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_txt);
            subtitle = itemView.findViewById(R.id.news_time);
            imageView = itemView.findViewById(R.id.news_img);
            cardView = itemView.findViewById(R.id.news_card);
            context1 = itemView.getContext();
            this.view = itemView;
        }
        public void bind(news_model sectionModel){
            typeface = Typeface.createFromAsset(context1.getAssets(),"font/hindi.ttf");
            title.setTypeface(typeface);
            title.setText(sectionModel.getTitle());
            subtitle.setText(sectionModel.getSubtitle());
//            imageView.setImageResource(sectionModel.getImg());
            Glide.with(view)
                    .load(config.IMAGE_URL+sectionModel.getId()+"."+sectionModel.getImg())
                    .fitCenter().error(R.drawable.logo_water).placeholder(R.drawable.logo_water)
                    .thumbnail(0.8f)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent;
                    intent = new Intent(context1, news_details.class);
                    intent.putExtra("id",sectionModel.getId());
                    intent.putExtra("title",sectionModel.getTitle());
                    intent.putExtra("img",sectionModel.getImg());
                    context1.startActivity(intent);
                }
            });
        }
    }
}
