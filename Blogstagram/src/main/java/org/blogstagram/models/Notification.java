package org.blogstagram.models;

import java.util.Date;

public class Notification {
    private int id;
    private int type;
    private int fromUserId;
    private int toUserId;
    private int blogId;
    private int hasSeen;
    private Date createdAt;

    private static final int SEEN = 1;

    public Notification(int id, int type, int fromUserId, int toUserId, int blogId, int hasSeen, Date createdAt) {
        this.id = id;
        this.type = type;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.blogId = blogId;
        this.hasSeen = hasSeen;
        this.createdAt = createdAt;
    }

    public int getNotificationId() {
        return id;
    }

    public int getNotificationType() {
        return type;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public int getBlogId() {
        return blogId;
    }

    public boolean hasSeen() {
        return hasSeen == SEEN;
    }

    public Date getCreationDate() {
        return createdAt;
    }

}
