<%@ page import="org.blogstagram.models.Blog" %>
<%@ page import="java.util.List" %>
<%@ page import="org.blogstagram.models.UserProvidedBlog" %><%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 06/08/2021
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<UserProvidedBlog> blogs = (List<UserProvidedBlog>)request.getAttribute("Blogs");
    Integer currentUserID = (Integer)request.getSession().getAttribute("currentUserID");
    if(currentUserID == null)
        currentUserID = -1;
%>

<html>
    <head>
        <jsp:include page="/jsp/templates/bootstrap.jsp" />
        <title>HomePage</title>
        <style>
            a:hover{
                text-decoration: none;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/jsp/templates/nav.jsp" />
        <div class="container text-center my-5">
            <div class="alert alert-info">
                <h3 class="p-2">
                    <span class="text-info">Welcome to the Blogstagram!</span><br/>
                    We have a friendly space for everyone, you can build up your blog career.
                </h3>
            </div>
        </div>

        <div class="container">
            <div class="container">
                <% for(UserProvidedBlog blog: blogs) {%>
                    <div class="media my-3 border border-3 border-info p-3">
                        <img class="mr-3" style="width:100px; height:100px; border-radius: 50%; object-fit: cover;" src="/blogstagram/<%= blog.getUserImage() %>" alt="Generic placeholder image">
                        <div class="media-body">
                            <h5 class="mt-0">
                                <%= blog.getTitle() %>
                                <span class="text-muted">
                                        (
                                        <span class="text-info">Author:</span>

                                        <a href="/blogstagram/user/<%= blog.getUser_id()%>">
                                            <% if(blog.getUser_id() == currentUserID) { %>
                                                Me
                                            <% } else { %>
                                                <%= blog.getUserFirstname()%> <%= blog.getUserLastname()%>
                                            <% } %>
                                        </a>
                                        )
                                </span>
                                <a class="btn btn-outline-primary" href="/blogstagram/blog/<%= blog.getId() %>">Checkout the blog!</a>


                                    <span style="font-size:15px;">
                                        <span class="text-info">Blogging Date: </span>
                                        <span class="text-muted"> <%= blog.getCreated_at() %> </span>
                                    </span>

                            </h5>
                            <% String content = blog.getContent(); %>
                            <%= content.substring(0,content.length()/4) %>...
                        </div>
                    </div>
                <% } %>

                <% if(blogs.size() == 0) { %>
                <div class="alert alert-warning">
                    <h5 class="mt-0 text-center">No Blogs At All :( </h5>
                </div>
                <% } %>
            </div>
        </div>


        <div class="container text-center my-5">
            <div class="alert alert-info">
                <h3 class="p-2">
                    That's it for now...
                </h3>
            </div>
        </div>

        <jsp:include page="/jsp/templates/footer.jsp" />
    </body>
</html>
