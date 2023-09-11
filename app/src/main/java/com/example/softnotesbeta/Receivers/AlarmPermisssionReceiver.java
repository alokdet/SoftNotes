package com.example.softnotesbeta.Receivers;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.softnotesbeta.Services.RescheduleRemindersService;

public class AlarmPermisssionReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED.equals(intent.getAction())) {
            startRescheduleReminderService(context);
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
