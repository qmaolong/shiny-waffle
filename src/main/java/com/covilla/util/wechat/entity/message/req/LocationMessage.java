package com.covilla.util.wechat.entity.message.req;

public class LocationMessage extends BaseMessage {
	private String Location_X;
	private String Location_Y;
	private String Scale;
	private String Label;

	public String getLocation_X() {
		return this.Location_X;
	}

	public void setLocation_X(String location_X) {
		this.Location_X = location_X;
	}

	public String getLocation_Y() {
		return this.Location_Y;
	}

	public void setLocation_Y(String location_Y) {
		this.Location_Y = location_Y;
	}

	public String getScale() {
		return this.Scale;
	}

	public void setScale(String scale) {
		this.Scale = scale;
	}

	public String getLabel() {
		return this.Label;
	}

	public void setLabel(String label) {
		this.Label = label;
	}

	public LocationMessage(String location_X, String location_Y, String scale,
			String label) {
		this.Location_X = location_X;
		this.Location_Y = location_Y;
		this.Scale = scale;
		this.Label = label;
	}
}
