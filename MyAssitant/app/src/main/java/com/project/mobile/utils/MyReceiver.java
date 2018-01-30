package com.project.mobile.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import com.project.mobile.myassitant.MainActivity;
import java.util.Random;

/**
 * Use to
 * Created by DzungVu on 8/26/2017.
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("TITLE");
        String message = intent.getStringExtra("MESSAGE");
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Random random = new Random();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        if (title != null) {
            builder.setContentTitle(title);
        } else {
            builder.setContentTitle("Notification");
        }
        if (message != null) {
            builder.setContentText(message);
        } else {
            builder.setContentText("Notification message");
        }
        builder.setSound(uri);
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        builder.setLights(0xFFb71c1c, 1000, 2000);
        builder.setContentIntent(pendingIntent);
        int ran = random.nextInt(100000);
        notificationManager.notify(ran, builder.build());
    }
}
