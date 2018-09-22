package com.example.rathinchopra.assignmentreminder_chopra_islam.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Rathin Chopra and Aurnob Islam on 2017-12-25.
 */

//this class is database class for the Courses class
public class CoursesDb {

    //declaring the variables

    //SQLiteDatabase and SQLiteOpenHelper
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;

    //Database name and version
    public static final String DB_NAME = "courses.db";
    public static final int DB_VERSION = 1;

    //Table name
    public static final String COURSES_TABLE = "Courses";

    //id and id column
    public static final String ID = "_id";
    public static final int ID_COLUMN = 0;

    //course name and the cloumn number
    public static final String COURSE_NAME = "course";
    public static final int COURSE_NAME_COLUMN = 1;

    //course id and column number
    public static final String COURSE_ID = "courseId";
    public static final int COURSE_ID_COLUMN = 2;

    //course user and the column number
    public static final String COURSE_USER = "courseUser";
    public static final int COURSE_USER_COLUMN = 3;

    //Creating the courses table
    public static final String CREATE_COURSES_TABLE =
            "CREATE TABLE " + COURSES_TABLE + " (" + ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + COURSE_NAME +
                    " TEXT, " + COURSE_ID + " TEXT, " + COURSE_USER + " TEXT)";

    public CoursesDb(Context context) {
        openHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    //This class extend SQLiteOpenHelper
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
        }

        //This is used in executing the query
        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_COURSES_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + COURSES_TABLE);
            onCreate(db);
        }
    }

    //This function is used for the saving the courses
    //Parameters:courses
    //Returns: courses
    public Courses saveCourses(Courses courses) {

        database = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COURSE_NAME, courses.getCourseName());
        values.put(COURSE_ID, courses.getId());
        values.put(COURSE_USER, courses.getUsername());

        long dbId = database.insert(COURSES_TABLE, null, values);

        courses.setDbId(dbId);

        database.close();

        return courses;
    }

    //This function is used for getting the courses
    //Parameters: string
    //Result: ArrayList<courses>
    public ArrayList<Courses> getCourses(String username){

        ArrayList<Courses> courses = new ArrayList<>();

        database = openHelper.getReadableDatabase();

        String selection = "courseUser=?";
        String[] selectionArgs = new String[] {username};

        //getting the information according to courseUser
        Cursor result = database.query(COURSES_TABLE, null, selection, selectionArgs, null, null, null);

        while (result.moveToNext()){
            long dbId = result.getLong(ID_COLUMN);
            String courseName = result.getString(COURSE_NAME_COLUMN);
            String courseId = result.getString(COURSE_ID_COLUMN);
            String studentName = result.getString(COURSE_USER_COLUMN);

            courses.add(new Courses(Integer.parseInt(courseId), courseName, studentName));
        }

        //closing result and database
        result.close(); database.close();

        return courses;
    }

    //This function is used for deleting the courses
    //Parameters: string title
    //Returns: int
    public int deleteCoursesByName(String name){
        database = openHelper.getWritableDatabase();

        String where = "course=?";
        String[] whereArgs = new String[] {name};

        int number = database.delete(COURSES_TABLE, where,whereArgs);

        return number;
    }
}
