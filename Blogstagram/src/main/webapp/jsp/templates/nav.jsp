<%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 25/06/2021
  Time: 23:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String currentUserNickname = (String)request.getSession().getAttribute("currentUserNickname");
%>
<nav class="navbar navbar-expand-sm navbar-dark bg-dark">
    <a class="navbar-brand" href="/">Blogstagram</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar" aria-controls="collapsibleNavbar" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="collapsibleNavbar">

        <ul class="navbar-nav ml-auto">
            <li class="nav-item active">
                <a class="nav-link" href="/">Home</a>
            </li>

            <jsp:include page="searchModal.jsp" />

            <% if(currentUserNickname == null) { %>
                <li class="nav-item">
                    <a class="nav-link" href="/login">Login</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/register">Register</a>
                </li>
            <% } else { %>
                <li class="nav-item">
                    <a class="nav-link" href="/notifications">Notifications</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="navbarDropdownMenuLink-4" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <%= currentUserNickname %> </a>
                    <div class="dropdown-menu dropdown-menu-right dropdown-info" aria-labelledby="navbarDropdownMenuLink-4">
                        <a class="dropdown-item" href="/user/<%=currentUserNickname%>">My account</a>
                        <form action="/logout" method="POST">
                            <button type="submit" class="dropdown-item">Logout</button>
                        </form>
                    </div>
                </li>
            <% } %>

        </ul>
    </div>
</nav>