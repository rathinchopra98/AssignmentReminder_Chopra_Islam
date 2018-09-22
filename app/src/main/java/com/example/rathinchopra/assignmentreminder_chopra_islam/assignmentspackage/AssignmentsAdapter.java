package com.example.rathinchopra.assignmentreminder_chopra_islam.assignmentspackage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rathinchopra.assignmentreminder_chopra_islam.R;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.Assignments;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.Courses;

import java.util.ArrayList;

/**
 * Created by Rathin Chopra and Aurnob Islam on 2017-12-28.
 */

//this class extends the ArrayAdapter
public class AssignmentsAdapter extends ArrayAdapter<Assignments> {

    //field variables
    private final Context context;
    private final int resource;

    //constructor
    //Parameters: Context context, int resource, ArrayList<Assignments> assignments
    public AssignmentsAdapter(Context context, int resource, ArrayList<Assignments> assignments) {
        super(context, resource, assignments);

        this.context = context;
        this.resource = resource;
    }

    //getting the view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent,false);
        }

        //Local variables.
        TextView txt_assignmentTitle = convertView.findViewById(R.id.txt_listAssignments);
        Assignments assignments = getItem(position);
        txt_assignmentTitle.setText(assignments.getAssignmentTitle());

        TextView txt_dueDate = convertView.findViewById(R.id.txt_listDueDate);
        txt_dueDate.setText(context.getResources().getString(R.string.due_date, String.valueOf(assignments.getDueDate())));

        return convertView;
    }
}
