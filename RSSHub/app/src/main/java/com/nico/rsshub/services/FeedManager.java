package com.nico.rsshub.services;

import android.content.Context;
import android.provider.MediaStore;

import com.nico.rsshub.modeles.Category;
import com.nico.rsshub.modeles.Feed;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 23/12/2016.
 */

public class FeedManager {
    public static void writeFeeds(Context context, List<Feed> feeds) {
        try {
            JSONArray jsonFeeds = new JSONArray();
            for(Feed feed : feeds) {
                JSONObject jsonFeed = new JSONObject();
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
                feed.setTitle(jsonObject.getString("title"));
                feed.setUrl(jsonObject.getString("url"));
                JSONArray jsonTags = jsonObject.getJSONArray("tags");
                for(int j = 0; i < jsonTags.length(); i++) {
                    feed.getTags().add(jsonTags.getString(j));
                }
                feed.setFavorite(jsonObject.getBoolean("isFavorite"));
                feed.setCacheFileName(jsonObject.getString("cacheFileName"));
                feeds.add(feed);
            }

            return feeds;
        } catch (Exception e) {
//            System.out.println(e);
            return null;
        }

    }


}
