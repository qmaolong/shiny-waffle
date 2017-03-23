package com.covilla.util.wechat.entity.menu;

public class CommonButton extends Button {
	private String type;
	private String key;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public CommonButton(String type, String key) {
		this.type = type;
		this.key = key;
	}

	public CommonButton() {
	}
}
