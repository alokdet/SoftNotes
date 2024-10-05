package com.example.softnotesbeta;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

public class NotificationHelper {
    private final String CHANNEL_ID = "REMINDERS NOTIFICATION";
    private final int NOTIFICATION_ID = Integer.parseInt(String.valueOf(new Date().getTime()).substring(String.valueOf(new Date().getTime()).length() - 5));

    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    public void createNotification(String title) {
        createNotificationChannel();

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_alarm)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_alarm))
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notifications to remind users for their tasks");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
