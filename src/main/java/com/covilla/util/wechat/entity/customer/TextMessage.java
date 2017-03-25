package com.covilla.util.wechat.entity.customer;

public class TextMessage extends CustomerBaseMessage {
	private Text text;

	public Text getText() {
		return this.text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public TextMessage(Text text) {
		this.text = text;
	}

	public TextMessage() {
	}
}
