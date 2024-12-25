package com.example.posyanduapps.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.posyanduapps.R;
import com.example.posyanduapps.features.DetailSubkategoriActivity;
import com.example.posyanduapps.models.Subkategori;

import java.util.List;

public class SubkategoriAdapter extends RecyclerView.Adapter<SubkategoriAdapter.SubkategoriViewHolder> {

    private List<Subkategori> subkategoriList;

    public SubkategoriAdapter(List<Subkategori> subkategoriList) {
        this.subkategoriList = subkategoriList;
    }

    @NonNull
    @Override
    public SubkategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subkategori, parent, false);
        return new SubkategoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubkategoriViewHolder holder, int position) {
        Subkategori subkategori = subkategoriList.get(position);
        holder.tvNamaSubkategori.setText(subkategori.getNama());
        holder.ivIcon.setImageResource(subkategori.getIconResId());
        holder.tvIntroSubkategori.setText(subkategori.getKeterangan());
        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailSubkategoriActivity.class);
            intent.putExtra("subkategori_nama", subkategori.getNama());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return subkategoriList.size();
    }

    static class SubkategoriViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvNamaSubkategori,tvIntroSubkategori;

        public SubkategoriViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvNamaSubkategori = itemView.findViewById(R.id.tvNamaSubkategori);
            tvIntroSubkategori = itemView.findViewById(R.id.tvIntroSubkategori);

        }
    }
}
