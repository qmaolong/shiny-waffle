package com.covilla.util.wechat.entity.customer;

public class MusicMessage extends CustomerBaseMessage {
	private Music music;

	public Music getMusic() {
		return this.music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

	public MusicMessage(Music music) {
		this.music = music;
	}

	public MusicMessage() {
	}
}
