package com.example.rathinchopra.assignmentreminder_chopra_islam.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.DatePicker;

import java.util.ArrayList;

/**
 * Created by Rathin Chopra and Aurnob Islam on 2017-12-27.
 */

//this class is database class for the Assignment class
public class AssignmentsDb {

    //declaring the variables

    //SQLiteDatabase and SQLiteOpenHelper
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;

    //Static variables

    //Database name and version
    public static final String DB_NAME = "assignments.db";
    public static final int DB_VERSION = 1;

    //Table name
    public static final String ASSIGNMENTS_TABLE = "AssignmentsTable";

    //id and id column
    public static final String ID = "_id";
    public static final int ID_COLUMN = 0;

    //Assignment name and column
    public static final String ASSIGNMENT_NAME = "assignment";
    public static final int ASSIGNMENT_NAME_COLUMN = 1;

    //due date and column number
    public static final String DUE_DATE = "dueDate";
    public static final int DUE_DATE_COLUMN = 2;

    //Assignment user and column
    public static final String ASSIGNMENT_USER = "assignmentUser";
    public static final int ASSIGNMENT_USER_COLUMN = 3;

    //Creating the assignments table
    public static final String CREATE_ASSIGNMENTS_TABLE =
            "CREATE TABLE " + ASSIGNMENTS_TABLE + " (" + ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + ASSIGNMENT_NAME +
                    " TEXT, " + DUE_DATE + " DATE, " + ASSIGNMENT_USER + " TEXT)";

    public AssignmentsDb(Context context) {
        openHelper = new AssignmentsDb.DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    //This class extend SQLiteOpenHelper
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
        }

        //This is used executing the query
        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_ASSIGNMENTS_TABLE);
        }

        //for upgrading the database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ASSIGNMENTS_TABLE);
            onCreate(db);
        }
    }

    //This function is used for the saving the assignments
    //Parameters:Assignments
    //Returns: Assignments
    public Assignments saveAssignments(Assignments assignments) {

        //OpenHelper
        database = openHelper.getWritableDatabase();

        //putting the values in the database
        ContentValues values = new ContentValues();
        values.put(ASSIGNMENT_NAME, assignments.getAssignmentTitle());
        values.put(DUE_DATE, assignments.getDueDate());
        values.put(ASSIGNMENT_USER, assignments.getUsername());

        //inserting
        long dbId = database.insert(ASSIGNMENTS_TABLE, null, values);

        assignments.setDbId(dbId);

        //Closing the database
        database.close();

        return assignments;
    }

    //This function is used for getting the assignments according to the assignmentUser
    //Parameters: string
    //Result: ArrayList<Assignments>
    public ArrayList<Assignments> getAssignments(String username){

        ArrayList<Assignments> assignments = new ArrayList<>();

        database = openHelper.getReadableDatabase();

        String selection = "assignmentUser=?";
        String[] selectionArgs = new String[] {username};

        //getting the information according to assignmentUser
        Cursor result = database.query(ASSIGNMENTS_TABLE, null, selection, selectionArgs, null, null, null);

        //if there is info then this while loop is executed.
        while (result.moveToNext()){
            long dbId = result.getLong(ID_COLUMN);
            String assignmentTitle = result.getString(ASSIGNMENT_NAME_COLUMN);
            String dueDate = result.getString(DUE_DATE_COLUMN);
            String assignmentUser = result.getString(ASSIGNMENT_USER_COLUMN);

            assignments.add(new Assignments(assignmentTitle, dueDate, username));
        }

        //closing the cursor and database
        result.close();
        database.close();

        return assignments;
    }

    //This function is used for deleting the assignments
    //Parameters: string title
    //Returns: int
    public int deleteAssignmentByTitle(String title){
        database = openHelper.getWritableDatabase();

        String where = "assignment=?";
        String[] whereArgs = new String[] {title};

        int number = database.delete(ASSIGNMENTS_TABLE, where,whereArgs);

        return number;
    }
}
