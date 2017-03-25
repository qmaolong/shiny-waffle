package com.covilla.util.wechat.entity;

import java.util.Date;

public class AccessToken {
	private String token;
	private int expiresIn;
	private Date expireAt;

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getExpiresIn() {
		return this.expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Date getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(Date expireAt) {
		this.expireAt = expireAt;
	}

	public AccessToken(String token, int expiresIn) {
		this.token = token;
		this.expiresIn = expiresIn;
	}

	public AccessToken() {
	}
}
