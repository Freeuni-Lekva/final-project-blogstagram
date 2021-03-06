<%@ page import="org.blogstagram.models.User" %>
<%@ page import="java.util.List" %>
<%@ page import="org.blogstagram.followSystem.api.StatusCodes" %>
<%@ page import="org.blogstagram.models.Blog" %><%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 16/07/2021
  Time: 18:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = (User)request.getAttribute("User");
    List<Blog> blogs = (List<Blog>)request.getAttribute("Blogs");
    Integer status = (Integer)request.getAttribute("FollowStatus");
    boolean canBlogsBeShown = (user.getPrivacy().equals(User.PUBLIC) || (status != null && status.equals(StatusCodes.followed)) || (status != null && status == -1));
%>
<div class="container my-5">
    <div class="container border" style="width:80%;">

        <% if(canBlogsBeShown) {%>
            <div class="container-fluid d-flex justify-content-around flex-wrap">
                <% for(Blog blog: blogs) { %>
                    <a href="/blogstagram/blog/<%= blog.getId() %>">
                        <div class="card bg-light blog-card m-3" style="height:100px;">
                            <div class="card-body text-center">
                                <p class="card-text">
                                    <span class="text-info font-weight-bold"><%= blog.getTitle()%></span><br/>
                                    <span class="text-muted"><%= blog.getContent().substring(0,blog.getContent().length()/4) %>...</span><br/>
                                </p>
                            </div>
                        </div>
                    </a>
                <% } %>

                <% if(blogs.size() == 0) { %>
                    <div class="container text-center">
                        <div class="alert alert-info my-5">
                            This user has no blogs. Encourage him to post one!
                        </div>
                    </div>
                <% } %>
            </div>
        <% } else { %>
            <div class="container text-center">
                <div class="alert alert-dark my-5">
                    This account is private.
                    Follow to see <%= user.getGender().equals(User.MALE) ? ("his") : ("her")%> blogs
                </div>
            </div>
        <% } %>
    </div>
</div>