<%@ page import="org.blogstagram.models.User" %><%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 13/07/2021
  Time: 15:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = (User)request.getAttribute("User");
    String currentUserNickname = (String)request.getSession().getAttribute("currentUserNickname");

    boolean isCurrentUser = currentUserNickname != null && (currentUserNickname.equals(user.getNickname()));
    boolean isUserLoggedIn = (currentUserNickname != null);

%>

<% if(!isCurrentUser && isUserLoggedIn) { %>
    <button type="button" class="btn btn-primary mr-2" style="width:20%;">Follow</button>
    <button type="button" class="btn btn-outline-dark" style="width:20%;">Requested</button>
    <button type="button" class="btn btn-outline-dark" style="width:20%;">Following</button>
<% } %>