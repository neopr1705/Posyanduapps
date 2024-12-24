package com.example.posyanduapps.features;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.posyanduapps.R;

public class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Tampilkan notifikasi atau ambil tindakan lain ketika alarm berbunyi
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                    .setContentTitle("Posyandu Alarm")
                    .setContentText("Waktu untuk Posyandu!")
                    .setSmallIcon(R.drawable.ic_alarm) // Ganti dengan ikon yang sesuai
                    .setAutoCancel(true);

            if (notificationManager != null) {
                notificationManager.notify(1, builder.build());
            }
        }
    }




