package com.example.rathinchopra.assignmentreminder_chopra_islam.slatelogin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.rathinchopra.assignmentreminder_chopra_islam.R;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.Courses;
import com.example.rathinchopra.assignmentreminder_chopra_islam.webviewclients.MyWebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SlateLoginActivity extends Activity {

    //declaring the field variables

    //WebView
    private WebView webView_slate;

    //Array of string that holds the keys used to access D2L API.
    private String[] keysD2L;

    //OkHttpClient
    private OkHttpClient client;

    //String
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the view to the activity_slate_login
        setContentView(R.layout.activity_slate_login);

        //Initialising the field variable and assigning the id
        webView_slate = findViewById(R.id.webView_slate);

        //initialising myWebViewClient and passing the webview and context
        MyWebViewClient myWebViewClient = new MyWebViewClient(webView_slate, this);

        //Setting the WebViewClient
        webView_slate.setWebViewClient(myWebViewClient);

        //calling the setUpView method
        setupViews();

        //setting the value for the views
        keysD2L = new String[2];
        username = getIntent().getStringExtra("PrimaryKeyForTable");

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

    //This function manages the cookies for the webview
    //Parameters: none
    //Result: void
    public void setupViews() {

        //managing the cookies
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieManager.getInstance().setAcceptCookie(true);

        //setting the url and loading it
        webView_slate.loadUrl("http://slate.sheridancollege.ca");
    }

    //this function is for setting the keys
    public void getKeys(String[] keys) {
        keysD2L[0] = keys[0];
        keysD2L[1] = keys[1];

        client = new OkHttpClient();

        //executing the async task
        ProccessTask proccessTask = new ProccessTask();
        proccessTask.execute();
    }

    //this class extend the async task
    private class ProccessTask extends AsyncTask<Void, Void, Void> {

        //arraylist of Courses
        ArrayList<Courses> allCourses = new ArrayList<>();

        //this function does all the stuff in background and doesnot interrupt with the main UI thread
        @Override
        protected Void doInBackground(Void... voids) {

            //here we are creating the request for the urla nd adding the url
            Request request = new Request.Builder()
                    .addHeader("Cookie", "d2lSessionVal=" + keysD2L[0] + ";d2lSecureSessionVal=" + keysD2L[1])
                    .url("https://slate.sheridancollege.ca/d2l/api/lp/1.10/enrollments/myenrollments/")
                    .build();

            //Calling API and getting all the information that is required.
            try {

                Response res = client.newCall(request).execute();
                String jsonString = res.body().string();

                //getting the JSON objects
                JSONObject root   = new JSONObject(jsonString);
                JSONArray courses = root.getJSONArray("Items");

                //looping through the course and getting the right ones
                for (int index = 0; index < courses.length(); index++) {
                    JSONObject course  = courses.getJSONObject(index);
                    JSONObject orgUnit = course.getJSONObject("OrgUnit");
                    JSONObject access  = course.getJSONObject("Access");

                    // Skiping non-course data that is not required.
                    if (!orgUnit.getJSONObject("Type").getString("Code").toUpperCase().equals("COURSE OFFERING")) {
                        continue;
                    }

                    Courses tempCourse = new Courses(orgUnit.getInt("Id"), orgUnit.getString("Name"), username);

                    //adding it in the arraylist
                    allCourses.add(tempCourse);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Enrollment failed to parse", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "API Enrollment call failed", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        //This function is called when the execution is done and it goes back to parent activity
        //Parameter: Void aVoid
        //Result: void
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //starting the intent and setting the result
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("courses", allCourses);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
