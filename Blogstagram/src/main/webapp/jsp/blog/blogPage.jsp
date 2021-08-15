<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "org.blogstagram.models.Blog" %>
<%@ page import = "org.blogstagram.models.User" %>
<%@ page import = "org.blogstagram.models.Comment" %>
<%@ page import = "org.blogstagram.dao.UserDAO" %>
<%@ page import = "java.util.List" %>

<%!
   Boolean canBeEdited(Integer currentUserId, Blog blog){
        if(currentUserId == null || blog == null) return false;
        Integer creatorId = blog.getUser_id();
        List <User> moderators = blog.getBlogModerators();
        if(creatorId == currentUserId) return true;
        for(Integer k = 0; k < moderators.size(); k++){
            if(moderators.get(k).getId() == currentUserId) return true;
        }
        return false;
    }
%>
<%
   Blog blog = (Blog) request.getAttribute("blog");
   Integer currentUserId = (Integer)request.getSession().getAttribute("currentUserID");
   Boolean canEdit = canBeEdited(currentUserId, blog);
   UserDAO userdao = (UserDAO) request.getSession().getAttribute("UserDAO");
   User currentUser = null;
   if(currentUserId != null)
        currentUser = userdao.getUserByID(currentUserId);
%>

<html>
    <head>
        <jsp:include page="/jsp/templates/bootstrap.jsp" />
        <title>Blogstagram</title>
        <script src = "/blogstagram/jsp/blog/javascript/buttons.js"></script>
        <script src = "/blogstagram/jsp/blog/javascript/search.js"></script>
        <script src = "/blogstagram/jsp/blog/javascript/removeModerators.js"></script>
    </head>

    <body>
        <% if(currentUserId == null && blog == null) response.sendError(response.SC_UNAUTHORIZED); %>
        <jsp:include page="/jsp/templates/nav.jsp" />
        <div id = "blog_container" class = "container row text-center" style = "margin: 0 auto;">
            <div class = "container mt-2 col-sm-3">
              <jsp:include page = "/jsp/blog/templates/hashtags.jsp"/>
            </div>
            <div class = "container mt-2 col-md-6">
                <jsp:include page = "/jsp/blog/templates/title.jsp"/>
                <jsp:include page = "/jsp/blog/templates/content.jsp"/>
            </div>
            <div id = "moderators_container" class = "container mt-2 col-sm-3 text-center">
                <jsp:include page = "/jsp/blog/templates/moderators.jsp"/>
                <% if(blog == null) { %>
                    <script>addModeratorButtons();</script>
                <% } %>
            </div>
            <jsp:include page = "/jsp/blog/templates/search.jsp"/>
            <jsp:include page = "/jsp/blog/templates/removeModerators.jsp" />
            <div id = "button_container" class = "container mt-2">
                <% if(canEdit) {%>
                    <div class = "container mt-2 cols-sm-1">
                        <button id="Edit" class ="btn btn-info form-control" onclick="changeToEdit()">Edit</button>
                    </div>
                <% } else if(blog == null) { %>
                    <div class = "container mt-2 cols-sm-1">
                        <button id="Add" class ="btn btn-info form-control" onclick="addBlog()">Add Blog</button>
                    </div>
                <% } %>

                <% if(blog != null && currentUser != null && (currentUser.getId() == blog.getUser_id() || currentUser.getRole() == User.MODERATOR_ROLE || currentUser.getRole() == User.ADMIN_ROLE)) { %>
                    <div id="remove_container" class = "container mt-2">
                        <button id = "remove" class = "btn btn-info form-control" onclick='removeBlog(<%= blog.getId() %>, "<%= currentUser.getNickname() %>")'>removeBlog</button>
                    </div>
                <% } %>
            </div>

        </div>
           <%if(blog != null) { %>
                 <div class = "container">
                    <jsp:include page = "/jsp/blog/templates/comments.jsp" />
                    <jsp:include page = "/jsp/blog/templates/interaction.jsp" />
                </div>
           <% } %>
        </br>
            <jsp:include page="/jsp/templates/footer.jsp" />
    </body>



</html>
