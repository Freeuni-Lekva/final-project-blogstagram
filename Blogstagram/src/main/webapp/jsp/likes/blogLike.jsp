<%@ page import="org.blogstagram.models.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<User> likers = (List<User>)request.getAttribute("likes");
%>

<html>
<head>
    <title>Post Likes</title>
    <jsp:include page="/jsp/templates/bootstrap.jsp" />
    <style>
    </style>
</head>
<body>
<jsp:include page="/jsp/templates/nav.jsp" />

<div class="container">
    <% for(User liker : likers) { %>

    <div class="media border p-3">
        <img src="/blogstagram/<%= liker.getImage() %>" alt="" class="mr-3 mt-3 rounded-circle" style="width:100px;height:100px;">
        <div class="media-body mt-4">
            <h4> <a href="/blogstagram/user/<%= liker.getNickname() %>"> <%= liker.getFirstname() %> <%= liker.getLastname() %> </a> <small></small></h4>
        </div>
    </div>
    <% } %>

</div>
<jsp:include page="/jsp/templates/footer.jsp" />
</body>
</html>