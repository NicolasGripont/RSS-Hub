package com.nico.rsshub.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nico.rsshub.modeles.Information;

import java.io.InputStream;

/**
 * Created by Nico on 19/12/2016.
 */

public class DownloadImageThread extends Thread {

    private Information information;

    private Bitmap bitmap;

    public DownloadImageThread(Information information) {
        super();
        this.bitmap = null;
        this.information = information;
    }

    public Information getInformation() {
        return information;
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
            InputStream input = new java.net.URL(this.information.getImage()).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);

            this.bitmap = bitmap;
        } catch (Exception e) {
            bitmap = null;
        }

    }

}