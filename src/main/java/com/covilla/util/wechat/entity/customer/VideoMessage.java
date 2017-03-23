package com.covilla.util.wechat.entity.customer;

public class VideoMessage extends MediaMessage {
	private Video video;

	public Video getVideo() {
		return this.video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public VideoMessage(Video video) {
		this.video = video;
	}

	public VideoMessage() {
	}
}
