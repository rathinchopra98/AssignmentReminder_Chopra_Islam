package com.example.rathinchopra.assignmentreminder_chopra_islam.assignmentspackage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rathinchopra.assignmentreminder_chopra_islam.R;
import com.example.rathinchopra.assignmentreminder_chopra_islam.settings.SettingsActivity;
import com.example.rathinchopra.assignmentreminder_chopra_islam.slatelogin.SlateLoginForAssignment;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.Assignments;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.AssignmentsDb;
import com.example.rathinchopra.assignmentreminder_chopra_islam.notificationPackage.ScheduleClient;

import java.util.ArrayList;
import java.util.Calendar;

public class AssignmentsActivity extends Activity {

    //field variables

    //ListView for the courses
    private ListView listView_assignments;

    //string for the usrename
    private String username;

    //Schedule client
    private ScheduleClient scheduleClient;

    private int id_slate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the view
        setContentView(R.layout.activity_assignments);

        //initialising the variables
        listView_assignments = findViewById(R.id.listView_assignments);
        Button btn_Assignment = findViewById(R.id.btn_Assignment);

        //setting the listeners
        btn_Assignment.setOnClickListener(onAddAssignmentClick);
        listView_assignments.setOnItemLongClickListener(onAssignmentLongClick);

        //initiliasing teh schedule client
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        //calling the methods
        GetIntentExtra();
        ListViewInflater();
    }

    //onCreateOptionsMenu() gets the inflator and mak the menu bar.
    //in this creating a custom menu
    //Parameters: Menu
    //returns: boolean
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assignment, menu);
        return true;
    }

    //This function is called when an item is click from the top menu bar
    //Parameters: MenuItem
    //Returns: boolean
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.item_loadSlate){
            Intent intent = new Intent(getApplicationContext(), SlateLoginForAssignment.class);
            intent.putExtra("PrimaryKeyForTable", username);
            intent.putExtra("id", id_slate);
            startActivityForResult(intent, 1);
            return true;
        } else if(id == R.id.item_settings){
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if(id == android.R.id.home){
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //listener for the Add assignments button
    private View.OnClickListener onAddAssignmentClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //staring the intent
            Intent intent = new Intent(getApplicationContext(), AddingAssignmentActivity.class);
            intent.putExtra("courseUser", username);
            startActivityForResult(intent, 0);
        }
    };

    //long press listener for the list view
    private AdapterView.OnItemLongClickListener onAssignmentLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final Assignments tempAssign = (Assignments) parent.getAdapter().getItem(position);

            //calling alertDialogs
            alertDialogs(tempAssign.getAssignmentTitle());

            return false;
        }
    };

    //this method deletes the courses when long pressed
    //parameters: String assignmentName
    //returns: void
    public void alertDialogs(final String assignmentName){
        AlertDialog.Builder dialog = new AlertDialog.Builder(AssignmentsActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Deleting " + assignmentName);
        dialog.setMessage("Are you sure you want to delete this entry?" );
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                int number;

                AssignmentsDb assignmentsDb = new AssignmentsDb(getApplicationContext());
                number = assignmentsDb.deleteAssignmentByTitle(assignmentName);

                Toast.makeText(getApplicationContext(), "Assignment Deleted.", Toast.LENGTH_SHORT).show();

                ListViewInflater();
            }
        })
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListViewInflater();
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    //getting the extras from intent
    public void GetIntentExtra(){
        Intent intent = getIntent();
        username = intent.getStringExtra("courseUser");
        id_slate = intent.getIntExtra("id", 0);
    }

    //List view inflator
    //parameteres: none
    //return: void
    public void ListViewInflater(){
        AssignmentsDb assignmentsDb = new AssignmentsDb(this);
        ArrayList<Assignments> assignmentsArrayList = assignmentsDb.getAssignments(username);
        AssignmentsAdapter adapter = new AssignmentsAdapter(this, R.layout.listassignments, assignmentsArrayList);
        listView_assignments.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }

    //this function is called for setting the notification on specific date
    //parameter: String date, String title
    //returns: void
    public void settingNotification(String date, String title){

        try{
            String[] separated = date.split("/");

            int day = Integer.parseInt(separated[0]);
            int month = Integer.parseInt(separated[1]);
            int year = Integer.parseInt(separated[2]);
            // Create a new calendar set to the date chosen
            // we set the time to midnight (i.e. the first minute of that day)
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            c.set(Calendar.HOUR_OF_DAY, 10);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
            scheduleClient.setAlarmForNotification(c, title);

            Toast.makeText(getApplicationContext(), "Notification set for: "+ day +"/"+ (month+1) +"/"+ year, Toast.LENGTH_SHORT).show();

        } catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "Cannot make notification for " + title, Toast.LENGTH_SHORT).show();
        }
    }

    //getting the activity result and displaying it
    //Parameters: int requestCode, int resultCode, Intent data
    //return: void
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                ArrayList<Assignments> assignments = data.getParcelableArrayListExtra("assignments");
                for(int x = 0; x < assignments.size(); x++){
                    Assignments tempAssign = new Assignments(assignments.get(x).getAssignmentTitle(),
                            assignments.get(x).getDueDate(), assignments.get(x).getUsername());
                    AssignmentsDb assignmentsDb = new AssignmentsDb(getApplicationContext());
                    tempAssign = assignmentsDb.saveAssignments(tempAssign);

                    settingNotification(tempAssign.getDueDate(), tempAssign.getAssignmentTitle());
                }
            }
            ListViewInflater();
        }
    }
}
