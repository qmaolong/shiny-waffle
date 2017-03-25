package com.covilla.util.wechat.entity.menu;

public class Menu {
	private Button[] button;

	public Button[] getButton() {
		return this.button;
	}

	public void setButton(Button[] button) {
		this.button = button;
	}

	public Menu(Button[] button) {
		this.button = button;
	}

	public Menu() {
	}
}
