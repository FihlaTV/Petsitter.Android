package com.example.katsutoshi.petsitter.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.katsutoshi.petsitter.R;

import java.util.List;

/**
 * Created by Katsutoshi on 23/05/2017.
 */

public class NotificationUtil {

    public static final String ACTION_VISUALIZAR = "ACTION_VISUALIZAR";
    public static void create (Context context, Intent intent, String contentTitle, String contentText, int id)
    {
        PendingIntent p = getPendingIntent(context, intent, id);
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);

        b.setDefaults(Notification.DEFAULT_ALL);
        b.setSmallIcon(R.mipmap.ic_dog);
        b.setContentTitle(contentTitle);
        b.setContentText(contentText);
        b.setContentIntent(p);
        b.setAutoCancel(true);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);

        nm.notify(id, b.build());
    }

    public static void createHeadsUpNotification (Context context, Intent intent, String contentTitle, String contentText, int id)
    {
        PendingIntent p = getPendingIntent(context, intent, id);
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);

        b.setDefaults(Notification.DEFAULT_ALL);
        b.setSmallIcon(R.mipmap.ic_dog);
        b.setContentTitle(contentTitle);
        b.setContentText(contentText);
        b.setContentIntent(p);
        b.setAutoCancel(true);

        b.setColor(Color.BLUE);

        b.setFullScreenIntent(p,false);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);

        nm.notify(id, b.build());
    }

    public static void createBig (Context context, Intent intent, String contentTitle, String contentText, List<String> lines, int id)
    {
        PendingIntent p = getPendingIntent(context, intent, id);

        int size = lines.size();
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(contentTitle);

        for(String s: lines)
        {
            inboxStyle.addLine(s);
        }
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);

        b.setDefaults(Notification.DEFAULT_ALL);
        b.setSmallIcon(R.mipmap.ic_dog);
        b.setContentTitle(contentTitle);
        b.setContentText(contentText);
        b.setContentIntent(p);
        b.setAutoCancel(true);
        b.setNumber(size);
        b.setStyle(inboxStyle);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);

        nm.notify(id, b.build());
    }

    public static void createWithAction(Context context, Intent intent, String contentTitle, String contentText, int id)
    {
        PendingIntent p = getPendingIntent(context, intent, id);
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);

        b.setDefaults(Notification.DEFAULT_ALL);
        b.setSmallIcon(R.mipmap.ic_dog);
        b.setContentTitle(contentTitle);
        b.setContentText(contentText);
        b.setContentIntent(p);
        b.setAutoCancel(true);

        PendingIntent actionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_VISUALIZAR), 0);

        b.addAction(R.drawable.ic_action_not, "NÃO", actionIntent);
        b.addAction(R.drawable.ic_action_done, "FEITO", actionIntent);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);

        nm.notify(id, b.build());

    }
    public static void cancell(Context context, int id)
    {
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);

        nm.cancel(id);
    }

    public static void cancellAll(Context context)
    {
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);

        nm.cancelAll();
    }

    private static PendingIntent getPendingIntent(Context context, Intent intent, int id)
    {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(intent.getComponent());
        stackBuilder.addNextIntent(intent);
        PendingIntent p = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

        return p;
    }



}
