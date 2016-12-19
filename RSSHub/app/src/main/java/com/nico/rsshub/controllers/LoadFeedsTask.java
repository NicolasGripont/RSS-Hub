package com.nico.rsshub.controllers;

import android.os.AsyncTask;
import android.os.Environment;

import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;
import com.nico.rsshub.services.FeedParser;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nico on 13/12/2016.
 */

public class LoadFeedsTask extends AsyncTask<Feed, Integer, List<Information>> {

    private long refreshTimeInMs;

    private List<LoadFeedTask> loadFeedTasks;

    private List<Information> informationList;

    public LoadFeedsTask() { this(0); }

    public LoadFeedsTask(final long refreshTime) {
        super();
        this.refreshTimeInMs = refreshTime;
        this.loadFeedTasks = new ArrayList<>();
        this.informationList = new ArrayList<>();
    }

    public long getRefreshTimeInMs() { return this.refreshTimeInMs; }

    public void setRefreshTimeInMs(final long refreshTimeInMs) { this.refreshTimeInMs = refreshTimeInMs; }

    protected List<Information> doInBackground(Feed... feeds) {
        List<Information> informationList = null;

        for(Feed feed : feeds) {
            try {
                LoadFeedTask loadFeedTask = new LoadFeedTask(feed,0);
                this.loadFeedTasks.add(loadFeedTask);
                loadFeedTask.start();
            } catch (Exception e) {
                informationList = null;
            }
        }

        return informationList;
    }

    protected void onPostExecute(List<Information> result) {
        for(LoadFeedTask loadFeedTask : this.loadFeedTasks) {
            try {
                loadFeedTask.join();
                if(loadFeedTask.getInformationList() != null){
                    this.informationList.addAll(loadFeedTask.getInformationList());
                }
            } catch (InterruptedException e) {
            }
        }

//        Collections.sort(this.informationList, new Comparator<Information>() {
//            @Override
//            public int compare(Information lhs, Information rhs) {
//                if(lhs.getDatePublication().getTime() - rhs.getDatePublication().getTime() > 0) {
//                    return -1;
//                } else if (lhs.getDatePublication().getTime() - rhs.getDatePublication().getTime() == 0) {
//                    return 0;
//                }
//                return 1;
//            }
//        });
        if(this.informationList.size() != 0) {
            Controller.getInstance().updateInformations(this.informationList);
        }
    }

}
