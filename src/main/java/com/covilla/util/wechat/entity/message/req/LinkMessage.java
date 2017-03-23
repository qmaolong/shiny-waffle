package com.covilla.util.wechat.entity.message.req;

public class LinkMessage extends BaseMessage {
	private String Title;
	private String Description;
	private String Url;

	public String getTitle() {
		return this.Title;
	}

	public void setTitle(String title) {
		this.Title = title;
	}

	public String getDescription() {
		return this.Description;
	}

	public void setDescription(String description) {
		this.Description = description;
	}

	public String getUrl() {
		return this.Url;
	}

	public void setUrl(String url) {
		this.Url = url;
	}

	public LinkMessage(String title, String description, String url) {
		this.Title = title;
		this.Description = description;
		this.Url = url;
	}
}
