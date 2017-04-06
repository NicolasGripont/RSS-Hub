package com.nico.rsshub.controllers;

import android.graphics.Bitmap;
import android.os.Environment;

import com.nico.rsshub.modeles.Feed;
import com.nico.rsshub.modeles.Information;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Nico on 19/12/2016.
 */

public class LoadFeedThread extends Thread {

    //TODO mettre en local

    private List<Information> informationList;

    private List<DownloadImageThread> downloadImageThreads;

    private long refreshTimeInMs;

    private Feed feed;

    public LoadFeedThread(Feed feed) {
        this(feed,0);
    }

    public LoadFeedThread(Feed feed, long refreshTimeInMs) {
        super();
        this.feed = feed;
        this.refreshTimeInMs = refreshTimeInMs;
        this.informationList = new ArrayList<>();
        this.downloadImageThreads = new ArrayList<>();
    }

    public List<Information> getInformationList() {
        return this.informationList;
    }

    public Feed getFeed() { return feed; }

    @Override
    public void run() {
        super.run();
        try {
            this.informationList = parseAFeed(this.feed);
        } catch (Exception e) {
            System.out.println(e);
        }

        //wait ending of children threads
        for(DownloadImageThread downloadImageThread : this.downloadImageThreads) {
            try {
                downloadImageThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Information> parseAFeed(Feed feed) throws IOException, JDOMException {
        URL url;

        url = new URL(feed.getUrl());

        // Cache File
        final File cacheFile = this.createCacheFile(url, feed);

        // Parsing XML
        final List<Information> informationsList = this.parseXmlFile(cacheFile, feed);

        return informationsList;
    }

    private File createCacheFile(final URL url, final Feed feed) throws IOException {
        final File cacheFile = new File(feed.getCacheFileName());
        try {
            final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("User-Agent", "Mozilla/4.0");

            final InputStream inputStream = httpURLConnection.getInputStream();
//            if (!cacheFile.exists()) {
                cacheFile.getParentFile().mkdir();
                cacheFile.createNewFile();
                final OutputStream outputStream = new FileOutputStream(cacheFile);

                int read = 0;
                final byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                outputStream.close();
//            } else { // TODO : refresh if date is old...
//
//            }

        } catch (final IOException e) {

        }

        return cacheFile;
    }

    private List<Information> parseXmlFile(final File xmlFile, Feed feed) throws JDOMException, IOException {
        final List<Information> informationList = new ArrayList<>();
        final SAXBuilder sxb = new SAXBuilder();

        final Document document = sxb.build(xmlFile);
        final Element rss = document.getRootElement();

        //RSS 2.0
        if(rss.getAttribute("version").getValue().equals("2.0")) {
            final Element channel = rss.getChild("channel");

            final List<Element> itemsList = channel.getChildren("item");

            for (int i = 0; i < itemsList.size(); i++) {
                final Element item = itemsList.get(i);
                final Information information = new Information();

                //title
                information.setTitle(item.getChild("title").getValue());

                //date
                information.setFeed(feed);
                try {
                    DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                    Date date = dateFormat.parse(this.formatDate(item.getChild("pubDate").getValue()));
                    information.setDatePublication(date);
                } catch (ParseException e) {
                    information.setDatePublication(null);
                }

                //image (on charge l'image seulement si date publication inférieur à un jour
                if( information.getDatePublication() != null &&
                        ((new Date()).getTime() - information.getDatePublication().getTime()) < 24*60*60*1000) {
                    if (item.getChild("enclosure") != null && item.getChild("enclosure").getAttributeValue("type").contains("image")) {
                        information.setImage(item.getChild("enclosure").getAttributeValue("url"));
                        DownloadImageThread downloadImageThread = new DownloadImageThread(information);
                        this.downloadImageThreads.add(downloadImageThread);
                        downloadImageThread.start();
                    } else {
                        information.setImage(null);
                    }
                } else {
                    information.setImage(null);
                }
                //url
                information.setUrl(item.getChild("link").getValue());

                informationList.add(information);
            }
        }

        return informationList;
    }

    private String formatDate(String date) {
        String newDate = "";

        char previousC = ' ';
        for (char c : date.toCharArray()){
            if(c != ' ') {
                newDate += c;
            } else if(c == ' ' && previousC != ' ') {
                newDate += c;
            }
            previousC = c;
        }

        return newDate;
    }
}
