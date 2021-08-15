<%@ page import="java.util.*" %>
<%@ page import="org.blogstagram.models.NotificationUser" %>
<%@ page import="static org.blogstagram.notifications.NotificationConstants.NotificationTypes.*" %>
<%@ page import="static org.blogstagram.notifications.NotificationConstants.SeenStatus.*" %><%--
  Created by IntelliJ IDEA.
  User: tazo
  Date: 12.08.21
  Time: 01:07
  To change this template use File | Settings | File Templates.
--%>
<%
    List<NotificationUser> notifications = (ArrayList<NotificationUser>)request.getAttribute("notifications");
    Integer currentUserID = (Integer)request.getSession().getAttribute("currentUserID");
    if(currentUserID == null)
        currentUserID = -1;
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Notifications</title>
  <jsp:include page="/jsp/templates/bootstrap.jsp" />
</head>
<body>
<jsp:include page="/jsp/templates/nav.jsp" />

<div class="container">
    <div class="container">
        <% for(NotificationUser notification : notifications) {%>
        <div class="media my-3 border border-3 border-info p-3">
            <img class="mr-3" style="width:100px; height:100px; border-radius: 50%; object-fit: cover;" src="/blogstagram/<%= notification.getImage() %>" alt="Generic placeholder image">
            <div class="media-body">
                <h5 class="mt-0">
<%--                    <%= notification.getTitle() %>--%>
                    <span class="text-muted">

                                            <% if(notification.getNotificationType() == FOLLOW_NOTIFICATION) { %>
                                                <a href="/blogstagram/user/<%= notification.getFromUserId()%>">
                                                <%= notification.getFirstName() %> <%= notification.getLastName() %> now follows you.
                                                </a>
                                            <% } else if(notification.getNotificationType() == REQUESTED_FOLLOW_NOTIFICATION ){ %>
                                                    <a href="/blogstagram/user/<%= notification.getFromUserId()%>">
                                                    <%= notification.getFirstName() %> <%= notification.getLastName() %> sent you follow request.
                                                    </a>
                                                    <a class="btn btn-outline-primary">Accept</a>
                                                    <a class="btn btn-outline-primary">Decline</a>
                                            <% } else if(notification.getNotificationType() == ACCEPTED_FOLLOW_NOTIFICATION) { %>
                                                <a href="/blogstagram/user/<%= notification.getFromUserId()%>">
                                                <%= notification.getFirstName() %> <%= notification.getLastName() %> accepted your follow request.
                                                </a>
                                            <% } else if(notification.getNotificationType() == POST_LIKE_NOTIFICATION) { %>
                                                <a href="/blogstagram/blog/<%= notification.getBlogId()%>">
                                                <%= notification.getFirstName() %> <%= notification.getLastName() %> likes your post.
                                                </a>
                                            <% } else if(notification.getNotificationType() == POST_COMMENT_NOTIFICATION) { %>
                                                <a href="/blogstagram/blog/<%= notification.getBlogId()%>">
                                                <%= notification.getFirstName() %> <%= notification.getLastName() %> commented on your post.
                                                </a>
                                            <% } else  { %>
                                                <a href="/blogstagram/blog/<%= notification.getBlogId()%>">
                                                <%= notification.getFirstName() %> <%= notification.getLastName() %> likes your comment.
                                                </a>
                                            <%}%>

                                            <% if(notification.hasSeen() == NOT_SEEN) { %>
                                                    <p style="color:red;">NEW!</p>
                                            <% } %>
                    </span>


                    <span style="font-size:15px;">
                                        <span class="text-info">Date: </span>
                                        <span class="text-muted"> <%= notification.getCreationDate() %> </span>
                    </span>

                </h5>
            </div>
        </div>
        <% } %>

        <% if(notifications.size() == 0) { %>
        <div class="alert alert-warning">
            <h5 class="mt-0 text-center">No notifications </h5>
        </div>
        <% } %>
    </div>
</div>

<jsp:include page="/jsp/templates/footer.jsp" />
</body>
</html>