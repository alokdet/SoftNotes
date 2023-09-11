package com.example.softnotesbeta.Application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.softnotesbeta.Database.DatabaseManager;

public class SoftNotes extends Application {

    public static final String CHANNEL_ID = "REMINDER_CHANNEL";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Reminder", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notification to remind about tasks.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
