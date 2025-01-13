package com.example.posyanduapps.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.posyanduapps.R;
import com.example.posyanduapps.features.FormPelayananKesehatanBayiBaruLahirActivity;
import com.example.posyanduapps.features.FormPelayananKesehatanIbuBersalinActivity;
import com.example.posyanduapps.features.FormPelayananKesehatanIbuHamilActivity; // Make sure this is the correct activity
import com.example.posyanduapps.features.FormPelayananKesehatanIbuNifasActivity;
import com.example.posyanduapps.models.FormDataIbu; // A model class for the data

import java.util.List;

public class FormDataIbuAdapter extends RecyclerView.Adapter<FormDataIbuAdapter.FormDataIbuHolder> {

    private List<FormDataIbu> formDataIbuList;

    public FormDataIbuAdapter(List<FormDataIbu> formDataIbuList) {
        this.formDataIbuList = formDataIbuList;
    }

    @NonNull
    @Override
    public FormDataIbuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_form_data_ibu, parent, false);
        return new FormDataIbuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FormDataIbuHolder holder, int position) {
        FormDataIbu formDataIbu = formDataIbuList.get(position);
        holder.tvNamaFormDataIbu.setText(formDataIbu.getNama());
        holder.tvIntroFormDataIbu.setText(formDataIbu.getIntro());
        holder.ivIcon.setImageResource(formDataIbu.getIconResId());

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = null;

            // Check form ID and navigate to corresponding activity
            switch (formDataIbu.getId()) {
                case "1":
                    intent = new Intent(context, FormPelayananKesehatanIbuHamilActivity.class);
                    break;
                case "2":
                    intent = new Intent(context, FormPelayananKesehatanIbuBersalinActivity.class);
                    break;
                case "3":
                    intent = new Intent(context, FormPelayananKesehatanIbuNifasActivity.class);
                    break;
                case "4":
                    intent = new Intent(context, FormPelayananKesehatanBayiBaruLahirActivity.class);
                    break;
                default:
                    // You can show a toast or handle a default case if necessary
                    Toast.makeText(context, "Form tidak ditemukan", Toast.LENGTH_SHORT).show();
                    return; // Exit the click handler if no match
            }

            // Pass the form ID or other necessary data to the target activity
            if (intent != null) {
                intent.putExtra("form_data_ibu_id", formDataIbu.getId()); // Pass the form ID or other necessary data
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return formDataIbuList.size();
    }

    static class FormDataIbuHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvNamaFormDataIbu, tvIntroFormDataIbu;

        public FormDataIbuHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvNamaFormDataIbu = itemView.findViewById(R.id.tvNamaFormDataIbu);
            tvIntroFormDataIbu = itemView.findViewById(R.id.tvIntroFormDataIbu);
        }
    }
}
