package com.example.katsutoshi.petsitter.receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.katsutoshi.petsitter.R;
import com.example.katsutoshi.petsitter.util.NotificationUtil;

import java.util.Date;

/**
 * Created by Katsutoshi on 30/05/2017.
 */

public class AlarmeNotificacao extends BroadcastReceiver {
    private static final String TAG = "petsitter";
    private static final String ACTION = "NOTIFICACAO";
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "Notificação:" + new Date());
        Intent notifIntent = new Intent(context, Notification.class);
        NotificationUtil.create(context, notifIntent, "Random", "do it", 1);
    }
}
