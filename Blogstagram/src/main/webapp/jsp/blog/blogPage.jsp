<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "org.blogstagram.models.Blog" %>

<%
   Blog blog = (Blog) request.getAttribute("blog");
%>

<html>
    <head
        <jsp:include page="/jsp/templates/bootstrap.jsp" />
        <title>Blogstagram</title>
    </head>

    <body>

        <jsp:include page="/jsp/templates/nav.jsp" />

        <div class = "container">
            <div class = "container mt-2 text-center">
                <textarea id = "title" class = "form-control" readonly><%= blog.getTitle() %></textarea>
            </div>
            <div class = "container mt-2">
                <textarea id = "content" class = "form-control"></textarea>
            </div>

        </div>

        <jsp:include page="/jsp/templates/footer.jsp" />
    </body>



</html>