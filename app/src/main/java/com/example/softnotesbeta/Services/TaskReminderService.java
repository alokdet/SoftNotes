package com.example.softnotesbeta.Services;

import static com.example.softnotesbeta.Receivers.ReminderReceiver.TITLE;
import static com.example.softnotesbeta.Application.SoftNotes.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.softnotesbeta.R;
import com.example.softnotesbeta.TaskWorkspace;

public class TaskReminderService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, TaskWorkspace.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        String reminderTitle = intent.getStringExtra(TITLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notify)
                .setContentTitle(reminderTitle)
                .setContentText("Do your task")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        startForeground(1, builder.build());

        /*
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

        } else {
            notificationManager.notify(1, builder.build());
        }
        notificationManager.notify(1, builder.build());
         */

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
