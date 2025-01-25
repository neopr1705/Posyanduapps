package com.example.posyanduapps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.posyanduapps.R;
import com.example.posyanduapps.models.Kesehatan;

import java.util.List;

public class KesehatanAdapter extends ArrayAdapter<Kesehatan> {
    private Context context;
    private List<Kesehatan> kesehatanList;

    public KesehatanAdapter(Context context, List<Kesehatan> kesehatanList) {
        super(context, 0, kesehatanList);
        this.context = context;
        this.kesehatanList = kesehatanList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_kesehatan, parent, false);
        }

        Kesehatan kesehatan = kesehatanList.get(position);

        // Menyambungkan TextView dari layout XML
        TextView namaText = convertView.findViewById(R.id.name);
        TextView beratBadanText = convertView.findViewById(R.id.berat_badan);
        TextView tinggiBadanText = convertView.findViewById(R.id.tinggi_badan);
        TextView statusVaksinText = convertView.findViewById(R.id.status_vaksin);
        TextView riwayatPenyakitText = convertView.findViewById(R.id.riwayat_penyakit);
        TextView Tanggal = convertView.findViewById(R.id.tvTanggal);
        TextView Jam = convertView.findViewById(R.id.tvJam);

        // Menambahkan TextView untuk elemen baru: Lingkar Kepala, Lingkar Lengan, Lingkar Perut
        TextView lingkarKepalaText = convertView.findViewById(R.id.lingkar_kepala);
        TextView lingkarLenganText = convertView.findViewById(R.id.lingkar_lengan);
        TextView lingkarPerutText = convertView.findViewById(R.id.lingkar_perut);

        // Menampilkan data dari model Kesehatan ke dalam TextView yang relevan
        namaText.setText("Id: " + kesehatan.getNama());
        beratBadanText.setText("Berat Badan: " + kesehatan.getBeratBadan());
        tinggiBadanText.setText("Tinggi Badan: " + kesehatan.getTinggiBadan());
        statusVaksinText.setText("Status Vaksin: " + kesehatan.getStatusVaksin());
        riwayatPenyakitText.setText("Riwayat Penyakit: " + kesehatan.getRiwayatPenyakit());
        Tanggal.setText("Tanggal: " + kesehatan.getTanggal());
        Jam.setText("Jam: " + kesehatan.getJam());

                // Menampilkan data untuk Lingkar Kepala, Lingkar Lengan, dan Lingkar Perut
        if(kesehatan.getLingkarKepala() == null){
            lingkarKepalaText.setVisibility(View.GONE);
        }
        if(kesehatan.getLingkarLengan() == null){
            lingkarLenganText.setVisibility(View.GONE);
        }
        if(kesehatan.getLingkarPerut() == null){
            lingkarPerutText.setVisibility(View.GONE);
        }
        if (kesehatan.getNama() == null){
            namaText.setVisibility(View.GONE);
        }
        lingkarKepalaText.setText("Lingkar Kepala: " + kesehatan.getLingkarKepala());
        lingkarLenganText.setText("Lingkar Lengan: " + kesehatan.getLingkarLengan());
        lingkarPerutText.setText("Lingkar Perut: " + kesehatan.getLingkarPerut());

        return convertView;
    }
}
