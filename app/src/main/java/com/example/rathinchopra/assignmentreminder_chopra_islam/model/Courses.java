package com.example.rathinchopra.assignmentreminder_chopra_islam.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rathin Chopra and Aurnob Islam on 2017-12-25.
 */

//This class implements Parcelable
public class Courses implements Parcelable{

    //declaring the field variables

    //int for the id
    private int id;

    //string for the course name
    private String courseName;

    //string for the user name
    private String username;

    //long for the database id
    private long dbId;

    //constructor for this class
    //Parameters: int id, String courseName, String username
    //Returns: void
    public Courses(int id, String courseName, String username){
        this.id = id;
        this.courseName = courseName;
        this.username = username;
    }

    ////Another constructor for the parcing
    //Parameter: Parcel
    //Returns: void
    protected Courses(Parcel in) {
        id = in.readInt();
        courseName = in.readString();
        username = in.readString();
    }

    public static final Creator<Courses> CREATOR = new Creator<Courses>() {
        @Override
        public Courses createFromParcel(Parcel in) {
            return new Courses(in);
        }

        @Override
        public Courses[] newArray(int size) {
            return new Courses[size];
        }
    };

    //getters and setters for all the variables
    public void setId(int ID){
        this.id = ID;
    }

    public void setCourseName(String name){
        this.courseName = name;
    }

    public int getId(){
        return id;
    }

    public String getCourseName(){
        return courseName;
    }

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(courseName);
        dest.writeString(username);
    }
}
