package com.nico.rsshub.controllers;

import android.os.AsyncTask;

import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Nico on 15/12/2016.
 */

public class AddFeedTask extends AsyncTask<Feed, Integer, String> {

    private LoadFeedThread loadFeedThread;

    private List<Information> informationList;

    public AddFeedTask() {
        super();
        this.informationList = new ArrayList<>();
    }


    protected String doInBackground(Feed... feeds) {
        try {
            this.loadFeedThread = new LoadFeedThread(feeds[0],0);
            this.loadFeedThread.start();
            this.loadFeedThread.join();
        } catch (Exception e) {
        }

        return null;
    }

    protected void onPostExecute(String result) {
        Controller.getInstance().loadFeedTaskFinished(this.loadFeedThread.getInformationList(), this.loadFeedThread.getImages());
    }

}
