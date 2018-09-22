package com.example.rathinchopra.assignmentreminder_chopra_islam.slatelogin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import com.example.rathinchopra.assignmentreminder_chopra_islam.R;
import com.example.rathinchopra.assignmentreminder_chopra_islam.model.Assignments;
import com.example.rathinchopra.assignmentreminder_chopra_islam.webviewclients.MyWebViewClientForAssignments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SlateLoginForAssignment extends Activity {

    //declaring the field variables

    //WebView
    private WebView webView_slate;

    //Array of string that holds the keys used to access D2L API.
    private String[] keysD2L;

    //OkHttpClient
    private OkHttpClient client;

    private String username;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the view to the activity_slate_login
        setContentView(R.layout.activity_slate_login);

        //Initialising the field variable and assigning the id
        webView_slate = findViewById(R.id.webView_slate);

        //initialising myWebViewClient and passing the webview and context
        MyWebViewClientForAssignments myWebViewClient = new MyWebViewClientForAssignments(webView_slate, this);

        //Setting the WebViewClient for webView_slate
        webView_slate.setWebViewClient(myWebViewClient);

        //calling the setUpView method
        setupViews();
        keysD2L = new String[2];
        username = getIntent().getStringExtra("PrimaryKeyForTable");
        id = getIntent().getIntExtra("id", 0);

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
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieManager.getInstance().setAcceptCookie(true);
        webView_slate.loadUrl("http://slate.sheridancollege.ca");
    }

    //this function is for setting the keys
    //Parameters: String[]
    //returns: void
    public void getKeys(String[] keys) {
        keysD2L[0] = keys[0];
        keysD2L[1] = keys[1];

        client = new OkHttpClient();
        SlateLoginForAssignment.ProccessTask proccessTask = new SlateLoginForAssignment.ProccessTask();
        proccessTask.execute();
    }

    //This function is used for formatting the string into date
    //Parameters: string date
    //Returns: String
    public String dateFormatter(String date){
        try {

            //creating the format that the string is in.
            SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            //creating it in the format we needed.
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
            Date serverDate = serverFormat.parse(date);
            return simpleDateFormat.format(serverDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //this class extend the async task
    private class ProccessTask extends AsyncTask<Void, Void, Void> {

        //adding the url and id
        String url = "https://slate.sheridancollege.ca/d2l/api/le/1.10/" + id + "/dropbox/folders/";
        ArrayList<Assignments> assignments = new ArrayList<>();

        //this function does all the stuff in background and doesnot interrupt with the main UI thread
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //here we are creating the request for the urla nd adding the url
                Request request = new Request.Builder()
                        .addHeader("Cookie", "d2lSessionVal=" + keysD2L[0] + ";d2lSecureSessionVal=" + keysD2L[1])
                        .url(url)
                        .build();

                Response res = client.newCall(request).execute();
                String jsonString = res.body().string();

                //Calling API and getting all the information that is required.
                if(res.code() != 404) {
                    JSONArray root = new JSONArray(jsonString);

                    for (int index = 0; index < root.length(); index++) {

                        //getting the JSON objects
                        JSONObject course = root.getJSONObject(index);
                        String title = course.getString("Name");
                        String dueDate = course.getString("DueDate");

                        dueDate = dateFormatter(dueDate);

                        Assignments tempAssignments = new Assignments(title, dueDate, username);

                        assignments.add(tempAssignments);
                    }
                }

            } catch (java.lang.RuntimeException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //This function is called when the execution is done and it goes back to parent activity
        //Parameter: Void aVoid
        //Result: void
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("assignments", assignments);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
