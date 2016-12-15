package com.nico.rsshub.controllers;

import android.app.Activity;
import android.content.Intent;
import android.widget.AdapterView;

import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;
import com.nico.rsshub.views.InformationActivity;
import com.nico.rsshub.views.InformationDetailActivity;

import java.util.List;

/**
 * Created by Nico on 14/12/2016.
 */

public class Controller {

    private static Controller instance = null;
    InformationActivity informationActivity = null;
    InformationDetailActivity informationDetailActivity = null;
    Activity currentActivity = null;

    public static Controller getInstance() {
        if(instance == null) {
            Controller.instance = new Controller();
        }
        return Controller.instance;
    }

    private Controller() {

    }


    public void setCurrentActivity(Activity activity) {
        if(activity.getClass().equals(InformationActivity.class)) {
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
        String[] urlSplitted = feed1.getUrl().split("/");
        feed1.setCacheFileName(urlSplitted[urlSplitted.length - 1]);

        final Feed feed2 = new Feed();
        feed2.setUrl("http://korben.info/feed");
        urlSplitted = feed2.getUrl().split("/");
        feed2.setCacheFileName(urlSplitted[urlSplitted.length - 1]  + ".xml");

        loadFeedsTask.execute(feed1,feed2);
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

    public void updateInformations(List<Information> informationList) {
      if(this.currentActivity == this.informationActivity && this.informationActivity != null) {
          this.informationActivity.updateInformations(informationList);
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