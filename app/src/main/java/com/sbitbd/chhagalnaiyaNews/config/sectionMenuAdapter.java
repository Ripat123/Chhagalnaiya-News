package com.sbitbd.chhagalnaiyaNews.config;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sbitbd.chhagalnaiyaNews.R;
import com.sbitbd.chhagalnaiyaNews.ui.viewsection.section_view;

import java.util.ArrayList;
import java.util.List;

public class sectionMenuAdapter extends RecyclerView.Adapter<sectionMenuAdapter.veiwHolder>{
    private Context context;
    private List<sectionModel> sectionModels;

    public sectionMenuAdapter(Context context) {
        this.context = context;
        this.sectionModels = new ArrayList<>();
    }

    @NonNull
    @Override
    public veiwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflat = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item,null);
        veiwHolder veiwHolder = new veiwHolder(inflat);
        return veiwHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull veiwHolder holder, int position) {
        sectionModel cart_model = sectionModels.get(position);
        holder.bind(cart_model);
    }

    @Override
    public int getItemCount() {
        return sectionModels.size();
    }
    public void ClearSection(){
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

    class veiwHolder extends RecyclerView.ViewHolder{
        TextView textView;
        final Context context1;
        MaterialCardView cardView;

        public veiwHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.section_text);
            cardView = itemView.findViewById(R.id.section_card);
            context1 = itemView.getContext();
        }
        public void bind(sectionModel sectionModel){
            textView.setText(sectionModel.getName());
            cardView.setOnClickListener(new View.OnClickListener() {
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
    }
}
