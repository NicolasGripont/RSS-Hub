package com.nico.rsshub.controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.AdapterView;

import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;
import com.nico.rsshub.views.InformationActivity;
import com.nico.rsshub.views.InformationDetailActivity;
import com.nico.rsshub.views.SplashActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Created by Nico on 14/12/2016.
 */

public class Controller {

    private static Controller instance = null;
    private SplashActivity splashActivity = null;
    private InformationActivity informationActivity = null;
    private InformationDetailActivity informationDetailActivity = null;
    private Activity currentActivity = null;
    private List<Information> informationList = null;
    private Map<Feed,List<Information>> feeds = null;
    private Map<Information, Bitmap> images = null;
    private Semaphore mutexFeeds = null;
    private Semaphore mutexImages = null;

    public static Controller getInstance() {
        if(instance == null) {
            Controller.instance = new Controller();
        }
        return Controller.instance;
    }

    private Controller() {
        informationList = new ArrayList<>();
        feeds = new HashMap<>();
        images = new HashMap<>();
        mutexFeeds = new Semaphore(1);
        mutexImages = new Semaphore(1);
    }

    public List<Information> getInformationList() { return informationList; }

    public Map<Feed, List<Information>> getFeeds() { return feeds; }

    public Map<Information, Bitmap> getImages() { return images; }

    public Semaphore getMutexFeeds() { return mutexFeeds; }

    public Semaphore getMutexImages() {
        return mutexImages;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        if(activity.getClass().equals(SplashActivity.class)) {
            this.splashActivity = (SplashActivity) activity;
            this.currentActivity = activity;
        } else if(activity.getClass().equals(InformationActivity.class)) {
            this.informationActivity = (InformationActivity) activity;
            this.currentActivity = activity;
        } else if(activity.getClass().equals(InformationDetailActivity.class)) {
            this.informationDetailActivity = (InformationDetailActivity) activity;
            this.currentActivity = activity;
        } else {
            this.currentActivity = null;
        }
    }

    public void loadInformations(){
        LoadFeedsTask loadFeedsTask = new LoadFeedsTask();

        final Feed feed1 = new Feed();
        feed1.setUrl("http://www.lequipe.fr/rss/actu_rss.xml");
        feed1.setTitle("L'Equipe");
        feed1.setCategory("Sport");
        String[] urlSplitted = feed1.getUrl().split("/");
        feed1.setCacheFileName(urlSplitted[urlSplitted.length - 1]);

//        final Feed feed2 = new Feed();
//        feed2.setUrl("http://korben.info/feed");
//        feed2.setTitle("Korben");
//        feed2.setCategory("Informatique");
//        urlSplitted = feed2.getUrl().split("/");
//        feed2.setCacheFileName(urlSplitted[urlSplitted.length - 1]  + ".xml");

        loadFeedsTask.execute(feed1/*,feed2*/);
    }

    public void onInformationClick(AdapterView<?> adapter, int position) {
        if(this.currentActivity == this.informationActivity && this.informationActivity != null) {
            Information information = (Information) adapter.getItemAtPosition(position);
            Intent intent = new Intent(this.informationActivity, InformationDetailActivity.class);
            intent.putExtra("information", information);
            this.informationActivity.startActivity(intent);
            TestUrlTask testUrlTask = new TestUrlTask();
            testUrlTask.execute(information.getUrl());
        }
    }

    public void showInformationActivity() {
        if(this.currentActivity == this.splashActivity && this.splashActivity != null) {
            Intent intent = new Intent(this.splashActivity, InformationActivity.class);
            this.splashActivity.startActivity(intent);
            this.splashActivity.finish();
        }
    }


    public void updateInformations() {
      if(this.currentActivity == this.informationActivity && this.informationActivity != null) {
          this.informationActivity.updateInformations(this.informationList);
      }
    }

    public void loadUrl(String url) {
        if(this.currentActivity == this.informationDetailActivity && this.informationDetailActivity != null) {
            this.informationDetailActivity.loadUrl(url);
        }
    }

    public void backToInformationActivity() {
        this.setCurrentActivity(this.informationActivity);
    }
}
