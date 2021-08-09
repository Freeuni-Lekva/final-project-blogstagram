<%@ page import="org.blogstagram.models.User" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 15/07/2021
  Time: 19:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String userNickname = (String)request.getAttribute("UserNickname");
    List<User> users = (List<User>)request.getAttribute("Users");
    String status = (String)request.getAttribute("FollowStatus");
%>

<html>
    <head>
        <title>Blogstagram</title>
        <jsp:include page="/jsp/templates/bootstrap.jsp" />
        <style>
        </style>
    </head>
    <body>
        <jsp:include page="/jsp/templates/nav.jsp" />

        <div class="alert alert-info text-center">
            <h1> <abbr> <%= userNickname %> </abbr> <%= (status.equals("Followers") ? ("followers") : ("following") )%> list</h1>

        </div>
        <div class="container">
            <% for(User user: users) { %>

                <div class="media border p-3">
                    <img src="<%= user.getImage() %>" alt="John Doe" class="mr-3 mt-3 rounded-circle" style="width:100px;height:100px;">
                    <div class="media-body mt-4">
                        <h4> <a href="/blogstagram/user/<%= user.getNickname() %>"> <%= user.getFirstname() %> <%= user.getLastname() %> </a> <small><i>Joined <%= user.getCreatedAt() %></i></small></h4>
                        <p><%=user.getPrivacy().equals(User.PRIVATE) ? "Private" : "Public" %> account</p>
                    </div>
                </div>
            <% } %>

        </div>
        <jsp:include page="/jsp/templates/bootstrap.jsp" />
    </body>
</html>
