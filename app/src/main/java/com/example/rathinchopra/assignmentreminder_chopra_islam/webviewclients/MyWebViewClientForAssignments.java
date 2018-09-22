package com.example.rathinchopra.assignmentreminder_chopra_islam.webviewclients;

import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.rathinchopra.assignmentreminder_chopra_islam.slatelogin.SlateLoginForAssignment;

/**
 * Created by Rathin Chopra and Aurnob Islam on 2017-12-31.
 */

public class MyWebViewClientForAssignments extends WebViewClient {

    //Declaring the field variables

    //WebView
    private WebView webView_slate;

    //SlateLoginForAssignment class
    private SlateLoginForAssignment slateLogin;

    //Constructor for this class
    //Parameters: WebView webView, SlateLoginForAssignment login
    public MyWebViewClientForAssignments(WebView webView, SlateLoginForAssignment login){
        webView_slate = webView;
        slateLogin = login;
    }

    //this function overrides the url and calls the loadUrl method
    //Parameters: WebView view, String url
    //Returns: boolean
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        WebSettings webSettings = webView_slate.getSettings();
        webSettings.setJavaScriptEnabled(true);
        view.loadUrl(url);
        return true;
    }

    //This is called when the page is finished loading and it gets the keys from the slate website
    //Parameters: WebView view, String url
    //Returns: void
    @Override
    public void onPageFinished(WebView view, String url) {

        // If page URL is the Home page of D2L, collect cookies.
        if (url.equals("https://slate.sheridancollege.ca/d2l/m/home")) {
            String cookies = CookieManager.getInstance().getCookie(url);
            String[] cookies2 = cookies.split(";");
            String[] keys = new String[2];

            // Find D2L Key placed in browser cookies.
            for (String c : cookies2) {
                String[] c2 = c.split("=");

                if (c2[0].equals(" d2lSecureSessionVal")) {
                    keys[1] = c2[1];
                }
                if (c2[0].equals(" d2lSessionVal")) {
                    keys[0] = c2[1];
                }
            }

            // Returning keys.
            slateLogin.getKeys(keys);
        }
    }
}
