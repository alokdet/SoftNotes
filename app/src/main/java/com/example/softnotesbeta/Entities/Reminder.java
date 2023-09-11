package com.example.softnotesbeta.Entities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.softnotesbeta.Receivers.ReminderReceiver;

import java.util.Calendar;

@Entity(tableName = "reminders")
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "taskId")
    public long taskId;

    @ColumnInfo(name = "hour")
    public int hour;

    @ColumnInfo(name = "minute")
    public int minute;

    public Reminder(long taskId, int hour, int minute) {
        this.taskId = taskId;
        this.hour = hour;
        this.minute = minute;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void schedule(Context context, String title) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(ReminderReceiver.TITLE, title);


        PendingIntent reminderPendingIntent = PendingIntent.getBroadcast(context, (int) id, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, getHour());
        calendar.set(Calendar.MINUTE, getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), reminderPendingIntent);
            } else {
                Intent intent1 = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                context.startActivity(intent1);
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), reminderPendingIntent);
        }
    }
}
