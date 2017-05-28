package com.example.katsutoshi.petsitter.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Katsutoshi on 26/05/2017.
 */

public class AlarmUtil {

    private static final String TAG = "petsitter";
    //agenda alarme com data/hora informado
    public static void schedule(Context context, Intent intent, Long triggerAtMillis)
    {
        PendingIntent p = PendingIntent.getBroadcast(context,1, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, p);

        Log.d("petsitter-alarm", "Alarme agendado com sucesso.");
    }
    //agenda o alarme com opção para repetir
    public static void scheduleRepeat(Context context, Intent intent, Long triggerAtMillis, Long intervalMillis)
    {
        PendingIntent p = PendingIntent.getBroadcast(context,1, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis,intervalMillis, p);

        Log.d("petsitter-alarm", "Alarme agendado com sucesso com repeat.");
    }
    //cancela o alarme
    public static void cancel(Context context, Intent intent)
    {
        PendingIntent p = PendingIntent.getBroadcast(context,1, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        alarm.cancel(p);
        Log.d("petsitter-alarm", "Alarme cancelado.");
    }

}
