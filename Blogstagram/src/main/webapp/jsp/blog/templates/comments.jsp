<%@ page import = "org.blogstagram.models.Blog" %>
<%@ page import = "org.blogstagram.models.User" %>
<%@ page import = "org.blogstagram.models.Comment" %>
<%@ page import = "org.blogstagram.dao.UserDAO" %>
<%@ page import = "java.util.List" %>
<%
    Blog blog = (Blog) request.getAttribute("blog");
    List <Comment> comments = blog.getComments();
    Integer currentUserId = (Integer)request.getSession().getAttribute("currentUserID");
    UserDAO userdao = (UserDAO) request.getSession().getAttribute("UserDAO");
    User user = null;
    if(currentUserId != null){
        user = userdao.getUserByID(currentUserId);
    }
%>


<div class = "container mt-2">
<span class = "text-center bg bg-info text-white form-control">Comments</span>
<div id = "comment_container" class = "jumbotron">
    <% for(Comment current : comments) { %>
        <% User creator = userdao.getUserByID(current.getUser_id()); %>
        <% if(user != null && (current.getUser_id() == user.getId() || user.getRole() == User.MODERATOR_ROLE || user.getRole() == User.ADMIN_ROLE)){ %>
            <div id = "comment-<%= current.getComment_id()%>" class = "row media border p-3">
               <img src="<%= creator.getImage() %>" alt="<%=creator.getFirstname() %> <%= creator.getLastname() %>" class="mr-3 mt-3 rounded-circle" style="width:60px;">
               <div class="media-body col">
                   <h4><%=creator.getFirstname() %> <%= creator.getLastname() %> <small><i> Posted on <%= current.getCommentDate() %></i></small></h4>
                   <p> <%= current.getComment() %></p>
                </div>
                <div class = "container col">
                  <button type = "button" class = "btn btn-info" onclick = "like()">like</button>
                </div>
                <div class = "container col">
                    <button type = "button" class = "btn btn-info" onclick = "deleteComment(<%= current.getComment_id()%>, this)">remove</button>
                </div>
            </div>
        <% } else if (user != null) { %>
            <div id = "comment-<%= current.getComment_id() %>" class = "media border p-3">
                <img src="<%= creator.getImage() %>" alt="<%=creator.getFirstname() %> <%= creator.getLastname() %>" class="mr-3 mt-3 rounded-circle" style="width:60px;">
                <div class="media-body">
                    <h4><%=creator.getFirstname() %> <%= creator.getLastname() %><small><i>Posted on <%= current.getCommentDate() %></i></small></h4>
                    <p><%= current.getComment() %></p>
                </div>
                <div class = "container">
                   <button type = "button" class = "btn btn-info" onclick = "like()">like</button>
                </div>
            </div>
        <% } else { %>
            <div id = "comment-<%= current.getComment_id() %>" class = "media border p-3">
               <img src="<%= creator.getImage() %>" alt="<%=creator.getFirstname() %> <%= creator.getLastname() %>" class="mr-3 mt-3 rounded-circle" style="width:60px;">
               <div class="media-body">
                   <h4><%=creator.getFirstname() %> <%= creator.getLastname() %><small><i>Posted on <%= current.getCommentDate() %></i></small></h4>
                   <p><%= current.getComment() %></p>
               </div>
            </div>

        <% } %>
    <% } %>
</div>
<div id = "err-comment" class = "text-alert alert">
</div>
</div>
<script>

    function like(){

    }
    function deleteComment(commentId, tag){
        $.post("/commentServlet", {
            comment_id : commentId,
            CommentAction : "DeleteComment",
            blog_id : "<%= blog.getId() %>"
        }).then(response => {
            let fields = JSON.parse(response);
            if(fields["errors"] === "[]"){
                let child = tag.parentNode.parentNode;
                child.parentNode.removeChild(child);
            } else {
               let errors = fields["errors"];
               let errorsJson = JSON.parse(errors);
               for(let k = 0; k < errorsJson.length; k++){
                    let error = errorsJson[k];
                    let {variableName, errorMessage} = error;
                    let errContainer = document.getElementById(`err-comment`);
                    errContainer.innerText += errorMessage + "\n";
               }
            }
        }).catch(errs => {
            console.log(errs);
        });
    }
</script>