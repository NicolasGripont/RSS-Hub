package com.nico.rsshub.modeles;

import java.util.ArrayList;
import java.util.List;

public class Feed {
	private String source;
	private String title;
	private String url;
	private String cacheFileName;
	private List<String> tags;
	private boolean isFavorite;

	public Feed() {
		super();
		this.source = "";
		this.title = "";
		this.url = "";
		this.cacheFileName = "";
		this.tags = new ArrayList<>();
		this.isFavorite = false;
	}

	public Feed(final String source, final String title, final String url, final String cacheFileName, final List<String> tags, final boolean isFavorite) {
		super();
		this.source = source;
		this.title = title;
		this.url = url;
		this.cacheFileName = cacheFileName;
		this.tags = tags;
		this.isFavorite = isFavorite;
	}

	public String getSource() { return source; }

	public void setSource(String source) { this.source = source; }

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

	public List<String> getTags() { return tags; }

	public void setTags(List<String> tags) { this.tags = tags; }

	public boolean isFavorite() { return isFavorite; }

	public void setFavorite(boolean favorite) { isFavorite = favorite; }

	@Override
	public String toString() {
		return "Feed{" +
				"title='" + title + '\'' +
				", url='" + url + '\'' +
				", cacheFileName='" + cacheFileName + '\'' +
				", tags=" + tags +
				", isFavorite=" + isFavorite +
				'}';
	}
}
