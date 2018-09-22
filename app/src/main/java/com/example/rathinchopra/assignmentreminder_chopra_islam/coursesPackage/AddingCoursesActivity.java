package com.example.rathinchopra.assignmentreminder_chopra_islam.coursesPackage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rathinchopra.assignmentreminder_chopra_islam.R;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.Courses;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.CoursesDb;

public class AddingCoursesActivity extends Activity {

    //field variables
    private EditText edtTxt_courseName;
    private EditText edtTxt_courseId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the view
        setContentView(R.layout.activity_adding_courses);

        //initialising the variables
        Button btn_add = findViewById(R.id.btn_addCourse);
        edtTxt_courseName = findViewById(R.id.edtTxt_courseName);
        edtTxt_courseId = findViewById(R.id.edtTxt_courseId);

        //setting lsitener for the button
        btn_add.setOnClickListener(onAddCourseClick);
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

    //this function add a new course in the listview
    private View.OnClickListener onAddCourseClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //calling the database class and saving the info
            Courses courses = new Courses(Integer.parseInt(edtTxt_courseId.getText().toString()),
                    edtTxt_courseName.getText().toString(), username);
            CoursesDb coursesDb = new CoursesDb(getApplicationContext());
            courses = coursesDb.saveCourses(courses);

            Toast.makeText(getApplicationContext(), "Course Created", Toast.LENGTH_SHORT).show();

            //setting the result
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    //getting the extras from intent
    public void GetIntentExtra(){
        Intent intent = getIntent();
        username = intent.getStringExtra("PrimaryKeyForTable");
    }
}
