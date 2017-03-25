package com.covilla.model.mongo.printer;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * Created by qmaolong on 2016/10/10.
 */
public class TicketForm {
    @Field("id")
    private Integer id;
    private String name;
    private String category;
    private Integer width;
    private List<TicketItemForm> header;
    private List<TicketItemForm> content;
    private List<TicketItemForm> tail;
    private Date createTime;
    private String creator;

    public enum  TicketZone{
        Header(0, "票头区域"),
        Content(1, "票中区域"),
        Tail(2, "票尾区域");

        private Integer code;
        private String name;

        TicketZone(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public List<TicketItemForm> getHeader() {
        return header;
    }

    public void setHeader(List<TicketItemForm> header) {
        this.header = header;
    }

    public List<TicketItemForm> getContent() {
        return content;
    }

    public void setContent(List<TicketItemForm> content) {
        this.content = content;
    }

    public List<TicketItemForm> getTail() {
        return tail;
    }

    public void setTail(List<TicketItemForm> tail) {
        this.tail = tail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
