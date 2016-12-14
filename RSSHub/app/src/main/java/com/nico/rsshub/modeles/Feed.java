package com.nico.rsshub.modeles;

import java.util.ArrayList;
import java.util.List;

public class Feed {
	private String title;
	private String url;
	private String cacheFileName;
	private String category;
	private List<String> tags;

	public Feed() {
		super();
		this.title = "";
		this.url = "";
		this.cacheFileName = "";
		this.category = "";
		this.tags = new ArrayList<>();
	}

	public Feed(final String title, final String url, final List<String> tags, final String category, final String cacheFileName) {
		super();
		this.title = title;
		this.url = url;
		this.cacheFileName = cacheFileName;
		this.category = category;
		this.tags = tags;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getCacheFileName() {
		return this.cacheFileName;
	}

	public void setCacheFileName(final String cacheFileName) {
		this.cacheFileName = cacheFileName;
	}

	public String getCategory() { return category; }

	public void setCategory(String category) { this.category = category; }

	public List<String> getTags() {
		return this.tags;
	}

	public void setTags(final List<String> tags) {
		this.tags = tags;
	}

}
