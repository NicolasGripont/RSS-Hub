package com.nico.rsshub.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nico.rsshub.modeles.Information;
import com.nico.rsshub.services.FeedManager;

import java.io.InputStream;

/**
 * Created by Nico on 19/12/2016.
 */

public class DownloadImageThread extends Thread {

    private Information information;


    public DownloadImageThread(Information information) {
        super();
        this.information = information;
    }

    public Information getInformation() {
        return information;
    }


    @Override
    public void run() {
        super.run();
        Bitmap bitmap;
        try {
            if(!FeedManager.imageExists(Controller.getInstance().getCurrentActivity().getApplicationContext(),information)) {
                // Download Image from URL
                InputStream input = new java.net.URL(information.getImage()).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);

                FeedManager.saveBitmap(Controller.getInstance().getCurrentActivity().getApplicationContext(), bitmap, information);
            }
        } catch (Exception e) {
        }

    }

}