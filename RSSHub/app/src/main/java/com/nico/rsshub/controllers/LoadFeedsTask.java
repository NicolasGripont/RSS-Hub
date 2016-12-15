package com.nico.rsshub.controllers;

import android.os.AsyncTask;

import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;
import com.nico.rsshub.services.FeedParser;

import java.util.List;

/**
 * Created by Nico on 13/12/2016.
 */

public class LoadFeedsTask extends AsyncTask<Feed, Integer, List<Information>> {


    public LoadFeedsTask(){
        super();
    }

    protected List<Information> doInBackground(Feed... feeds) {
        List<Information> informationList;
        try {
            FeedParser feedParser = new FeedParser(10000);
            informationList = feedParser.parse(feeds);
        } catch (Exception e) {
            informationList = null;
            System.out.println(e);
        }
        return informationList;
    }

    protected void onProgressUpdate(Integer... progress) { }

    protected void onPostExecute(List<Information> result) {
        Controller.getInstance().updateInformations(result);
    }


}
