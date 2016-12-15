package com.nico.rsshub.controllers;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nico on 15/12/2016.
 */

public class TestUrlTask extends AsyncTask<String, Integer, String> {

    public TestUrlTask(){
        super();
    }

    protected String doInBackground(String... urls) {
        String url;
        try {
            int code = TestUrlTask.getResponseCode(urls[0].replace("www.","m."));
            if(code == 200) {
                url = urls[0].replace("www.","m.");
            } else {
                url = urls[0];
            }
        } catch (Exception e) {
            url = urls[0];
        }
        return url;
    }

    protected void onProgressUpdate(Integer... progress) { }

    protected void onPostExecute(String result) {
        Controller.getInstance().loadUrl(result);
    }

    private static int getResponseCode(String urlString) throws IOException {
        URL u = new URL(urlString);
        HttpURLConnection huc =  (HttpURLConnection)  u.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        return huc.getResponseCode();
    }

}
