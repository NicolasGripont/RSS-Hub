package com.nico.rsshub.modeles;

import java.util.Date;

public class Information {
	private String title;
	private String url;
	private String description;
	private Date datePublication;
	private Feed feed;
	private String image;

	public Information() {
		super();
		this.title = "";
		this.url = "";
		this.description = "";
		this.datePublication = null;
		this.feed = null;
		this.image = "";
	}

	public Information(final String title, final String url, final String description, final Date datePublication,
			final Feed feed, String image) {
		super();
		this.title = title;
		this.url = url;
		this.description = description;
		this.datePublication = datePublication;
		this.feed = feed;
		this.image = image;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Date getDatePublication() {
		return this.datePublication;
	}

	public void setDatePublication(final Date datePublication) {
		this.datePublication = datePublication;
	}

	public Feed getFeed() {
		return this.feed;
	}

	public void setFeed(final Feed feed) {
		this.feed = feed;
	}

	public String getImage() { return image; }

	public void setImage(String image) { this.image = image; }

	@Override
	public String toString() {
		return "Information [title=" + this.title + ", url=" + this.url + ", description=" + this.description
				+ ", datePublication=" + this.datePublication + ", feed=" + this.feed + "]";
	}

}
