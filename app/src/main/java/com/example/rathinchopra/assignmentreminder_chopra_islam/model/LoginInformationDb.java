package com.example.rathinchopra.assignmentreminder_chopra_islam.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Rathin Chopra and Aurnob Islam on 2017-12-25.
 */

public class LoginInformationDb {

    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;

    public static final String DB_NAME = "logininfo.db";
    public static final int DB_VERSION = 1;

    public static final String LOGIN_TABLE = "LoginCredentials";

    public static final String ID = "_id";
    public static final int ID_COLUMN = 0;

    public static final String USERNAME = "username";
    public static final int USERNAME_COLUMN = 1;

    public static final String PASSWORD = "password";
    public static final int PASSWORD_COLUMN = 2;

    public static final String CREATE_LOGIN_TABLE =
            "CREATE TABLE " + LOGIN_TABLE + " (" + ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME +
                    " TEXT, " + PASSWORD + " TEXT)";

    public LoginInformationDb(Context context) {
        openHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_LOGIN_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE);
            onCreate(db);
        }
    }

    public LoginInformation saveLoginInfo(LoginInformation loginInformation){

        database = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERNAME, loginInformation.getUsername());
        values.put(PASSWORD, loginInformation.getPassword());

        long dbId = database.insert(LOGIN_TABLE, null, values);

        loginInformation.setDbId(dbId);

        database.close();

        return loginInformation;
    }

    public ArrayList<LoginInformation> getUserByName(String username){

        ArrayList<LoginInformation> loginInformations = new ArrayList<>();

        database = openHelper.getReadableDatabase();

        String selection = "username=?";
        String[] selctionArgs = new String[] {username};

        Cursor result = database.query(LOGIN_TABLE, null, selection, selctionArgs, null, null, null);

        while (result.moveToNext()){
            long dbId = result.getLong(ID_COLUMN);
            String name = result.getString(USERNAME_COLUMN);
            String passkey = result.getString(PASSWORD_COLUMN);

            loginInformations.add(new LoginInformation(name, passkey));
        }

        result.close(); database.close();

        return loginInformations;
    }
}
