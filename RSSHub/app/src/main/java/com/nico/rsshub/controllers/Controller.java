package com.nico.rsshub.controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.AdapterView;

import com.nico.rsshub.modeles.Category;
import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;
import com.nico.rsshub.views.AddFeedActivity;
import com.nico.rsshub.views.InformationActivity;
import com.nico.rsshub.views.InformationDetailActivity;
import com.nico.rsshub.views.ManageFeedsActivity;
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
    private ManageFeedsActivity manageFeedsActivity = null;
    private AddFeedActivity addFeedActivity = null;

    private Activity currentActivity = null;
    private List<Information> informationList = null;
    private List<Information> favorites = null;
    private Map<Feed,List<Information>> feeds = null;
    private Map<Information, Bitmap> images = null;
    private List<Feed> feedsList = null;
    private Semaphore mutexFeeds = null;
    private Semaphore mutexImages = null;

    private List<Feed> selectedFeeds = null;
    private boolean isManageFeedsMode = false;

    private Feed newFeed = null;


    public static Controller getInstance() {
        if(instance == null) {
            Controller.instance = new Controller();
        }
        return Controller.instance;
    }

    private Controller() {
        informationList = new ArrayList<>();
        favorites = new ArrayList<>();
        feeds = new HashMap<>();
        images = new HashMap<>();
        mutexFeeds = new Semaphore(1);
        mutexImages = new Semaphore(1);
        feedsList = new ArrayList<>();
        selectedFeeds = new ArrayList<>();
    }

    public List<Information> getInformationList() { return informationList; }

    public List<Information> getFavorites() { return favorites; }

    public Map<Feed, List<Information>> getFeeds() { return feeds; }

    public Map<Information, Bitmap> getImages() { return images; }

    public Semaphore getMutexFeeds() { return mutexFeeds; }

    public Semaphore getMutexImages() {
        return mutexImages;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public List<Feed> getFeedsList() { return feedsList; }

    public List<Feed> getSelectedFeeds() { return selectedFeeds; }

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
        } else if(activity.getClass().equals(ManageFeedsActivity.class)) {
            this.manageFeedsActivity = (ManageFeedsActivity) activity;
            this.currentActivity = activity;
        } else if(activity.getClass().equals(AddFeedActivity.class)) {
            this.addFeedActivity = (AddFeedActivity) activity;
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
        feed1.setCategory(Category.SPORT);
        feed1.setCacheFileName(createCacheFileName(feed1.getTitle(),feed1.getUrl()));
        feed1.setFavorite(true);

        final Feed feed2 = new Feed();
        feed2.setUrl("http://korben.info/feed");
        feed2.setTitle("Korben");
        feed2.setCategory(Category.COMPUTING);
        feed2.setCacheFileName(createCacheFileName(feed2.getTitle(),feed2.getUrl()));

        feedsList.add(feed1);
        feedsList.add(feed2);

        Feed[] feeds = new Feed[feedsList.size()];
        for(int i = 0; i < feedsList.size(); i++) {
            feeds[i] = feedsList.get(i);
        }

        loadFeedsTask.execute(feeds);
    }

    public void onInformationClick(AdapterView<?> adapter, int position) {
        if(this.currentActivity == this.informationActivity && this.informationActivity != null) {
            Information information = (Information) adapter.getItemAtPosition(position);
            Intent intent = new Intent(this.informationActivity, InformationDetailActivity.class);
            intent.putExtra("information", information);
            this.informationActivity.startActivity(intent);
            LoadFeedTask loadFeedTask = new LoadFeedTask();
            loadFeedTask.execute(information.getUrl());
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
          this.informationActivity.updateInformations(this.informationList,this.favorites);
          this.informationActivity.refreshNavigationMenu(this.feedsList);
      }
    }

    public void loadUrl(String url) {
        if(this.currentActivity == this.informationDetailActivity && this.informationDetailActivity != null) {
            this.informationDetailActivity.loadUrl(url);
        }
    }

    private String createCacheFileName(String feedName, String url) {
        StringBuilder sb = new StringBuilder();

        //ajout dossier cache
        sb.append(this.getCurrentActivity().getCacheDir().getAbsolutePath());
        sb.append("/");

        //ajout nom du nom du feed
        sb.append(feedName.replaceAll("[^A-Z^a-z^0-9]", ""));
        sb.append(".");

        //ajout nom du fichier de l'url
        String[] urlSplitted = url.split("/");
        sb.append(urlSplitted[urlSplitted.length - 1]);

        //ajout extension .xml
        if(!urlSplitted[urlSplitted.length - 1].endsWith(".xml")) {
            sb.append(".xml");
        }

        return sb.toString();
    }

    public void onMannageFeedsClicked() {
        if(this.currentActivity == this.informationActivity && this.informationActivity != null) {
            Intent intent = new Intent(this.informationActivity, ManageFeedsActivity.class);
            this.informationActivity.startActivity(intent);
        }
    }

    public void onBackClicked() {
        if(this.currentActivity != null) {
            if(this.currentActivity == this.informationDetailActivity) {
                this.setCurrentActivity(this.informationActivity);
                this.informationDetailActivity.finish();
                this.informationDetailActivity = null;
            } else if(this.currentActivity == this.manageFeedsActivity) {
                this.setCurrentActivity(this.informationActivity);
                this.manageFeedsActivity.finish();
                this.manageFeedsActivity = null;
                this.updateFavorites();
                this.informationActivity.refreshListViews();
                this.informationActivity.refreshNavigationMenu(this.feedsList);
            } else if(this.currentActivity == this.addFeedActivity) {
                this.setCurrentActivity(this.manageFeedsActivity);
                this.addFeedActivity.finish();
                this.addFeedActivity = null;
                this.manageFeedsActivity.refreshListViewFeeds();
                this.newFeed = null;
            }
        }
    }

    public void onFavoriteButtonClicked(Feed feed) {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.manageFeedsActivity) {
                feed.setFavorite(!feed.isFavorite());
                this.manageFeedsActivity.refreshListViewFeeds();
            }
        }
    }

    public void updateFavorites() {
        this.favorites.clear();
        for(Information information : this.informationList) {
            if(information.getFeed().isFavorite()){
                favorites.add(information);
            }
        }
    }

    public void setManageFeedsMode(boolean isManageFeedsMode) {
        if(this.currentActivity != null) {
            if(this.currentActivity == this.manageFeedsActivity) {
                if(!this.isManageFeedsMode) {
                    this.manageFeedsActivity.showActionMode();
                }
                if(!isManageFeedsMode){
                    this.selectedFeeds.clear();
                }
                this.isManageFeedsMode = isManageFeedsMode;
                this.manageFeedsActivity.setManageFeedsMode(isManageFeedsMode);
                this.manageFeedsActivity.updateListView();
            }
        }
    }

    public void selectFeed(int position) {
        if(this.currentActivity != null) {
            if(this.currentActivity == this.manageFeedsActivity) {
                if(this.isManageFeedsMode) {
                    if(!this.selectedFeeds.contains(feedsList.get(position))) {
                        this.selectedFeeds.add(feedsList.get(position));
                    } else {
                        this.selectedFeeds.remove(feedsList.get(position));
                    }

                    if(this.selectedFeeds.size() == 0) {
                        this.manageFeedsActivity.setModifyingItemVisibility(false);
                        this.manageFeedsActivity.setDeletingItemVisibility(false);
                    } else if(this.selectedFeeds.size() == 1) {
                        this.manageFeedsActivity.setModifyingItemVisibility(true);
                        this.manageFeedsActivity.setDeletingItemVisibility(true);
                    } else {
                        this.manageFeedsActivity.setModifyingItemVisibility(false);
                        this.manageFeedsActivity.setDeletingItemVisibility(true);
                    }

                    this.manageFeedsActivity.setSelectAllItemIcon(this.selectedFeeds.size() == this.feedsList.size());

                    this.manageFeedsActivity.updateListView();
                }
            }
        }
    }

    public void selectAllFeeds() {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.manageFeedsActivity) {
                if (this.isManageFeedsMode) {

                    if (this.selectedFeeds.size() < this.feedsList.size()) {
                        this.selectedFeeds.clear();
                        this.selectedFeeds.addAll(this.feedsList);
                    } else {
                        this.selectedFeeds.clear();
                    }

                    if(this.selectedFeeds.size() == 0) {
                        this.manageFeedsActivity.setModifyingItemVisibility(false);
                        this.manageFeedsActivity.setDeletingItemVisibility(false);
                        this.manageFeedsActivity.setSelectAllItemIcon(false);
                    } else {
                        this.manageFeedsActivity.setModifyingItemVisibility(false);
                        this.manageFeedsActivity.setDeletingItemVisibility(true);
                        this.manageFeedsActivity.setSelectAllItemIcon(true);
                    }

                    this.manageFeedsActivity.updateListView();
                }
            }
        }
    }


    public void onDeleteButtonClicked() {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.manageFeedsActivity) {
                if (this.isManageFeedsMode) {
                    this.manageFeedsActivity.showDeleteAlertDialog(this.selectedFeeds.size());
                }
            }
        }
    }

    public void deleteSelectedFeeds() {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.manageFeedsActivity) {
                if (this.isManageFeedsMode) {
                    feedsList.removeAll(selectedFeeds);

                    for(Feed feed : selectedFeeds) {
                        List<Information> informations = feeds.get(feed);
                        feeds.remove(feed);
                        for(Information information : informations) {
                            images.remove(information);
                        }
                        favorites.removeAll(informations);
                        informationList.removeAll(informations);
                    }

                    selectedFeeds.clear();

                    if(this.feedsList.size() == 0) {
                        this.manageFeedsActivity.finishActionMode();
                    } else {
                        this.manageFeedsActivity.setModifyingItemVisibility(false);
                        this.manageFeedsActivity.setDeletingItemVisibility(false);
                        this.manageFeedsActivity.setSelectAllItemIcon(false);
                    }

                    this.manageFeedsActivity.updateListView();
                }
            }
        }
    }

    public void onPlusButtonClicked() {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.manageFeedsActivity) {
                this.selectedFeeds.clear();
                if(this.isManageFeedsMode) {
                    this.isManageFeedsMode = false;
                    this.manageFeedsActivity.setManageFeedsMode(false);
                    this.manageFeedsActivity.finishActionMode();
                }

                Intent intent = new Intent(this.manageFeedsActivity, AddFeedActivity.class);
                this.manageFeedsActivity.startActivity(intent);

                //TODO afficher vue ajout
                //TODO ajouter refressh this.manageFeedsActivity.updateListView(); lors du retour a la liste des feeds
            }
        }
    }

    public void onModifyButtonClicked() {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.manageFeedsActivity) {
                //TODO afficher vue modifier
                //TODO ajouter refressh this.manageFeedsActivity.updateListView(); lors du retour a la liste des feeds
            }
        }
    }



    public void onAddButtonClicked() {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.addFeedActivity) {
//                if (this.addFeedActivity.areInputsEdited()) {
                    this.newFeed = new Feed();
                    this.addFeedActivity.showAddAlertDialog();
//                } else {
//
//                }
            }
        }
    }

}
