package com.example.posyanduapps.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posyanduapps.R;
import com.example.posyanduapps.Helper.DatabaseHelper;
import com.example.posyanduapps.utils.AlarmHelper;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReminderAdapter extends ArrayAdapter<String> {

    private DatabaseHelper databaseHelper;
    private ExecutorService executorService;
    private Handler mainHandler;
    private int currentOption;
    boolean initialSwitchState ;
    private String id="1";

    public ReminderAdapter(Context context, List<String> reminders) {
        super(context, 0, reminders);
        databaseHelper = new DatabaseHelper(context);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("debug now","adapter from usser");
        if (position < 0 || position >= getCount()) {
            return null;  // Validate index
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alarm, parent, false);
        }

        String reminder = getItem(position);
        if (reminder == null || reminder.isEmpty()) {
            return convertView;
        }


        // Extract and display data
        TextView tvTitleNama = convertView.findViewById(R.id.tvTitleNamaAlarm);
        TextView tvReminderName = convertView.findViewById(R.id.tvNamaAlarm);
        TextView tvReminderTime = convertView.findViewById(R.id.tvJamAlarm);
        TextView tvReminderDate = convertView.findViewById(R.id.tvTanggalAlarm);
        TextView tvReminderDay = convertView.findViewById(R.id.tvHariAlarm);
        TextView tvReminderPlace = convertView.findViewById(R.id.tvTempatAlarm);
        Switch swAktif = convertView.findViewById(R.id.swAktif);
        LinearLayout HolderSwitch = convertView.findViewById(R.id.HolderSwitch);

        // Ambil status switch berdasarkan ID alarm
        SharedPreferences AlarmSP = getContext().getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE);
        boolean isAlarmActive = AlarmSP.getBoolean("alarm_status_" + reminder, false); // Default false jika tidak ada data
        swAktif.setChecked(isAlarmActive);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        String roles = sharedPreferences.getString("currentRoles","");
        if (roles.equalsIgnoreCase("admin")) {
            HolderSwitch.setVisibility(View.GONE);
        }else{
            tvTitleNama.setText("ID Pengingat :");
        }


        String[] parts = reminder.split(";");
        if (parts.length >= 5) {
            String name = getDataFromPart(parts[0]);
            String date = getDataFromPart(parts[1]);
            String day = getDataFromPart(parts[2]);
            String place = getDataFromPart(parts[3]);
            String time = parts.length >= 5 ? getDataFromPart(parts[4]).replace(".", ":") : "";

            tvReminderName.setText(name);
            tvReminderDate.setText(date);
            tvReminderDay.setText(day);
            tvReminderTime.setText(time);
            tvReminderPlace.setText(place);
            swAktif.setChecked(initialSwitchState);
        }

        // Switch listener to activate/deactivate alarm
        swAktif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Simpan status switch untuk setiap alarm berdasarkan ID unik
            SharedPreferences AlarmPref = getContext().getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = AlarmPref.edit();
            editor.putBoolean("alarm_status_" + reminder, isChecked);  // Gunakan ID atau identifier unik untuk setiap alarm
            editor.apply(); // J

            if (isChecked) {
                            // Assuming reminder contains both date and time in "tanggal;jam" format
                            LogDebug(""+reminder);
                            String[] reminderParts = reminder.split(";");
                            String tanggal = reminderParts[1]; // First part is the date
                            String jam = reminderParts[4];     // Second part is the time
                            String Hari = reminderParts[2];
                            id = reminderParts [0];
                            String tempat = reminderParts [3];
                            LogDebug(id+tanggal+Hari+tempat+jam);
                            executorService.submit(() -> {
                                boolean alarmSet = databaseHelper.insertAlarm((id.hashCode()),tanggal, jam, 1); // 1 means active
                                mainHandler.post(() -> {
                                    if (alarmSet) {
                                        setAlarm(reminder);
                                        Toast.makeText(getContext(), "Alarm berhasil diset!", Toast.LENGTH_SHORT).show();
                                     } else {
                                        Toast.makeText(getContext(), "Gagal set alarm!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            });

            } else {


                    executorService.submit(() -> {
                        mainHandler.post(() -> {
                            boolean isDeleted = databaseHelper.deleteAlarm(id.hashCode());
                            if (isDeleted) {
                                Toast.makeText(getContext(), "Alarm berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                    deleteAlarm(reminder);
                            } else {
                                Toast.makeText(getContext(), "Gagal hapus alarm!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });



            }
        });


        return convertView;
    }
    private void addNewReminder(String reminder) {
        // Add new reminder to the list
        add(reminder);  // 'add' is a method from ArrayAdapter to add items to the list
        notifyDataSetChanged(); // Notify the adapter to update the UI
    }

    private void removeReminder(String reminder) {
        remove(reminder);  // 'remove' is a method from ArrayAdapter to remove items from the list
        notifyDataSetChanged(); // Notify the adapter to update the UI
    }

    private void deleteAlarm(String reminder){
        String[] parts = reminder.split(";");
        if (parts.length < 5) return;
        AlarmHelper alarmHelper = new AlarmHelper(getContext());
        alarmHelper.cancelAlarm(parts[0]);
        Log.d("ReminderAdapter", "Canceled alarm with id: "+(parts[0]+"\n"+parts[1]+"\n"+parts[2]+"\n"+parts[3]).hashCode());
        boolean deleted = databaseHelper.deleteAlarm(parts[0].hashCode()); // Hapus berdasarkan ID yang hashCode
        if (deleted) {
            Log.d("ReminderAdapter", "Alarm dengan id " + parts[0] + " berhasil dihapus.");
        } else {
            Log.d("ReminderAdapter", "Gagal menghapus alarm dengan id: " + parts[0]);
        }

    }
    private void setAlarm(String reminder) {
        // Parse reminder details
        String[] parts = reminder.split(";");
        if (parts.length < 5) return;

        String time = parts[4].replace(".", ":");
        time = time.replace(" Jam: ","");
        String[] timeParts = time.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            if (!alarmManager.canScheduleExactAlarms()) {
                // Buka pengaturan untuk meminta izin
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                getContext().startActivity(intent);
                Toast.makeText(getContext(), "Aktifkan izin alarm di pengaturan!", Toast.LENGTH_LONG).show();
                return; // Jangan lanjut jika izin belum diberikan
            }
        }

        // Jika izin sudah diberikan, lanjutkan menyetel alarm
        AlarmHelper alarmHelper = new AlarmHelper(getContext());
        LogDebug(""+reminder);
        alarmHelper.setAlarm(parts[0],hour, minute, ("Kontrol Posyandu\n" + parts[1] + " - " + parts[2] + " - " + parts[3])+" - "+parts[4]);
    }


    private String getDataFromPart(String part) {
        return part != null ? part.trim() : "";
    }

    private void LogDebug(String message){
    Log.d("debug now",message);
    }

}
