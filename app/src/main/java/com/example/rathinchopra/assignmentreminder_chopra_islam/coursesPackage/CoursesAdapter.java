package com.example.rathinchopra.assignmentreminder_chopra_islam.coursesPackage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rathinchopra.assignmentreminder_chopra_islam.R;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.Courses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rathin Chopra and Aurnob Islam on 2017-12-26.
 */

//this class extends the ArrayAdapter
public class CoursesAdapter extends ArrayAdapter<Courses> {

    //field variables
    private final Context context;
    private final int resource;

    //constructor
    //Parameters: Context context, int resource, ArrayList<Courses> courses
    public CoursesAdapter(Context context, int resource, ArrayList<Courses> courses) {
        super(context, resource, courses);

        this.context = context;
        this.resource = resource;
    }

    //getting the view
    //Parameters: int position, View convertView, ViewGroup parent
    //Result: View
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent,false);
        }

        //Local variables.
        TextView txt_courseName = convertView.findViewById(R.id.txt_listCourse);
        Courses courses = getItem(position);
        txt_courseName.setText(context.getResources().getString(R.string.course_display, String.valueOf(courses.getCourseName())));

        TextView txt_courseId = convertView.findViewById(R.id.txt_listCourseId);
        txt_courseId.setText(context.getResources().getString(R.string.id_display, String.valueOf(courses.getId())));

        return convertView;
    }
}
