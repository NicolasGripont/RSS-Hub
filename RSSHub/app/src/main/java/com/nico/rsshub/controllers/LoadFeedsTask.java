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

public class LoadFeedsTask extends AsyncTask<Feed, Integer, String> {

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

    protected String doInBackground(Feed... feeds) {

        for(Feed feed : feeds) {
            try {
                LoadFeedTask loadFeedTask = new LoadFeedTask(feed,0);
                this.loadFeedTasks.add(loadFeedTask);
                loadFeedTask.start();
            } catch (Exception e) {
            }
        }

        return null;
    }

    protected void onPostExecute(String result) {
        for(LoadFeedTask loadFeedTask : this.loadFeedTasks) {
            try {
                loadFeedTask.join();
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
        if(Controller.getInstance().getInformationList().size() != 0) {
            Controller.getInstance().showInformationActivity();
        }
    }

}
