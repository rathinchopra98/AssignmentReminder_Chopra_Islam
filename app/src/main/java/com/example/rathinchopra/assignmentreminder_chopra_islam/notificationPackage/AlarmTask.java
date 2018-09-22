package com.example.rathinchopra.assignmentreminder_chopra_islam.notificationPackage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Rathin Chopra and Aurnob Islam on 2017-12-30.
 * resource: http://blog.blundellapps.co.uk/
 */

public class AlarmTask implements Runnable {

    // The date selected for the alarm
    private final Calendar date;
    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;

    private String title;

    public AlarmTask(Context context, Calendar date, String title) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
        this.title = title;
    }

    @Override
    public void run() {
        // Request to start are service when the alarm date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra("Intent", true);
        intent.putExtra("title", title);
        Random random = new Random();
        int MY_NOTIFICATION = random.nextInt(9999 - 1000) + 1000;
        PendingIntent pendingIntent = PendingIntent.getService(context, MY_NOTIFICATION, intent, 0);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);

    }
}
