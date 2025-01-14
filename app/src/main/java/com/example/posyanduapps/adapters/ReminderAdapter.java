package com.example.posyanduapps.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    public ReminderAdapter(Context context, List<String> reminders) {
        super(context, 0, reminders);
        databaseHelper = new DatabaseHelper(context);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        TextView tvReminderName = convertView.findViewById(R.id.tvNamaAlarm);
        TextView tvReminderTime = convertView.findViewById(R.id.tvJamAlarm);
        TextView tvReminderDate = convertView.findViewById(R.id.tvTanggalAlarm);
        TextView tvReminderDay = convertView.findViewById(R.id.tvHariAlarm);
        TextView tvReminderPlace = convertView.findViewById(R.id.tvTempatAlarm);
        Switch swAktif = convertView.findViewById(R.id.swAktif);

        String[] parts = reminder.split(";");
        if (parts.length >= 4) {
            String name = getDataFromPart(parts[0]);
            String date = getDataFromPart(parts[1]);
            String day = getDataFromPart(parts[2]);
            String place = getDataFromPart(parts[3]);
            String time = parts.length > 4 ? getDataFromPart(parts[4]).replace(".", ":") : "";

            tvReminderName.setText(name);
            tvReminderDate.setText(date);
            tvReminderDay.setText(day);
            tvReminderTime.setText(time);
            tvReminderPlace.setText(place);
        }

        // Switch listener to activate/deactivate alarm
        swAktif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Store the initial state of the switch
            final boolean initialSwitchState = swAktif.isChecked();

            if (isChecked) {
                new android.app.AlertDialog.Builder(getContext())
                        .setMessage("Alarm untuk " + reminder + " akan ditambahkan?")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            // Assuming reminder contains both date and time in "tanggal;jam" format
                            String[] reminderParts = reminder.split(";");
                            String tanggal = reminderParts[0]; // First part is the date
                            String jam = reminderParts[1];     // Second part is the time
                            String id= tvReminderName.getText().toString()+tvReminderDate.getText().toString()+tvReminderDay.getText().toString()+tvReminderPlace.getText().toString();
                            int newid = id.hashCode();
                            executorService.submit(() -> {
                                Log.d("ReminderAdapter", "Setting alarm for: "+newid+" " + tanggal + " " + jam);
                                boolean alarmSet = databaseHelper.insertAlarm(newid,tanggal, jam, 1); // 1 means active
                                mainHandler.post(() -> {
                                    if (alarmSet) {
                                        setAlarm(reminder);
                                        Toast.makeText(getContext(), "Alarm berhasil diset!", Toast.LENGTH_SHORT).show();
                                        addNewReminder(reminder);
                                    } else {
                                        Toast.makeText(getContext(), "Gagal set alarm!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            });
                        })
                        .setNegativeButton("Tidak", (dialog, which) -> {
                            swAktif.setChecked(false);
                        })
                        .show();
            } else {

                new android.app.AlertDialog.Builder(getContext())
                        .setMessage("Apakah Anda yakin ingin mematikan alarm ini?" + (tvReminderName.getText().toString() + tvReminderDate.getText().toString() + tvReminderDay.getText().toString() + tvReminderPlace.getText().toString()).hashCode())
                        .setPositiveButton("Ya", (dialog, which) -> {
                            executorService.submit(() -> {
                                mainHandler.post(() -> {
                                    boolean isDeleted = databaseHelper.deleteAlarm((tvReminderName.getText().toString() + tvReminderDate.getText().toString() + tvReminderDay.getText().toString() + tvReminderPlace.getText().toString()).hashCode());
                                    if (isDeleted) {
                                        Toast.makeText(getContext(), "Alarm berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                        // Update the data and notify the adapter to refresh the list
                                        deleteAlarm(reminder);

                                        removeReminder(reminder);
                                    } else {
                                        Toast.makeText(getContext(), "Gagal hapus alarm!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });
                        })
                        .setNegativeButton("Tidak", (dialog, which) -> {
                            // Reset the switch to its initial state if "Tidak" is clicked
                            swAktif.setChecked(false);
                        })
                        .show();

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
        alarmHelper.cancelAlarm(parts[0]+"\n"+parts[1]+"\n"+parts[2]+"\n"+parts[3]);
        Log.d("ReminderAdapter", "Canceled alarm with id: "+(parts[0]+"\n"+parts[1]+"\n"+parts[2]+"\n"+parts[3]).hashCode());


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
        AlarmHelper alarmHelper = new AlarmHelper(getContext());
        alarmHelper.setAlarm(hour,minute,(parts[0]+"\n"+parts[1]+"\n"+parts[2]+"\n"+parts[3]));

    }

    private String getDataFromPart(String part) {
        return part != null ? part.trim() : "";
    }
}
