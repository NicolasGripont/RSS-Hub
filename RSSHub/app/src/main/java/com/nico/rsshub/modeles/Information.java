package com.nico.rsshub.modeles;

public class Information {
	private String title;
	private String url;
	private String description;
	private String datePublication;
	private String sourceName;

	public Information() {
		super();
		this.title = "";
		this.url = "";
		this.description = "";
		this.datePublication = "";
		this.sourceName = "";
	}

	public Information(final String title, final String url, final String description, final String datePublication,
			final String sourceName) {
		super();
		this.title = title;
		this.url = url;
		this.description = description;
		this.datePublication = datePublication;
		this.sourceName = sourceName;
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

	public String getDatePublication() {
		return this.datePublication;
	}

	public void setDatePublication(final String datePublication) {
		this.datePublication = datePublication;
	}

	public String getSourceName() {
		return this.sourceName;
	}

	public void setSourceName(final String sourceName) {
		this.sourceName = sourceName;
	}

	@Override
	public String toString() {
		return "Information [title=" + this.title + ", url=" + this.url + ", description=" + this.description
				+ ", datePublication=" + this.datePublication + ", sourceName=" + this.sourceName + "]";
	}

}
