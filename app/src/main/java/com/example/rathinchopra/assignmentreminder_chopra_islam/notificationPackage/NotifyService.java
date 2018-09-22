package com.example.rathinchopra.assignmentreminder_chopra_islam.notificationPackage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.rathinchopra.assignmentreminder_chopra_islam.MainActivity;
import com.example.rathinchopra.assignmentreminder_chopra_islam.R;

import java.util.Random;

/**
 * Created by Rathin Chopra and Aurnob Islam on 2017-12-30.
 */

public class NotifyService extends Service {

    private String title;
    private boolean notifications;
    private boolean vibrations;

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "Intent";
    // The system notification manager
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        title = intent.getStringExtra("title");

        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification() {

        //vibration pattern
        long[] vibrate = { 0, 100, 200, 300 };

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        getSharedPrefs();

        Notification notify;

        //getting the preferences of teh user and then creating the notification according to that
        if(notifications) {
            if (vibrations) {
                notify = new Notification.Builder(this)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_assignment_late_black_24dp)
                        .setTicker("Notification")
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .setContentTitle("Need to complete " + title)
                        .setOnlyAlertOnce(false)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(vibrate)
                        .getNotification();
            } else {
                notify = new Notification.Builder(this)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_assignment_late_black_24dp)
                        .setTicker("Notification")
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .setContentTitle("Need to complete " + title)
                        .setOnlyAlertOnce(false)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .getNotification();
            }

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Random random = new Random();
            int MY_NOTIFICATION = random.nextInt(9999 - 1000) + 1000;

            manager.notify(MY_NOTIFICATION, notify);
        }
    }

    //getting the preferences of teh user from the SharedPreferences
    public void getSharedPrefs(){
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        notifications = myPrefs.getBoolean("Notification", true);
        vibrations = myPrefs.getBoolean("Vibrate", true);
    }
}
