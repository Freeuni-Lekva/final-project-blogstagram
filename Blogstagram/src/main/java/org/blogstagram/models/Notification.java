package org.blogstagram.models;

import java.sql.Date;

public class Notification {
    private Integer id;
    private Integer type;
    private Integer fromUserId;
    private Integer toUserId;
    private Integer blogId;
    private Integer hasSeen;
    private Date createdAt;

    public Notification(Integer id, Integer type, Integer fromUserId, Integer toUserId, Integer blogId, Integer hasSeen, Date createdAt) {
        this.id = id;
        this.type = type;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.blogId = blogId;
        this.hasSeen = hasSeen;
        this.createdAt = createdAt;
    }

    public Notification(Notification other) {
        this.id = other.getNotificationId();
        this.type = other.getNotificationType();
        this.fromUserId = other.getFromUserId();
        this.toUserId = other.getToUserId();
        this.blogId = other.getBlogId();
        this.hasSeen = other.hasSeen();
        this.createdAt = other.getCreationDate();
    }

    public Integer getNotificationId() {
        return id;
    }

    public Integer getNotificationType() {
        return type;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public Integer hasSeen() {
        return hasSeen;
    }

    public Date getCreationDate() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
