package com.example.rathinchopra.assignmentreminder_chopra_islam.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rathin Chopra and Aurnob Islam on 2017-12-27.
 */

//This class implements Parcelable
public class Assignments implements Parcelable{

    //declaring the field variables

    //String for the assignment title
    private String assignmentTitle;

    //due date
    private String dueDate;

    //user's name
    private String username;

    //Database id
    private long dbId;

    //constructor for this class
    //Parameters: String assignmentTitle, String dueDate, String username
    //Returns: void
    public Assignments(String assignmentTitle, String dueDate, String username){
        this.assignmentTitle = assignmentTitle;
        this.dueDate = dueDate;
        this.username = username;
    }

    //Another constructor for the parcing
    //Parameter: Parcel
    //Returns: void
    protected Assignments(Parcel in) {
        assignmentTitle = in.readString();
        dueDate = in.readString();
        username = in.readString();
        dbId = in.readLong();
    }

    public static final Creator<Assignments> CREATOR = new Creator<Assignments>() {
        @Override
        public Assignments createFromParcel(Parcel in) {
            return new Assignments(in);
        }

        @Override
        public Assignments[] newArray(int size) {
            return new Assignments[size];
        }
    };

    //getter for the username
    //Parameters: none
    //returns: string username
    public String getUsername() {
        return username;
    }

    //setter for the username
    //Parameter: String
    //Returns: void
    public void setUsername(String username) {
        this.username = username;
    }

    //getter for the assignmentTitle
    //Parameters: none
    //returns: String assignmentTitle
    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    //setter for the assignmentTitle
    //Parameter: String
    //Returns: void
    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    //getter for the dueDate
    //Parameters: none
    //returns: String dueDate
    public String getDueDate() {
        return dueDate;
    }

    //setter for the dueDate
    //Parameter: String
    //Returns: void
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    //setter for the dBId
    //Parameter: long
    //Returns: void
    public void setDbId(long dbId){
        this.dbId = dbId;
    }

    //getter for the dBId
    //Parameters: none
    //returns: String dBId
    public long getDbId(){
        return dbId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(assignmentTitle);
        dest.writeString(dueDate);
        dest.writeString(username);
        dest.writeLong(dbId);
    }
}
