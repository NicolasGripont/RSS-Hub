package com.nico.rsshub.modeles;

import java.util.List;

public class Feed {
	private String title;
	private String url;
	private String cacheFileName;
	private Category category;
	private boolean isFavorite;

	public Feed() {
		super();
		this.title = "";
		this.url = "";
		this.cacheFileName = "";
		this.category = Category.OTHER;
		this.isFavorite = false;
	}

	public Feed(final String title, final String url, final List<String> tags, final Category category, final String cacheFileName, final boolean isFavorite) {
		super();
		this.title = title;
		this.url = url;
		this.cacheFileName = cacheFileName;
		this.category = category;
		this.isFavorite = isFavorite;
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

	public Category getCategory() { return category; }

	public void setCategory(Category category) { this.category = category; }

	public boolean isFavorite() { return isFavorite; }

	public void setFavorite(boolean favorite) { isFavorite = favorite; }

	@Override
	public String toString() {
		return "Feed{" +
				"title='" + title + '\'' +
				", url='" + url + '\'' +
				", cacheFileName='" + cacheFileName + '\'' +
				", category=" + category +
				", isFavorite=" + isFavorite +
				'}';
	}
}
