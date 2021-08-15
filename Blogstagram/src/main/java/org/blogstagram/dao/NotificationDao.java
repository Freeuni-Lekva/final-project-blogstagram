package org.blogstagram.dao;

import org.blogstagram.listeners.followNotificationSender;
import org.blogstagram.models.Notification;
import org.blogstagram.models.NotificationUser;

import java.sql.*;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import static org.blogstagram.notifications.NotificationConstants.NotificationQueries.*;
import static org.blogstagram.notifications.NotificationConstants.NotificationTypes.*;
import static org.blogstagram.notifications.NotificationConstants.SeenStatus.*;

public class NotificationDao implements followNotificationSender {

    private static final int NO_NOTIFICATION = 0;

    Connection connection;

    public NotificationDao(Connection connection) {
        this.connection = connection;
    }

    //adds new notification in database

    public void addNotification(Notification newNotification) throws SQLException {
        if(newNotification.getNotificationId() != null)
            throw new RuntimeException("Notification with ID " + newNotification.getNotificationId() + " Already Exists");
        PreparedStatement statement = connection.prepareStatement(ADD_NOTIFICATION, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, newNotification.getNotificationType());
        statement.setInt(2, newNotification.getFromUserId());
        statement.setInt(3, newNotification.getToUserId());
        statement.setInt(4, newNotification.getBlogId());
        statement.setInt(5, newNotification.hasSeen());
        statement.setDate(6, newNotification.getCreationDate());
        int affectedRows = statement.executeUpdate();
        if(affectedRows == 0)
            throw new SQLException("Adding notification in database failed");
        ResultSet resultSet = statement.getGeneratedKeys();
        if(resultSet.next())
            newNotification.setId(resultSet.getInt(1));
        else
            throw new SQLException("ID generation failed");
    }

    public boolean notificationExists(int fromUserId, int toUserId, int notificationType) throws SQLException{
        PreparedStatement statement = connection.prepareStatement(NOTIFICATION_EXISTENCE);
        statement.setInt(1, fromUserId);
        statement.setInt(2, toUserId);
        statement.setInt(3, notificationType);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int notificationCount = resultSet.getInt(1);
        if(notificationCount == NO_NOTIFICATION)
            return false;
        return true;
    }

    private List<Notification> getNotificationsHelper(ResultSet resultSet) throws SQLException{
        ArrayList<Notification> notifications = new ArrayList<>();
        while (resultSet.next()) {
            int notificationId = resultSet.getInt(1);
            int notificationType = resultSet.getInt(2);
            int fromUserId = resultSet.getInt(3);
            int toUserId = resultSet.getInt(4);
            int blogId = resultSet.getInt(5);
            int hasSeen = resultSet.getInt(6);
            Date creationDate = resultSet.getDate(7);
            Notification notification = new Notification(notificationId, notificationType, fromUserId, toUserId, blogId, hasSeen, creationDate);
            notifications.add(notification);
        }
        return notifications;
    }

    //gets already seen notifications

    public List<Notification> getNotifications(int userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_NOTIFICATIONS);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();
        return getNotificationsHelper(resultSet);
    }

    private void updateRow(int notificationId) throws SQLException{
        PreparedStatement statement = connection.prepareStatement(UPDATE_SEEN_STATUS);
        statement.setInt(1, notificationId);
        statement.executeUpdate();
    }

    //after seeing not seen notifications, makes them seen

    public void markNotificationsAsSeen(int userId) throws SQLException {
        List<Notification> notifications = getNotifications(userId);
        for(int i = 0; i < notifications.size(); i++) {
            if(notifications.get(i).hasSeen() == NOT_SEEN)
                updateRow(notifications.get(i).getNotificationId());
        }
    }


    public int getNewNotificationsCount(int userId) throws SQLException {
        return getNotifications(userId).size();
    }

    public Notification getNotification(int notificationId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_SINGLE_NOTIFICATION);
        statement.setInt(1, notificationId);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int notId = resultSet.getInt(1);
        int notificationType = resultSet.getInt(2);
        int fromUserId = resultSet.getInt(3);
        int toUserId = resultSet.getInt(4);
        int blogId = resultSet.getInt(5);
        int hasSeen = resultSet.getInt(6);
        Date creationDate = resultSet.getDate(7);
        return new Notification(notificationId, notificationType, fromUserId, toUserId, blogId, hasSeen, creationDate);
    }

    public List<NotificationUser> getNotificationUserData(int userId) throws SQLException{
        PreparedStatement statement = connection.prepareStatement(GET_NOTIFICATION_USER);
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<NotificationUser> result = new ArrayList<>();
        while (resultSet.next()) {
            int notificationId = resultSet.getInt(1);
            String firstname = resultSet.getString(2);
            String lastname = resultSet.getString(3);
            String image = resultSet.getString(4);
            NotificationUser newNotificationUser = new NotificationUser(firstname, lastname, image, getNotification(notificationId));
            result.add(newNotificationUser);
        }
        return result;
    }

    @Override
    public void sendFollowRequest(int fromUserId, int toUserId) {
        Notification notification = new Notification(null, REQUESTED_FOLLOW_NOTIFICATION, fromUserId, toUserId, null, NOT_SEEN, new Date(System.currentTimeMillis()));
        try {
            addNotification(notification);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void sendFollowNotification(int fromUserId, int toUserId) {
        Notification notification = new Notification(null, FOLLOW_NOTIFICATION, fromUserId, toUserId, null, NOT_SEEN, new Date(System.currentTimeMillis()));
        try {
            addNotification(notification);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

