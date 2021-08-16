package org.blogstagram.notifications.servlets;

import org.blogstagram.dao.NotificationDao;
import org.blogstagram.dao.SqlBlogDAO;
import org.blogstagram.models.Notification;
import org.blogstagram.models.NotificationUser;
import org.blogstagram.models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/notifications")
public class NotificationsServlet extends HttpServlet {

    private boolean isUserLoggedIn(HttpServletRequest req) {
        Integer userID = (Integer) req.getSession().getAttribute("currentUserID");
        String nickname = (String) req.getSession().getAttribute("currentUserNickname");
        return userID != null && nickname != null;
    }

    private static Connection getConnectionFromContext(HttpServletRequest req){
        Connection connection = (Connection) req.getServletContext().getAttribute("dbConnection");
        return connection;
    }

    private NotificationDao getNotificationDao(HttpServletRequest req){
        NotificationDao notificationDao = (NotificationDao) req.getSession().getAttribute("NotificationDao");
        return notificationDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!isUserLoggedIn(request)) {
            response.sendRedirect("/blogstagram/login");
            return;
        }
        Integer userID = (Integer) request.getSession().getAttribute("currentUserID");
        NotificationDao notificationDao = getNotificationDao(request);
        try {
            List<NotificationUser> notifications = notificationDao.getNotificationUserData(userID);
            request.setAttribute("notifications", notifications);
            notificationDao.markNotificationsAsSeen(userID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/jsp/notifications/notifications.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection dbConnection = getConnectionFromContext(request);
        if(!isUserLoggedIn(request)) {
            response.sendRedirect("/blogstagram/login");
            return;
        }
        Integer userID = (Integer) request.getSession().getAttribute("currentUserID");
        NotificationDao notificationDao = getNotificationDao(request);


    }
}
