package org.blogstagram.models;

import java.util.Date;

public class DirectedFollow {
    private int id;
    private int fromId;
    private int toId;
    private Date createdAt;
    public static final int NO_ID = -1;
    public static final Date defaultTimeValue = null;

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
