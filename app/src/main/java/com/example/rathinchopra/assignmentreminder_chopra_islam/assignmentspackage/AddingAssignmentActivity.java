package com.example.rathinchopra.assignmentreminder_chopra_islam.assignmentspackage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rathinchopra.assignmentreminder_chopra_islam.R;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.Assignments;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.AssignmentsDb;
import com.example.rathinchopra.assignmentreminder_chopra_islam.notificationPackage.ScheduleClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddingAssignmentActivity extends Activity {

    //field variables

    //string for iusername
    private String username;

    //EditText for assignments
    private EditText edtTxt_assignment;

    //due date
    private EditText edtTxt_dueDate;

    // This is a handle so that we can call methods on our service
    private ScheduleClient scheduleClient;
    // This is the date picker used to select the date for our notification
    private DatePicker picker;

    private int dayForCalendar;
    private int monthForCalendar;
    private int yearForCalendar;

    //my calender for teh datte
    private Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the view
        setContentView(R.layout.activity_adding_assignment);

        //initialisng the scheduleclient
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        //initialising the variables
        Button btn_addAssignments = findViewById(R.id.btn_addAssignment);
        edtTxt_assignment = findViewById(R.id.edtTxt_assignment);
        edtTxt_dueDate = findViewById(R.id.edtTxt_dueDate);
        picker = findViewById(R.id.picker);

        //setting the listener for the buttons
        btn_addAssignments.setOnClickListener(onAddAssignmentClick);
        edtTxt_dueDate.setOnClickListener(onPopUpCalendarClick);

        GetIntentExtra();
    }

    //This function is called when an item is click from the top menu bar
    //Parameters: MenuItem
    //Returns: boolean
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    //listener for assignments click
    private View.OnClickListener onAddAssignmentClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //calling onAddingAssignments
            onAddingAssignments();
        }
    };

    //listener for date set
    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dayForCalendar = dayOfMonth;
            monthForCalendar = monthOfYear;
            yearForCalendar = year;
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    //listener for pop up calendar
    private View.OnClickListener onPopUpCalendarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(AddingAssignmentActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }

    //this funtion calls the database and adds a new assignment in t hat course
    public void onAddingAssignments(){
        if(!edtTxt_assignment.getText().toString().equals("")) {
            settingNotification();

            Assignments assignments = new Assignments(edtTxt_assignment.getText().toString(),
                    edtTxt_dueDate.getText().toString(), username);

            AssignmentsDb assignmentsDb = new AssignmentsDb(getApplicationContext());
            assignments = assignmentsDb.saveAssignments(assignments);

            Toast.makeText(getApplicationContext(), "Assignment Created", Toast.LENGTH_SHORT).show();

            //starting intent
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else{
            Toast.makeText(getApplicationContext(), "Please enter information in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    //this function is called for setting the notification on specific date
    //parameter: String date, String title
    //returns: void
    public void settingNotification(){

        int day = picker.getDayOfMonth();
        int month = picker.getMonth();
        int year = picker.getYear();
        // Create a new calendar set to the date chosen
        // we set the time to midnight (i.e. the first minute of that day)
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        c.set(Calendar.HOUR_OF_DAY, 10);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service

        scheduleClient.setAlarmForNotification(c, edtTxt_assignment.getText().toString());

        Calendar secondCalendar = Calendar.getInstance();
        secondCalendar.set(yearForCalendar, monthForCalendar, dayForCalendar);
        scheduleClient.setAlarmForNotification(secondCalendar, edtTxt_assignment.getText().toString());

        Toast.makeText(getApplicationContext(), "Notification set for: "+ day +"/"+ (month+1) +"/"+ year, Toast.LENGTH_SHORT).show();

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edtTxt_dueDate.setText(sdf.format(myCalendar.getTime()));
    }

    //getting the intent extras
    public void GetIntentExtra(){
        Intent intent = getIntent();
        username = intent.getStringExtra("courseUser");
    }

}
