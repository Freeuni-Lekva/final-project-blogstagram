<%@ page import="org.blogstagram.models.Blog" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 06/08/2021
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<Blog> blogs = (List<Blog>)request.getAttribute("Blogs");
%>

<html>
    <head>
        <jsp:include page="/jsp/templates/bootstrap.jsp" />
    </head>
    <body>
        <jsp:include page="/jsp/templates/nav.jsp" />

        <% for(Blog blog: blogs) {%>
            <div class="container m-5 p-5">
                <%= blog.getTitle() %>
            </div>
        <% } %>
        <jsp:include page="/jsp/templates/footer.jsp" />
    </body>
</html>
