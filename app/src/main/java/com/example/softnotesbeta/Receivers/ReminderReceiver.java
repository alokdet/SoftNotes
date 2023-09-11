package com.example.softnotesbeta.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.softnotesbeta.Services.RescheduleRemindersService;
import com.example.softnotesbeta.Services.TaskReminderService;

public class ReminderReceiver extends BroadcastReceiver {

    public static final String TITLE = "TITLE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            startRescheduleReminderService(context);
        } else {
            startRemindingService(context, intent);
        }
    }

    private void startRemindingService(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, TaskReminderService.class);
        serviceIntent.putExtra(TITLE, intent.getStringExtra(TITLE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }

    private void startRescheduleReminderService(Context context) {
        Intent intent = new Intent(context, RescheduleRemindersService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }
}
