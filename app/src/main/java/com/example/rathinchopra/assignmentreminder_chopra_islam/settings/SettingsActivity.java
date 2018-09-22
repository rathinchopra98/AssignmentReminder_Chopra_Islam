package com.example.rathinchopra.assignmentreminder_chopra_islam.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.rathinchopra.assignmentreminder_chopra_islam.R;

public class SettingsActivity extends Activity {

    //Declaring the field variables

    //Swtich for the switch_notification
    private Switch switch_notification;

    //Switch for the switch_vibrate
    private Switch switch_vibrate;

    //TextView for the username
    private TextView txt_userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the view to activity_settings
        setContentView(R.layout.activity_settings);

        //Initialisng the field variables and assigning them their id's
        switch_notification = findViewById(R.id.switch_notification);
        switch_vibrate = findViewById(R.id.switch_vibrate);
        txt_userName = findViewById(R.id.txt_userInfo);

        //setting the onCheckedChangeListener for both switches
        switch_notification.setOnCheckedChangeListener(onSwitchNotificationCheck);
        switch_vibrate.setOnCheckedChangeListener(onSwitchVibrateCheck);

        //calling the metod load prefs
        loadPrefs();
    }

    //this function gets the SharedPreferences
    //Parameters: none
    //Returns: void
    public void loadPrefs(){
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        txt_userName.setText(getResources().getString(R.string.user_name, myPrefs.getString("User", null)));
        switch_notification.setChecked(myPrefs.getBoolean("Notification", true));
        switch_vibrate.setChecked(myPrefs.getBoolean("Vibrate", true));
    }

    //OnCheckedChangeListener for the Vibrations
    private CompoundButton.OnCheckedChangeListener onSwitchVibrateCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            //it save teh data in SharedPreferences
            SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.putBoolean("Vibrate", switch_vibrate.isChecked()).apply();
        }
    };

    //OnCheckedChangeListener for the notifications
    private CompoundButton.OnCheckedChangeListener onSwitchNotificationCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            //it save teh data in SharedPreferences
            SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.putBoolean("Notification", switch_notification.isChecked()).apply();
        }
    };
}
