package com.covilla.model.mongo.printer;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by qmaolong on 2016/10/10.
 */
public class TicketItemForm {
    private String name;
    private String title;
    private String property;
    private Integer width;
    private Boolean isWrap;
    private String fontFamily;
    private Integer fontSize;
    private String textStyle;
    private String textSize;
    private String overFLow;
    private String alignment;
    @Field("formator")
    private String formatter;
    private String type;
    private String fillSpace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Boolean getWrap() {
        return isWrap;
    }

    public void setWrap(Boolean wrap) {
        isWrap = wrap;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getTextSize() {
        return textSize;
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }

    public String getOverFLow() {
        return overFLow;
    }

    public void setOverFLow(String overFLow) {
        this.overFLow = overFLow;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
    }

    public String getFillSpace() {
        return fillSpace;
    }

    public void setFillSpace(String fillSpace) {
        this.fillSpace = fillSpace;
    }
}
