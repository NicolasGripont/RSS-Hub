package com.nico.rsshub.services;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FeedParser {

	private static String cacheDirectory = Environment.getExternalStorageDirectory() + "/RSS Hub/cache/";

	private long refreshTimeInMs;

	public FeedParser() {
		super();
	}

	public FeedParser(final long refreshTime) {
		super();
		this.refreshTimeInMs = refreshTime;
	}

	public long getRefreshTimeInMs() {
		return this.refreshTimeInMs;
	}

	public void setRefreshTimeInMs(final long refreshTimeInMs) {
		this.refreshTimeInMs = refreshTimeInMs;
	}

	public List<Information> parse(final List<Feed> feedList) {
		final List<Information> informationsList = new ArrayList<>();

		for (final Feed feed : feedList) {

		}

		return informationsList;
	}

	public List<Information> parseAFeed(final Feed feed) throws MalformedURLException, IOException, JDOMException {
		URL url;

		url = new URL(feed.getUrl());

		// Cache File
		final File cacheFile = this.createCacheFile(url, feed);

		// Parsing XML
		final List<Information> informationsList = this.parseXmlFile(cacheFile);

		return informationsList;
	}

	private File createCacheFile(final URL url, final Feed feed) throws IOException {
		final InputStream inputStream = url.openStream();
		final File cacheFile = new File(FeedParser.cacheDirectory + feed.getCacheFileName());

		if (!cacheFile.exists()) {
			cacheFile.getParentFile().getParentFile().mkdirs();
			cacheFile.getParentFile().mkdirs();
			cacheFile.createNewFile();
		}

		//TODO : verifier que le test est bon
		if(new Date().getTime() -  cacheFile.lastModified() > refreshTimeInMs) {
			final OutputStream outputStream = new FileOutputStream(cacheFile);

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.close();
		}

		return cacheFile;
	}

	private List<Information> parseXmlFile(final File xmlFile) throws JDOMException, IOException {
		final List<Information> informationsList = new ArrayList<>();
		final SAXBuilder sxb = new SAXBuilder();

		final Document document = sxb.build(xmlFile);
		final Element rss = document.getRootElement();
		final Element channel = rss.getChild("channel");

		final List<Element> itemsList = channel.getChildren("item");

		for (int i = 0; i < itemsList.size(); i++) {
			final Element item = itemsList.get(i);
			final Information information = new Information();
			information.setTitle(item.getChild("title").getValue());
			informationsList.add(information);
		}

		return informationsList;
	}

	public boolean tryParseAFeed(final Feed feed, List<Information> informationsResult) {
		boolean result = false;
		try {
			URL url = new URL(feed.getUrl());

			// Cache File
			final File cacheFile = this.createCacheFile(url, feed);

			// Parsing XML
			informationsResult.addAll(this.parseXmlFile(cacheFile));

			if(informationsResult.size() > 0) {
				result = true;
			}
		} catch (Exception e) {

		}
		return result;
	}


}
