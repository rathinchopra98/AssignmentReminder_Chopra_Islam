package com.example.rathinchopra.assignmentreminder_chopra_islam.coursesPackage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rathinchopra.assignmentreminder_chopra_islam.R;
import com.example.rathinchopra.assignmentreminder_chopra_islam.settings.SettingsActivity;
import com.example.rathinchopra.assignmentreminder_chopra_islam.slatelogin.SlateLoginActivity;
import com.example.rathinchopra.assignmentreminder_chopra_islam.assignmentspackage.AssignmentsActivity;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.Courses;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.CoursesDb;

import java.util.ArrayList;

public class CoursesActivity extends Activity {

    //field variables

    //ListView for the courses
    private ListView listView_courses;

    //string for the username
    private String username;

    //Arraylist
    private ArrayList<Courses> courses;

    //int for counting
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the view
        setContentView(R.layout.activity_courses);

        //Initialising the variables
        listView_courses = findViewById(R.id.listView_courses);
        Button btn_addCourses = findViewById(R.id.btn_addCourses);

        //Setting the onclick listeners
        btn_addCourses.setOnClickListener(onAddCourseClick);
        listView_courses.setOnItemClickListener(onListViewItemClick);
        listView_courses.setOnItemLongClickListener(onCourseLongClick);

        //loading Prefs
        loadPrefrences();

        //first Time the user gets the alert message
        if(counter == 0){
            displayInfo();
            counter++;
            saveSharedPrefrences();
        }

        //calling methods
        GetIntentExtra();
        ListViewInflater();
    }

    //onCreateOptionsMenu() gets the inflator and mak the menu bar.
    //in this creating a custom menu
    //Parameters: Menu
    //returns: boolean
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //This function is called when an item is click from the top menu bar
    //Parameters: MenuItem
    //Returns: boolean
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.item_loadSlate){
            Intent intent = new Intent(getApplicationContext(), SlateLoginActivity.class);
            intent.putExtra("PrimaryKeyForTable", username);
            startActivityForResult(intent, 1);
            return true;
        } else if(id == R.id.item_settings){
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //This function takes the user to android screen
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    //listener for the Add course button
    private View.OnClickListener onAddCourseClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //starting an intent when this button is clicked
            Intent intent = new Intent(getApplicationContext(), AddingCoursesActivity.class);
            //passing the info
            intent.putExtra("PrimaryKeyForTable", username);
            startActivityForResult(intent, 0);
        }
    };

    //listener for the listview
    private AdapterView.OnItemClickListener onListViewItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //when a respective item is clicked take to the assignment activity
            Courses tempCourse = (Courses) parent.getAdapter().getItem(position);
            Intent intent = new Intent(getApplicationContext(), AssignmentsActivity.class);
            intent.putExtra("courseUser", username + tempCourse.getCourseName());
            intent.putExtra("id", tempCourse.getId());
            startActivity(intent);
        }
    };

    //long click listener for listview
    private AdapterView.OnItemLongClickListener onCourseLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final Courses tempCourse = (Courses) parent.getAdapter().getItem(position);

            //calling the alertDialog method
            alertDialogs(tempCourse.getCourseName());

            return false;
        }
    };

    //this method deletes the courses when long pressed
    //parameters: String courseName
    //returns: void
    public void alertDialogs(final String courseName){
        AlertDialog.Builder dialog = new AlertDialog.Builder(CoursesActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Deleting " + courseName);
        dialog.setMessage("Are you sure you want to delete this entry?" );
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                int number;

                //delteing the course
                CoursesDb coursesDb = new CoursesDb(getApplicationContext());
                number = coursesDb.deleteCoursesByName(courseName);

                Toast.makeText(getApplicationContext(), "Course Deleted.", Toast.LENGTH_SHORT).show();

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

    //saving the prefs
    public void saveSharedPrefrences(){
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putInt("counter", counter).apply();
    }

    //loading the prefs
    public void loadPrefrences(){
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        counter  = myPrefs.getInt("counter", 0);
    }

    //This methods displays the info for the first user
    //Parameters: none
    //returns: void
    public void displayInfo(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Information").setMessage(
                        "You can add courses from slate by clicking on load from slate in menubar");
        dialog.setPositiveButton("Confirm", null);
        final AlertDialog alert = dialog.create();
        alert.show();

        new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                alert.dismiss();
            }
        }.start();
    }

    //getting the extras from the intent
    public void GetIntentExtra(){
        Intent intent = getIntent();
        username = intent.getStringExtra("nameForDb");
    }

    //This method inflates the listview
    //parameters: none
    //returns: void
    public void ListViewInflater(){

        //getting the arraylist from the SQL
        CoursesDb coursesDb = new CoursesDb(this);
        ArrayList<Courses> coursesArrayList = coursesDb.getCourses(username);
        CoursesAdapter adapter = new CoursesAdapter(this, R.layout.listcourses, coursesArrayList);
        listView_courses.setAdapter(adapter);
    }

    //getting the activity result and displaying it
    //Parameters: int requestCode, int resultCode, Intent data
    //return: void
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                courses = data.getParcelableArrayListExtra("courses");
                for(int x = 0; x < courses.size(); x++){
                    Courses tempCourse = new Courses(courses.get(x).getId(), courses.get(x).getCourseName(), courses.get(x).getUsername());
                    CoursesDb coursesDb = new CoursesDb(getApplicationContext());
                    tempCourse = coursesDb.saveCourses(tempCourse);
                }
            }
            ListViewInflater();
        }
    }
}
