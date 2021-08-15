<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "org.blogstagram.models.Blog" %>
<%@ page import = "java.util.List" %>
<%
    Blog blog = (Blog) request.getAttribute("blog");
%>


<div class = "container mt-2 text-center">
    <% if(blog != null) { %>
        <textarea id = "title" class = "form-control" maxlength="100" minlength="10" placeholder="title" readonly><%= blog.getTitle() %></textarea>
    <% } else { %>
        <textarea id = "title" maxlength="100" minlength="10" class = "form-control" placeholder="title"></textarea>
    <% } %>
    <div id="err-title" class = "container text-danger">
    </div>
</div>
