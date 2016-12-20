package com.nico.rsshub.views;

import android.content.Context;
import android.graphics.Bitmap;
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

    public InformationAdapter(Context context, List<Information> informationList) {
        this.context = context;
        this.informationList = informationList;
        this.layoutInflater = LayoutInflater.from(context);
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
        TextView information_feed = (TextView)layoutItem.findViewById(R.id.information_feed);
        TextView information_category = (TextView)layoutItem.findViewById(R.id.information_category);
        TextView information_title = (TextView)layoutItem.findViewById(R.id.information_title);
        TextView information_date = (TextView)layoutItem.findViewById(R.id.information_date);
        ImageView information_image = (ImageView)layoutItem.findViewById(R.id.information_image);

        //(3) : Renseignement des valeurs
        information_feed.setText(informationList.get(position).getFeed().getTitle());
        information_category.setText(informationList.get(position).getFeed().getCategory().getValue());
        information_title.setText(informationList.get(position).getTitle());
        if(informationList.get(position).getDatePublication() != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.ENGLISH);
            information_date.setText(dateFormat.format(informationList.get(position).getDatePublication()));
        }

        if(this.informationList.get(position).getImage() != null && !this.informationList.get(position).getImage().equals("")) {
            Bitmap image = Controller.getInstance().getImages().get(this.informationList.get(position));
            if(image != null) {
                try {
                    double ratio = (information_image.getWidth()*1.0) / (image.getWidth()*1.0);
                    Bitmap newBitmap = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * ratio), (int) (image.getHeight() * ratio), false);
                    information_image.setImageBitmap(newBitmap);
                } catch (Exception e){
                    information_image.setImageDrawable(null);
                }
            } else {
                information_image.setImageDrawable(null);
            }
        } else {
            information_image.setImageDrawable(null);
        }


        //On retourne l'item créé.
        return layoutItem;
    }

}
