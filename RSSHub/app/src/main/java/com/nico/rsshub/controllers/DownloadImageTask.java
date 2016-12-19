package com.nico.rsshub.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by Nico on 19/12/2016.
 */

public class DownloadImageTask extends Thread {

    private String imageURL = null;

    private Bitmap bitmap;

    public DownloadImageTask(String imageURL) {
        super();
        this.bitmap = null;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void run() {
        super.run();
        Bitmap bitmap;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);

            this.bitmap = bitmap;
        } catch (Exception e) {
            bitmap = null;
        }

    }

}