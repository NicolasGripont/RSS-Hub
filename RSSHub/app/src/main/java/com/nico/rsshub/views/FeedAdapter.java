package com.nico.rsshub.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nico.rsshub.R;
import com.nico.rsshub.controllers.Controller;
import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nico on 20/12/2016.
 */

public class FeedAdapter extends BaseAdapter {
    private List<Feed> feeds;

    private List<Feed> selectedFeeds;

    private Context context;

    private boolean isManageFeedsMode;

    private LayoutInflater layoutInflater;

    public FeedAdapter(Context context, List<Feed> feeds, List<Feed>selectedFeeds) {
        this.context = context;
        this.feeds = feeds;
        this.selectedFeeds = selectedFeeds;
        this.layoutInflater = LayoutInflater.from(context);
        this.isManageFeedsMode = false;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public boolean isManageFeedsMode() { return isManageFeedsMode; }

    public void setManageFeedsMode(boolean manageFeedsMode) { isManageFeedsMode = manageFeedsMode; }

    public int getCount() {
        return feeds.size();
    }

    public Object getItem(int position) {
        return feeds.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;

        //(1) : Réutilisation des layouts
        if (convertView == null) {
            //Initialisation de notre item à partir du  layout XML "information_layout.xml"
            layoutItem = (LinearLayout) layoutInflater.inflate(R.layout.feed_layout, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        //(2) : Récupération des TextView de notre layout
        TextView feed_title = (TextView)layoutItem.findViewById(R.id.feed_title);
        TextView feed_category = (TextView)layoutItem.findViewById(R.id.feed_category);
        TextView feed_url = (TextView)layoutItem.findViewById(R.id.feed_url);
        ImageView feed_isFavorite_ImageView = (ImageView) layoutItem.findViewById(R.id.feed_isFavorite_imageView);
        LinearLayout feed_layout_favorites = (LinearLayout) layoutItem.findViewById(R.id.feed_layout_favorites);

        //(3) : Renseignement des valeurs
        feed_title.setText(feeds.get(position).getTitle());
        feed_category.setText(feeds.get(position).getCategory().getValue());
        feed_url.setText(feeds.get(position).getUrl());

        if(!isManageFeedsMode) {
            if (feeds.get(position).isFavorite()) {
                feed_isFavorite_ImageView.setImageResource(R.mipmap.yellow_star);
            } else {
                feed_isFavorite_ImageView.setImageResource(R.mipmap.gray_star);
            }

            feed_isFavorite_ImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Controller.getInstance().onFavoriteButtonClicked(feeds.get(position));
                }
            });
        } else {
            if (selectedFeeds.contains(feeds.get(position))) {
                feed_isFavorite_ImageView.setImageResource(R.mipmap.check_orange);
            } else {
                feed_isFavorite_ImageView.setImageResource(R.mipmap.uncheck_orange);
            }
            feed_isFavorite_ImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Controller.getInstance().selectFeed(position);
                }
            });
        }


        //On retourne l'item créé.
        return layoutItem;
    }

}
