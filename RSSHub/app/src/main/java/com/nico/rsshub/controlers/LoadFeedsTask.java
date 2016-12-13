package com.nico.rsshub.controlers;

import android.os.AsyncTask;

import com.nico.rsshub.services.FeedParser;
import com.nico.rsshub.views.MainActivity;
import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;


import java.util.List;

/**
 * Created by Nico on 13/12/2016.
 */

public class LoadFeedsTask extends AsyncTask<String, Integer, List<Information>> {

    private MainActivity mainActivity = null;

    public LoadFeedsTask(MainActivity mainActivity){
        super();
        this.mainActivity = mainActivity;
    }

    protected List<Information> doInBackground(String... urls) {
        List<Information> informationsList = null;
        try {
            final Feed feed = new Feed();
            feed.setUrl(urls[0]);
            final String[] urlSplitted = feed.getUrl().split("/");
            feed.setCacheFileName(urlSplitted[urlSplitted.length - 1]);
            FeedParser feedParser = new FeedParser(10000);
            informationsList = feedParser.parseAFeed(feed);

        } catch (Exception e) {
            informationsList = null;
        }
        return informationsList;
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(List<Information> result) {
        mainActivity.updateInformations(result);
    }


}
