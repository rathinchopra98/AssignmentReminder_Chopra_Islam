package com.example.rathinchopra.assignmentreminder_chopra_islam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rathinchopra.assignmentreminder_chopra_islam.accountlogin.CreateAccountActivity;
import com.example.rathinchopra.assignmentreminder_chopra_islam.coursesPackage.CoursesActivity;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.LoginInformation;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.LoginInformationDb;
import com.example.rathinchopra.assignmentreminder_chopra_islam.settings.SettingsActivity;

import java.util.ArrayList;

public class MainActivity extends Activity {

    //declaring field variables

    //EditText for the username
    private EditText edtTxt_username;

    //EditText for the password
    private EditText edtTxt_password;

    //onCreate() is being called when we start the application
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the content view for MainActivity
        setContentView(R.layout.activity_main);

        //initialising the field variable and assigning them to their id's
        edtTxt_username = findViewById(R.id.edtTxt_username);
        edtTxt_password = findViewById(R.id.edtTxt_password);

        //declaring local buttons and initialising them at once.
        Button btn_logIn = findViewById(R.id.btn_logIn);
        Button btn_newUser = findViewById(R.id.btn_newUser);

        //setting the onClickListener for both buttons
        btn_logIn.setOnClickListener(onSubmitClick);
        btn_newUser.setOnClickListener(onCreateAccountClick);

    }

    //onCreateOptionsMenu() gets the inflator and mak the menu bar.
    //in this creating a custom menu
    //Parameters: Menu
    //returns: boolean
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //setting the menu bar
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    //This function is called when an item is click from the top menu bar
    //Parameters: MenuItem
    //Returns: boolean
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //getting the id for the button which is clicked.
        int id = item.getItemId();

        if(id == R.id.item_settings){

            //starting a new intent when settings button i sclicked
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //OnClickListener for the submit button
    private View.OnClickListener onSubmitClick = new View.OnClickListener() {

        //onClick Method
        @Override
        public void onClick(View v) {

        try{

            //creating an instance of the LoginInformationDb
            LoginInformationDb loginInformationDb = new LoginInformationDb(MainActivity.this);

            //arraylist of LoginInformation
            ArrayList<LoginInformation> loginInformationArrayList =
                    loginInformationDb.getUserByName(edtTxt_username.getText().toString());

            //checking whether the user has entered something or not
            if(!edtTxt_username.getText().toString().equals("") || !edtTxt_password.getText().toString().equals("")){

                //getting the user information from the database and matching it
                if(loginInformationArrayList.get(0).getUsername().equals(edtTxt_username.getText().toString())){

                    if(loginInformationArrayList.get(0).getPassword().equals(edtTxt_password.getText().toString())){

                        //calling the saveUserName() method.
                        saveUserName();

                        //starting an intent when logIn is click and going to CoursesActivity
                        Intent intent = new Intent(getApplicationContext(), CoursesActivity.class);
                        intent.putExtra("nameForDb", edtTxt_username.getText().toString());
                        startActivity(intent);
                    }
                    else {
                        //calling the alertDialogs method for the error
                        alertDialogs();
                    }
                }
                else {
                    //calling the alertDialogs method for the error
                    alertDialogs();
                }
            }
            else {
                //giving error when user doesnot enter anything
                Toast.makeText(getApplicationContext(), "Please enter username and password.", Toast.LENGTH_LONG).show();
            }
        } catch (IndexOutOfBoundsException e){
            //calling the alertDialogs method for the error
            alertDialogs();
        }
        }
    };

    //OnClickListener for the new account button.
    private View.OnClickListener onCreateAccountClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //starting an intent for result
            Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
            startActivityForResult(intent, 1);
        }
    };

    //getting the result from the intent that was started
    //Parameters: int requestCode, int resultCode, Intent data
    //Result: void
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            try {

                //getting the user name and password
                edtTxt_username.setText(data.getStringExtra("username"));
                edtTxt_password.setText(data.getStringExtra("password"));
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
            }
        }
    }

    //This function is called to give the alert when an error occurs
    //Parameters: null
    //Result void
    public void alertDialogs(){

        //creating an alert
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //setting the specification for the alert
        alertDialog.setTitle("ALERT")
                .setMessage("Wrong username or password.")
                .setPositiveButton("Ok", null)
                .setIcon(R.mipmap.ic_launcher_round);

        AlertDialog dialog = alertDialog.create();

        //showing the alert
        dialog.show();

    }

    //this function saves the username in SharedPreferences and is being used in many classes
    //Parameters: null
    //Returns: void
    public void saveUserName(){
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("User", edtTxt_username.getText().toString()).apply();
    }
}
