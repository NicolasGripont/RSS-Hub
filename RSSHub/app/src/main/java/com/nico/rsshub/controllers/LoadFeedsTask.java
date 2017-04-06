package com.nico.rsshub.controllers;

import android.os.AsyncTask;

import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;
import com.nico.rsshub.services.FeedManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.CheckedOutputStream;

/**
 * Created by Nico on 13/12/2016.
 */

public class LoadFeedsTask extends AsyncTask<Feed, Integer, String> {

    private long refreshTimeInMs;

    private List<LoadFeedThread> loadFeedThreads;

    private List<Information> informationList;

    public LoadFeedsTask() { this(0); }

    public LoadFeedsTask(final long refreshTime) {
        super();
        this.refreshTimeInMs = refreshTime;
        this.loadFeedThreads = new ArrayList<>();
        this.informationList = new ArrayList<>();
    }

    public long getRefreshTimeInMs() { return this.refreshTimeInMs; }

    public void setRefreshTimeInMs(final long refreshTimeInMs) { this.refreshTimeInMs = refreshTimeInMs; }

    protected String doInBackground(Feed... feeds) {

        if(feeds != null)
        {
            for(Feed feed : feeds) {
                try {
                    LoadFeedThread loadFeedThread = new LoadFeedThread(feed,0);
                    this.loadFeedThreads.add(loadFeedThread);
                    loadFeedThread.start();
                } catch (Exception e) {
                }
            }
        }

        return null;
    }

    protected void onPostExecute(String result) {

        for(LoadFeedThread loadFeedThread : this.loadFeedThreads) {
            try {
                loadFeedThread.join();
                if(loadFeedThread.getInformationList() != null && !loadFeedThread.getInformationList().isEmpty()) {
                    Controller.getInstance().getInformationList().addAll(loadFeedThread.getInformationList());
                    Controller.getInstance().getFeeds().put(loadFeedThread.getFeed(),loadFeedThread.getInformationList());
                    for(Information information : loadFeedThread.getInformationList()) {
                        if(information.getImage() != null && !information.getImage().isEmpty()) {
                            Controller.getInstance().getImages().add(Integer.toString(information.getImage().hashCode()));
                        }
                    }
                }
            } catch (InterruptedException e) {
            }
        }

        Collections.sort(Controller.getInstance().getInformationList(), new Comparator<Information>() {
            @Override
            public int compare(Information lhs, Information rhs) {
                if(lhs.getDatePublication().getTime() - rhs.getDatePublication().getTime() > 0) {
                    return -1;
                } else if (lhs.getDatePublication().getTime() - rhs.getDatePublication().getTime() == 0) {
                    return 0;
                }
                return 1;
            }
        });

        Controller.getInstance().updateFavorites();
        FeedManager.removeUnusedImages(Controller.getInstance().getCurrentActivity().getApplicationContext(),Controller.getInstance().getImages());


//        if(Controller.getInstance().getInformationList().size() != 0) {
            Controller.getInstance().showInformationActivity();
//        }
    }

}
