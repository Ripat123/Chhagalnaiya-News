package com.sbitbd.chhagalnaiyaNews.config;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.sbitbd.chhagalnaiyaNews.R;
import com.sbitbd.chhagalnaiyaNews.ui.details.news_details;

import java.util.ArrayList;
import java.util.List;

public class multiSectionAdapter extends RecyclerView.Adapter<multiSectionAdapter.viewHolder>{
    private Context context;
    private List<multisection_model> multisection_models;

    public multiSectionAdapter(Context context) {
        this.context = context;
        this.multisection_models = new ArrayList<>();

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflat = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card_item,null);
        viewHolder veiwHolder = new viewHolder(inflat);
        return veiwHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        multisection_model multisection_model = multisection_models.get(position);
        holder.bind(multisection_model);
    }

    @Override
    public int getItemCount() {
        return multisection_models.size();
    }
    public void ClearNews(){
        multisection_models.clear();
        notifyDataSetChanged();
    }
    public void adduser(multisection_model cart_model){
        try {
            multisection_models.add(cart_model);
            int position = multisection_models.indexOf(cart_model);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        MaterialCardView cardView;
        final Context context1;
        final View view;
        DoConfig config = new DoConfig();

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.menu_text);
            imageView = itemView.findViewById(R.id.menuCardimg);
            cardView = itemView.findViewById(R.id.section_menu_card);
            context1 = itemView.getContext();
            this.view = itemView;
        }
        public void bind(multisection_model sectionModel){
            textView.setText(sectionModel.getTitle());
            Glide.with(view)
                    .load(config.IMAGE_URL+sectionModel.getId()+"."+sectionModel.getImage())
                    .fitCenter().error(R.drawable.logo_water).placeholder(R.drawable.logo_water)
                    .into(imageView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent;
                    intent = new Intent(context1, news_details.class);
                    intent.putExtra("id",sectionModel.getId());
                    intent.putExtra("title",sectionModel.getTitle());
                    intent.putExtra("img",sectionModel.getImage());
                    context1.startActivity(intent);
                }
            });
        }
    }
}
