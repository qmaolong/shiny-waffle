package com.covilla.util.wechat.entity.customer;

public class MediaMessage extends CustomerBaseMessage {
	private Media media;

	public Media getMedia() {
		return this.media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public MediaMessage(Media media) {
		this.media = media;
	}

	public MediaMessage() {
	}
}
