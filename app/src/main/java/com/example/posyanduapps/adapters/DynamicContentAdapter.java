package com.example.posyanduapps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.posyanduapps.R;
import com.example.posyanduapps.models.DynamicContent;
import java.util.List;

public class DynamicContentAdapter extends RecyclerView.Adapter<DynamicContentAdapter.ViewHolder> {
    private Context context;
    private DynamicContent dynamicContent;

    public DynamicContentAdapter(Context context, DynamicContent dynamicContent) {
        this.context = context;
        this.dynamicContent = dynamicContent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Mengisi data pada item berdasarkan posisi
        if(dynamicContent.getTextList().get(position).equalsIgnoreCase("nodata")){
            holder.textView.setVisibility(View.GONE);
        }else{
        holder.textView.setText(dynamicContent.getTextList().get(position));
        }
        if (dynamicContent.getImageList().get(position)!= -1) {
            holder.imageView.setImageResource(dynamicContent.getImageList().get(position));
        }else{
            holder.imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dynamicContent.getTextList().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvContentText);
            imageView = itemView.findViewById(R.id.ivContentImage);

        }


    }
}
