package com.nico.rsshub.controllers;

import android.content.Context;
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
import java.util.List;
import java.util.Locale;

/**
 * Created by Nico on 19/12/2016.
 */

public class LoadFeedTask extends Thread {

    private static String cacheDirectory = Environment.getExternalStorageDirectory() + "/RSS Hub/cache/";

    private List<Information> informationList;

    private List<DownloadImageTask> downloadImageTasks;

    private long refreshTimeInMs;

    private Feed feed;

    public LoadFeedTask(Feed feed) {
        this(feed,0);
    }

    public LoadFeedTask(Feed feed, long refreshTimeInMs) {
        super();
        this.feed = feed;
        this.refreshTimeInMs = refreshTimeInMs;
        this.informationList = new ArrayList<>();
        this.downloadImageTasks = new ArrayList<>();
    }

    public List<Information> getInformationList() {
        return this.informationList;
    }

    @Override
    public void run() {
        super.run();
        try {
            this.informationList = parseAFeed(this.feed);
            if(this.informationList != null && !this.informationList.isEmpty()) {
                Controller.getInstance().getMutexFeeds().acquire();
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
                Controller.getInstance().getInformationList().addAll(this.informationList);
                Controller.getInstance().getFeeds().put(feed,this.informationList);
                Controller.getInstance().getMutexFeeds().release();
            }
        } catch (Exception e) {
            this.informationList = null;
            System.out.println(e);
        }

        //wait ending of children threads
        for(DownloadImageTask downloadImageTask : this.downloadImageTasks) {
            try {
                downloadImageTask.join();
                if(downloadImageTask.getBitmap() != null) {
                    //TODO mutex
                    Controller.getInstance().getMutexImages().acquire();
                    Controller.getInstance().getImages().put(downloadImageTask.getInformation(),downloadImageTask.getBitmap());
                    Controller.getInstance().getMutexImages().release();
                }
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

//                image
                if(item.getChild("enclosure") != null && item.getChild("enclosure").getAttributeValue("type").contains("image")) {
                    information.setImage(item.getChild("enclosure").getAttributeValue("url"));
                    DownloadImageTask downloadImageTask = new DownloadImageTask(information);
                    this.downloadImageTasks.add(downloadImageTask);
                    downloadImageTask.start();
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
