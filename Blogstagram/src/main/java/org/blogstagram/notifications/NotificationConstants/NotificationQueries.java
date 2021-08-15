package org.blogstagram.notifications.NotificationConstants;

import static org.blogstagram.notifications.NotificationConstants.SeenStatus.*;

public class NotificationQueries {
    public static final String GET_SINGLE_NOTIFICATION = "SELECT * FROM notifications WHERE id = ?";
    public static final String GET_NOTIFICATIONS = "SELECT * FROM notifications WHERE to_user_id = ? ORDER BY created_at DESC";
    public static final String ADD_NOTIFICATION = "INSERT INTO notifications(type, from_user_id, to_user_id, blog_id, has_seen, created_at) VALUES( ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_SEEN_STATUS = "UPDATE notifications SET has_seen = " + SEEN +" WHERE id = ?";
    public static final String GET_NOTIFICATION_USER = "SELECT notifications.id, users.firstname, users.lastname, users.image \n" +
            "FROM notifications \n" +
            "JOIN users ON notifications.from_user_id = users.id\n" +
            "WHERE notifications.to_user_id = ?\n" +
            "ORDER BY notifications.id DESC;";
    public static final String NOTIFICATION_EXISTENCE = "SELECT COUNT(id) FROM notifications WHERE from_user_id = ? AND to_user_id = ? AND type = ?;";
}
