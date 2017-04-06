package com.nico.rsshub.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Nico on 23/12/2016.
 */

public class FeedManager {
    public static void writeFeeds(Context context, List<Feed> feeds) {
        try {
            JSONArray jsonFeeds = new JSONArray();
            for(Feed feed : feeds) {
                JSONObject jsonFeed = new JSONObject();
                jsonFeed.put("source",feed.getSource());
                jsonFeed.put("title",feed.getTitle());
                jsonFeed.put("url",feed.getUrl());
                JSONArray jsonTags = new JSONArray();
                for(String tag : feed.getTags()) {
                    jsonTags.put(tag);
                }
                jsonFeed.put("tags",jsonTags);
                jsonFeed.put("isFavorite",feed.isFavorite());
                jsonFeed.put("cacheFileName",feed.getCacheFileName());
                jsonFeeds.put(jsonFeed);
            }
            FileWriter file = new FileWriter(context.getFilesDir().getAbsoluteFile() + "/feeds.json");
            file.write(jsonFeeds.toString());
            file.flush();
            file.close();
        } catch (Exception e) {
        }
    }

    public static List<Feed> readFeeds(Context context) {
        try {

            InputStream is = new FileInputStream(context.getFilesDir().getAbsoluteFile() + "/feeds.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            JSONArray jsonArray = new JSONArray(new String(buffer, "UTF-8"));
            List<Feed> feeds = new ArrayList<>();

            for(int i = 0; i < jsonArray.length(); i++) {
                Feed feed = new Feed();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                feed.setSource(jsonObject.getString("source"));
                feed.setTitle(jsonObject.getString("title"));
                feed.setUrl(jsonObject.getString("url"));
                JSONArray jsonTags = jsonObject.getJSONArray("tags");
                for(int j = 0; j < jsonTags.length(); j++) {
                    feed.getTags().add(jsonTags.getString(j));
                }
                feed.setFavorite(jsonObject.getBoolean("isFavorite"));
                feed.setCacheFileName(jsonObject.getString("cacheFileName"));
                feeds.add(feed);
            }
            return feeds;
        } catch (Exception e) {
//            System.out.println(e);
            return new ArrayList<>();
        }
    }

    public static void saveBitmap(Context context, Bitmap bitmap, Information information) {
        FileOutputStream out = null;
        try {
            File cacheDir = context.getCacheDir().getAbsoluteFile();
            File imagesDir = new File(cacheDir.getAbsolutePath() + "/images");
            if(!imagesDir.exists())
            {
                imagesDir.mkdir();
            }
            out = new FileOutputStream(imagesDir.getAbsoluteFile() + "/" + Integer.toString(information.getImage().hashCode()));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeUnusedImages(Context context, Set<String> images) {
        File cacheDir = context.getCacheDir().getAbsoluteFile();
        File imagesDir = new File(cacheDir.getAbsolutePath() + "/images");
        if(imagesDir.exists())
        {
           for(File file : imagesDir.listFiles()) {
               if(!images.contains(file.getName())) {
                   file.delete();
               }
           }
        }
    }

    public static boolean imageExists(Context context,Information information) {
        File cacheDir = context.getCacheDir().getAbsoluteFile();
        File imagesDir = new File(cacheDir.getAbsolutePath() + "/images");
        if(information.getImage() == null) {
            return  false;
        }
        return new File(imagesDir + "/" + Integer.toString(information.getImage().hashCode())).exists();
    }

    public static Bitmap loadImage(Context context,Information information) {
        File cacheDir = context.getCacheDir().getAbsoluteFile();
        File imagesDir = new File(cacheDir.getAbsolutePath() + "/images");
        if(imageExists(context,information)){
            return BitmapFactory.decodeFile(imagesDir + "/" + Integer.toString(information.getImage().hashCode()));
        }
        return null;
    }

}
