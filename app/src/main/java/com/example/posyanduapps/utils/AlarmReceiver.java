package com.example.posyanduapps.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.posyanduapps.R;

public class AlarmReceiver extends BroadcastReceiver {

    private static MediaPlayer mediaPlayer; // MediaPlayer bersifat statis agar dapat diakses di seluruh siklus aplikasi

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("ACTION");

        Log.d("AlarmReceiver", "onReceive: ACTION = " + action); // Tambahkan log untuk debugging

        if (action != null) {
            handleAction(context, intent, action); // ✅ Kirim intent sebagai parameter

        } else {
            handleNewAlarm(context, intent);
        }
    }

    private void handleAction(Context context, Intent intent, String action) {
        Log.d("AlarmReceiver", "handleAction: Received action = " + action);

        String alarmId = intent.getStringExtra("ALARM_ID"); // ✅ Sekarang intent sudah tersedia

        if ("OFF".equals(action)) {
            turnOffAlarm(context, alarmId);
        } else if ("OK".equals(action)) {
            keepAlarmActive(context);
        } else {
            Log.d("AlarmReceiver", "Unknown action: " + action);
        }
    }
    private void turnOffAlarm(Context context, String alarmId) {
        Log.d("AlarmReceiver", "Turning off alarm with ID: " + alarmId);
        stopAlarmSound();
        cancelNotification(context, alarmId);
        cancelAlarm(context, alarmId);
        showToast(context, "Alarm " + alarmId + " dimatikan");
    }

    private void cancelAlarm(Context context, String alarmId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent cancelIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, Integer.parseInt(alarmId), cancelIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent); // Membatalkan alarm berdasarkan ID spesifik
            Log.d("AlarmReceiver", "Alarm cancelled with ID: " + alarmId);
        }
    }

    private void keepAlarmActive(Context context) {
        Log.d("AlarmReceiver", "Keeping the alarm active"); // Tambahkan log saat alarm tetap aktif
        showToast(context, "Alarm tetap aktif");
    }

    private void handleNewAlarm(Context context, Intent intent) {
        String message = intent.getStringExtra("ALARM_MESSAGE");
        String alarmId = intent.getStringExtra("ALARM_ID");

        Log.d("AlarmReceiver", "Alarm triggered! ID: " + alarmId + ", Message: " + message);

        showToast(context, "Alarm: " + message);
        playAlarmSound(context);
        showNotification(context, message, alarmId);
    }

    private void playAlarmSound(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.alarm_1);
            mediaPlayer.setLooping(true); // Aktifkan mode loop agar suara terus dimainkan
            mediaPlayer.start();
        }
    }

    private void stopAlarmSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null; // Hapus referensi MediaPlayer
        }
    }

    private void cancelNotification(Context context, String alarmId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Integer.parseInt(alarmId));
    }

    private void showNotification(Context context, String message, String alarmId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "ALARM_CHANNEL",
                    "Alarm Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        PendingIntent offPendingIntent = createPendingIntent(context, "OFF", alarmId, 1);
        PendingIntent okPendingIntent = createPendingIntent(context, "OK", alarmId, 2);

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context, "ALARM_CHANNEL")
                    .setContentTitle("Alarm!")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setAutoCancel(false)
                    .addAction(-1, "Matikan", offPendingIntent)
                    .addAction(-1, "OK", okPendingIntent)
                    .build();
        }

        notificationManager.notify(Integer.parseInt(alarmId), notification); // Gunakan alarmId agar unik
    }


    private PendingIntent createPendingIntent(Context context, String action, String alarmId, int requestCode) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ACTION", action);
        intent.putExtra("ALARM_ID", alarmId);
        return PendingIntent.getBroadcast(context, Integer.parseInt(alarmId) + requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }




    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
