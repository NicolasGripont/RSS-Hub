package com.nico.rsshub.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.nico.rsshub.controllers.DownloadImageTask;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FeedParser {

	private static String cacheDirectory = Environment.getExternalStorageDirectory() + "/RSS Hub/cache/";

	private long refreshTimeInMs;

	private List<DownloadImageTask> downloadImageTasks;

	public FeedParser() {
		this(0);
	}

	public FeedParser(final long refreshTime) {
		super();
		this.refreshTimeInMs = refreshTime;
		downloadImageTasks = new ArrayList<>();
	}

	public long getRefreshTimeInMs() {
		return this.refreshTimeInMs;
	}

	public void setRefreshTimeInMs(final long refreshTimeInMs) { this.refreshTimeInMs = refreshTimeInMs; }

	public List<Information> parse(Feed... feeds) throws IOException, JDOMException {
		final List<Information> informationList = new ArrayList<>();

		for (final Feed feed : feeds) {
			final List<Information> informationListTemp = parseAFeed(feed);
			if(!informationListTemp.isEmpty()) {
				informationList.addAll(informationListTemp);
			}
		}

		return informationList;
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
		final File cacheFile = new File(FeedParser.cacheDirectory + feed.getCacheFileName());
		try {
			final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.addRequestProperty("User-Agent", "Mozilla/4.0");

			final InputStream inputStream = httpURLConnection.getInputStream();
			if (!cacheFile.exists()) {
				cacheFile.getParentFile().mkdirs();
				cacheFile.createNewFile();
				final OutputStream outputStream = new FileOutputStream(cacheFile);

				int read = 0;
				final byte[] bytes = new byte[1024];

				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				outputStream.close();
			} else { // TODO : refresh if date is old...

			}

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

				//image
				if(item.getChild("enclosure") != null && item.getChild("enclosure").getAttributeValue("type").contains("image")) {
					information.setImage(item.getChild("enclosure").getAttributeValue("url"));
				}

				//url
				information.setUrl(item.getChild("link").getValue());

				informationList.add(information);
			}
		}

		return informationList;
	}

	//TODO : ne pas parser tous le fichier (juste compter le nb de balises 'item'
	public boolean tryParseAFeed(final Feed feed, List<Information> informationsResult) {
		boolean result = false;
		try {
			URL url = new URL(feed.getUrl());

			// Cache File
			final File cacheFile = this.createCacheFile(url, feed);

			// Parsing XML
			informationsResult.addAll(this.parseXmlFile(cacheFile,feed));

			if(informationsResult.size() > 0) {
				result = true;
			}
		} catch (Exception e) {

		}
		return result;
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
