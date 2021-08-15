package org.blogstagram.listeners;

public interface followNotificationSender {
    void sendFollowRequest(int fromUserId, int toUserId);
    void sendFollowNotification(int fromUserId, int toUserId);
}
