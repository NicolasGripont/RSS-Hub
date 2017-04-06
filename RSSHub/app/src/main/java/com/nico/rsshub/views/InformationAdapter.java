package com.nico.rsshub.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nico.rsshub.R;
import com.nico.rsshub.controllers.Controller;
import com.nico.rsshub.modeles.Information;
import com.nico.rsshub.services.FeedManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nico on 15/12/2016.
 */
public class InformationAdapter extends BaseAdapter {
    private List<Information> informationList;

    private Context context;

    private LayoutInflater layoutInflater;

    private boolean withImage;

    public InformationAdapter(Context context, List<Information> informationList, boolean withImage) {
        this.context = context;
        this.informationList = informationList;
        this.layoutInflater = LayoutInflater.from(context);
        this.withImage = withImage;
    }

    public int getCount() {
        return informationList.size();
    }

    public Object getItem(int position) {
        return informationList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layoutItem;

        //(1) : Réutilisation des layouts
        if (convertView == null) {
            //Initialisation de notre item à partir du  layout XML "information_layout.xml"
            layoutItem = (LinearLayout) layoutInflater.inflate(R.layout.information_layout, parent, false);
        } else {
            layoutItem = (LinearLayout) convertView;
        }

        //(2) : Récupération des TextView de notre layout
        TextView information_feed_source = (TextView)layoutItem.findViewById(R.id.information_feed_source);
        TextView information_feed_title = (TextView)layoutItem.findViewById(R.id.information_feed_title);
        TextView information_feed_tags = (TextView)layoutItem.findViewById(R.id.information_feed_tags);
        TextView information_title = (TextView)layoutItem.findViewById(R.id.information_title);
        TextView information_date = (TextView)layoutItem.findViewById(R.id.information_date);
        ImageView information_image = (ImageView)layoutItem.findViewById(R.id.information_image);
        LinearLayout LL_Fond = (LinearLayout)layoutItem.findViewById(R.id.LL_Fond);

        //(3) : Recupération de l'élément
        Information information = informationList.get(position);

        //(4) : Renseignement des valeurs
        information_feed_source.setText(information.getFeed().getSource());
        information_feed_title.setText(information.getFeed().getTitle());
        String tags = "";
        for (String tag: information.getFeed().getTags()) {
            tags += tag + " ";
        }
        information_feed_tags.setText(tags);
        information_title.setText(information.getTitle());
        if(information.getDatePublication() != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.ENGLISH);
            information_date.setText(dateFormat.format(information.getDatePublication()));
        }

        if(this.withImage) {
            Bitmap image = FeedManager.loadImage(context,information);
            if(image != null)
                information_image.setImageBitmap(image);
            else
                information_image.setImageDrawable(null);
        } else {
            information_image.setImageDrawable(null);
        }

        if(information.getFeed().isFavorite()) {
//            LL_Fond.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
//            information_title.setTextColor(ContextCompat.getColor(context, R.color.white));
//            information_feed.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
//            information_feed.setTextColor(ContextCompat.getColor(context, R.color.black));
            information_feed_source.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
        } else {
//            LL_Fond.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
//            information_title.setTextColor(ContextCompat.getColor(context, R.color.black));
//            information_feed.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
//            information_feed.setTextColor(ContextCompat.getColor(context, R.color.white));
            information_feed_source.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
        }

        //On retourne l'item créé.
        return layoutItem;
    }


}
