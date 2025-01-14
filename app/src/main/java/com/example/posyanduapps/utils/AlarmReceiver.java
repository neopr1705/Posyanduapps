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
import android.widget.Toast;

import com.example.posyanduapps.R;

public class AlarmReceiver extends BroadcastReceiver {

    private static MediaPlayer mediaPlayer; // MediaPlayer bersifat statis agar dapat diakses di seluruh siklus aplikasi

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("ACTION");

        // Menangani aksi berdasarkan intent
        if (action != null) {
            handleAction(context, action, intent);
        } else {
            // Jika tidak ada aksi, berarti alarm baru diterima
            handleNewAlarm(context, intent);
        }
    }

    private void handleAction(Context context, String action, Intent intent) {
        if ("OFF".equals(action)) {
            turnOffAlarm(context);
        } else if ("OK".equals(action)) {
            keepAlarmActive(context);
        }
    }

    private void turnOffAlarm(Context context) {
        stopAlarmSound();
        cancelNotification(context);
        showToast(context, "Alarm dimatikan");
    }

    private void keepAlarmActive(Context context) {
        showToast(context, "Alarm tetap aktif");
    }

    private void handleNewAlarm(Context context, Intent intent) {
        String message = intent.getStringExtra("ALARM_MESSAGE");
        String alarmTime = intent.getStringExtra("ALARM_TIME");

        showToast(context, "Alarm: " + message);
        playAlarmSound(context);
        showNotification(context, message, alarmTime);
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

    private void cancelNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1); // Menghilangkan notifikasi dengan ID 1
    }

    private void showNotification(Context context, String message, String alarmTime) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "ALARM_CHANNEL",
                    "Alarm Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        PendingIntent offPendingIntent = createPendingIntent(context, "OFF");
        PendingIntent okPendingIntent = createPendingIntent(context, "OK");

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context, "ALARM_CHANNEL")
                    .setContentTitle("Alarm!")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setAutoCancel(false) // Notifikasi tetap aktif sampai alarm dimatikan
                    .addAction(-1, "Matikan", offPendingIntent)
                    .addAction(-1, "OK", okPendingIntent)
                    .build();
        }

        notificationManager.notify(1, notification);
    }

    private PendingIntent createPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ACTION", action);
        return PendingIntent.getBroadcast(context, action.equals("OFF") ? 0 : 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
