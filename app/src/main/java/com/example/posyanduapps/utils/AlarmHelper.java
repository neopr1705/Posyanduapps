package com.example.posyanduapps.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmHelper {
    private final Context context;

    public AlarmHelper(Context context) {
        this.context = context;
    }

    public void setAlarm(int hour, int minute, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ALARM_MESSAGE", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, message.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            Log.d("AlarmHelper", "Setting alarm for: "+message.hashCode() + hour + ":" + minute);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public void cancelAlarm(String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Menggunakan message.hashCode() sebagai ID unik untuk PendingIntent
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ALARM_MESSAGE", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, message.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.d("AlarmHelper", "Canceled alarm with message: " + message);
        }
    }
}
