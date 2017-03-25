package com.covilla.util.wechat.entity.customer;

public class NewsMessage extends CustomerBaseMessage {
	private News news;

	public News getNews() {
		return this.news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public NewsMessage(News news) {
		this.news = news;
	}

	public NewsMessage() {
	}
}
