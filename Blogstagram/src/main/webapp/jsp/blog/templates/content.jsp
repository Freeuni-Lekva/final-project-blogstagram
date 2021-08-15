<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "org.blogstagram.models.Blog" %>
<%@ page import = "org.blogstagram.models.User" %>

<%
    Blog blog = (Blog) request.getAttribute("blog");
%>

<div class = "container mt-2">
    <% if(blog != null) { %>
        <textarea id = "content" class = "form-control" rows= "10" minlength="10" maxlength="1000" placeholder="content" oninput="addHashTag()" readonly><%= blog.getContent() %></textarea>
    <% } else { %>
        <textarea id = "content" class = "form-control" rows= "10" minlength="10" maxlength="1000" onchange ="addHashTag()" placeholder="content"></textarea>
    <% } %>
    <div id = "err-content" class = "container text-danger">
    </div>
</div>
