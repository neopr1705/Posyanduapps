package com.example.posyanduapps.features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.posyanduapps.R;
import java.util.List;

public class ReminderAdapter extends ArrayAdapter<String> {

    public ReminderAdapter(Context context, List<String> reminders) {
        super(context, 0, reminders);
    }

    public ReminderAdapter(Context context) {
        super(context,0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position < 0 || position >= getCount()) {
            return null;  // Pastikan index valid
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alarm, parent, false);
        }

        String reminder = getItem(position);  // Ambil data reminder

        if (reminder == null || reminder.isEmpty()) {
            return convertView;  // Jika data kosong, kembalikan tampilan default
        }

        // Set data ke tampilan item_alarm
        TextView tvReminderName = convertView.findViewById(R.id.tvNamaAlarm);
        TextView tvReminderTime = convertView.findViewById(R.id.tvJamAlarm);
        TextView tvReminderDate = convertView.findViewById(R.id.tvTanggalAlarm);
        TextView tvReminderDay = convertView.findViewById(R.id.tvHariAlarm);
        TextView tvReminderPlace = convertView.findViewById(R.id.tvTempatAlarm);

        // Misalnya, memisahkan bagian yang relevan
        // Format data yang diproses:
        // Nama: Fariz; Tanggal: 13/12/2024; Hari: Jumat; Jam: 4:00; Tempat: Posyandu A
        String[] parts = reminder.split(";");
        if (parts.length >= 4) {
            // Ambil nama
            String name = getDataFromPart(parts[0]);

            // Ambil tanggal
            String date = getDataFromPart(parts[1]);

            // Ambil hari
            String day = getDataFromPart(parts[2]);

            // Ambil tempat
            String place = getDataFromPart(parts[3]);
            String time = "";

            // Jika ada waktu (jam), dapat ditambahkan pemeriksaan lebih lanjut
            if (parts.length > 4) {
                time = getDataFromPart(parts[4]);
                time = time.replace(".", ":");  // Gantilah titik dengan kolon untuk format jam
            }

            // Set data ke tampilan
            tvReminderName.setText("Nama: " + name);
            tvReminderDate.setText("Tanggal: " + date);
            tvReminderDay.setText("Hari: " + day);
            tvReminderTime.setText("Jam: " + time);
            tvReminderPlace.setText("Tempat: " + place);
        }

        return convertView;
    }

    // Helper function untuk mengambil data dari setiap bagian string
    private String getDataFromPart(String part) {
        // Jika part mengandung format waktu (xx:xx) atau format jam lainnya
        if (part != null && part.matches("\\d{2}.\\d{2}")) {
            return part.trim();  // Mengambil waktu secara keseluruhan
        }

        // Jika part mengandung tanda ":"
        String[] splitData = part.split(":");
        if (splitData.length > 1) {
            return splitData[1].trim();  // Mengambil bagian setelah ":"
        }

        return part.trim();  // Jika tidak mengandung ":", kembalikan nilai asli
    }
}
