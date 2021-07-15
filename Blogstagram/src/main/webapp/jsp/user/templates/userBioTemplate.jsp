<%@ page import="org.blogstagram.models.User" %><%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 13/07/2021
  Time: 16:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User)request.getAttribute("User");
    String bio = user.getBio();
%>

<% if(bio != null) { %>
    <div class="container mt-2 mb-4">
        <div class="container border border-info"  style="width:80%;">
            <h6 class="text-center mt-1" style="font-size:25px;">Bio</h6>
            <hr class="border-info" />
            <article class="p-2 text-center">
                <%= bio %>
            </article>
        </div>
    </div>
<% } %>
