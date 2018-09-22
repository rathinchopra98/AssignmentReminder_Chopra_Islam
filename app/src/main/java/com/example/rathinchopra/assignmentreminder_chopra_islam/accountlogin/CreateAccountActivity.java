package com.example.rathinchopra.assignmentreminder_chopra_islam.accountlogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rathinchopra.assignmentreminder_chopra_islam.R;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.LoginInformation;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.LoginInformationDb;

import java.util.ArrayList;

public class CreateAccountActivity extends Activity {

    //field variables
    private EditText edtTxt_username;
    private EditText edtTxt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the view
        setContentView(R.layout.activity_create_account);

        //initialising the variables
        edtTxt_username = findViewById(R.id.edtTxt_username);
        edtTxt_password = findViewById(R.id.edtTxt_password);
        Button btn_createAccount = findViewById(R.id.btn_createAccount);

        //setting the listener for the button
        btn_createAccount.setOnClickListener(onCreateAccountClick);
    }

    //listener for the create account button
    private View.OnClickListener onCreateAccountClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //creating the instance
            LoginInformation loginInformation = new LoginInformation(edtTxt_username.getText().toString(),
                    edtTxt_password.getText().toString());

            //instance for database class
            LoginInformationDb loginInformationDb = new LoginInformationDb(getApplicationContext());
            ArrayList<LoginInformation> loginInformationArrayList = loginInformationDb.getUserByName(edtTxt_username.getText().toString());

            //getting the username and password
            String username = edtTxt_username.getText().toString();
            String password = edtTxt_password.getText().toString();
            if(!username.isEmpty() || !password.isEmpty()) {
                if(loginInformationArrayList.size() == 0){
                    loginInformation = loginInformationDb.saveLoginInfo(loginInformation);
                    Toast.makeText(CreateAccountActivity.this, "Account created for " +
                            edtTxt_username.getText().toString() , Toast.LENGTH_LONG).show();

                    Intent intent = new Intent();
                    intent.putExtra("username", edtTxt_username.getText().toString());
                    intent.putExtra("password", edtTxt_password.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Username already exists. Please try a new one.", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Please enter username and password.", Toast.LENGTH_LONG).show();
            }
        }
    };
}
