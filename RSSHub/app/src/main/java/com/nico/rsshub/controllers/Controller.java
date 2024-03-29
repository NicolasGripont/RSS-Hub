package com.nico.rsshub.controllers;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;
import com.nico.rsshub.services.FeedManager;
import com.nico.rsshub.views.AddFeedActivity;
import com.nico.rsshub.views.InformationActivity;
import com.nico.rsshub.views.InformationDetailActivity;
import com.nico.rsshub.views.ManageFeedsActivity;
import com.nico.rsshub.views.SplashActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private List<Feed> feedsList = null;
    private Set<String> images = null;

    private List<Feed> selectedFeeds = null;
    private boolean isManageFeedsMode = false;

    private Feed newFeed = null;
    private List<Information> newInformationList = null;
    private AddFeedTask addFeedTask = null;

    private Information informationDetail = null;

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
        feedsList = new ArrayList<>();
        selectedFeeds = new ArrayList<>();
        images = new HashSet<>();
    }

    public List<Information> getInformationList() { return informationList; }

    public List<Information> getFavorites() { return favorites; }

    public Map<Feed, List<Information>> getFeeds() { return feeds; }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public List<Feed> getFeedsList() { return feedsList; }

    public List<Feed> getSelectedFeeds() { return selectedFeeds; }

    public Set<String> getImages() { return images; }

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
        this.feedsList = FeedManager.readFeeds(this.currentActivity.getApplicationContext());

        Feed[] feeds = new Feed[feedsList.size()];
            for (int i = 0; i < feedsList.size(); i++) {
                feeds[i] = feedsList.get(i);
            }
        loadFeedsTask.execute(feeds);
    }

    public void onInformationClick(AdapterView<?> adapter, int position) {
        if(this.currentActivity == this.informationActivity && this.informationActivity != null) {
            this.informationDetail = (Information) adapter.getItemAtPosition(position);
            Intent intent = new Intent(this.informationActivity, InformationDetailActivity.class);
            this.informationActivity.startActivity(intent);
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

    public void testURL() {
        TestUrlTask loadInformationDetailTask = new TestUrlTask();
        loadInformationDetailTask.execute(this.informationDetail.getUrl());
    }

    public void loadUrl(String url) {
        if(this.informationDetailActivity != null) {
            this.informationDetailActivity.loadUrl(url);
        }
    }

    private String createCacheFileName(String feedSource, String feedTitle, String url) {
        StringBuilder sb = new StringBuilder();

        //ajout dossier cache
        sb.append(this.getCurrentActivity().getCacheDir().getAbsolutePath());
        sb.append("/");

        //ajout source du feed
        sb.append(feedSource.replaceAll("[^A-Z^a-z^0-9]", ""));
        sb.append(".");

        //ajout titre du feed
        sb.append(feedTitle.replaceAll("[^A-Z^a-z^0-9]", ""));
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
                this.informationActivity.updateInformations(informationList,favorites);
                this.informationActivity.refreshListViews();
                this.informationActivity.refreshNavigationMenu(this.feedsList);
            } else if(this.currentActivity == this.addFeedActivity) {
                this.setCurrentActivity(this.manageFeedsActivity);
                this.addFeedActivity.finish();
                this.addFeedActivity = null;
                this.manageFeedsActivity.refreshListViewFeeds();
                this.newFeed = null;
                this.newInformationList = null;
                this.addFeedTask = null;
            }
        }
    }

    public void onFavoriteButtonClicked(Feed feed) {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.manageFeedsActivity) {
                feed.setFavorite(!feed.isFavorite());
                FeedManager.writeFeeds(this.currentActivity.getApplicationContext(),feedsList);
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
                        if(informations != null) {
                            for (Information information : informations) {
                                favorites.removeAll(informations);
                                informationList.removeAll(informations);
                            }
                        }
                        feeds.remove(feed);
                    }
                    FeedManager.writeFeeds(this.currentActivity.getApplicationContext(),feedsList);
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
                if (this.addFeedActivity.areInputsEdited()) {
                    this.newFeed = new Feed();
                    this.newFeed.setSource(this.addFeedActivity.getFeedSource());
                    this.newFeed.setTitle(this.addFeedActivity.getFeedTitle());
                    this.newFeed.setUrl(this.addFeedActivity.getFeedUrl());
                    this.newFeed.setTags(this.addFeedActivity.getFeedTags());
                    this.newFeed.setCacheFileName(this.createCacheFileName(newFeed.getSource(),newFeed.getTitle(),newFeed.getUrl()));
                    this.addFeedActivity.showLoadFeedAlertDialog();
                    this.addFeedTask = new AddFeedTask();
                    this.addFeedTask.execute(this.newFeed);
                } else {
                }
            }
        }
    }



    public void loadFeedTaskFinished(List<Information> informationList) {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.addFeedActivity) {
                this.addFeedActivity.dismissLoadingDialog();
                if(informationList.size() != 0) {
                    this.newInformationList = informationList;
                    this.addFeedActivity.showConfirmationAlertDialog();
                } else {
                    this.newFeed = null;
                    this.newInformationList = null;
                    this.addFeedActivity.showErrorAlertDialog();
                }
                this.addFeedTask = null;
            }
        }
    }

    public void addNewFeed() {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.addFeedActivity) {
                this.feedsList.add(newFeed);
                this.informationList.addAll(newInformationList);
                Collections.sort(this.informationList, new Comparator<Information>() {
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
                this.feeds.put(newFeed,newInformationList);
                FeedManager.writeFeeds(this.currentActivity.getApplicationContext(),feedsList);
            }
        }
    }

    public void cancelLoadFeed() {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.addFeedActivity) {
                if(this.addFeedTask != null) {
                    this.addFeedTask.cancel(true);
                }
                this.addFeedTask = null;
                this.newFeed = null;
                this.newInformationList = null;
            }
        }
    }

    public List<String> getTags() {
        List<String> tags = new ArrayList<>();
        for(Feed feed : feedsList) {
            List<String> tmpTags = new ArrayList<>();
            for(String tag : feed.getTags()) {
                if (!tags.contains(tag)) {
                    tmpTags.add(tag);
                }
            }
            tags.addAll(tmpTags);
        }
        return tags;
    }

    public void onNavigationFeedItemSelected(MenuItem item) {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.informationActivity) {
                for(Feed feed : feedsList) {
                    if(item.toString().equals(feed.getSource() + " - " + feed.getTitle())) {
                        this.informationActivity.updateInformations(this.feeds.get(feed),null);
                        this.informationActivity.refreshListViews();
                        this.informationActivity.setTitle(item.toString());
                        return;
                    }
                }
            }
        }
    }

    public void onNavigationTagsItemSelected(MenuItem item) {
        if(this.currentActivity != null) {
            if (this.currentActivity == this.informationActivity) {
                List<Information> informationList = new ArrayList<>();
                List<Information> favorites = new ArrayList<>();
                for(Feed feed : feedsList) {
                    for(String tag : feed.getTags()) {
                        if (item.toString().equals(tag)) {
                            informationList.addAll(feeds.get(feed));
                            if(feed.isFavorite()) {
                                favorites.addAll(feeds.get(feed));
                            }
                            break;
                        }
                    }
                }
                if(favorites.size() == 0) {
                    favorites = null;
                }

                this.informationActivity.updateInformations(informationList, favorites);
                this.informationActivity.refreshListViews();
                this.informationActivity.setTitle(item.toString());

            }
        }
    }
}
