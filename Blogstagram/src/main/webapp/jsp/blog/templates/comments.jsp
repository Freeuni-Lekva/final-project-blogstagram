<%@ page import = "org.blogstagram.models.Blog" %>
<%@ page import = "org.blogstagram.models.User" %>
<%@ page import = "org.blogstagram.models.Comment" %>
<%@ page import = "org.blogstagram.dao.UserDAO" %>
<%@ page import = "org.blogstagram.dao.CommentDAO" %>
<%@ page import = "java.util.List" %>
<%
    Blog blog = (Blog) request.getAttribute("blog");
    List <Comment> comments = blog.getComments();
    Integer currentUserId = (Integer)request.getSession().getAttribute("currentUserID");
    UserDAO userdao = (UserDAO) request.getSession().getAttribute("UserDAO");
    CommentDAO commentDao = (CommentDAO) request.getSession().getAttribute("CommentDAO");
    User user = null;
    if(currentUserId != null){
        user = userdao.getUserByID(currentUserId);
    }
%>








<div class = "container mt-2">
<span class = "text-center bg bg-info text-white form-control">Comments</span>
<div id = "comment_container" class = "jumbotron">
    <% for(Comment current : comments) { %>
        <% User creator = userdao.getUserByID(current.getUser_id());
        %>
        <% if(user != null && (current.getUser_id() == user.getId() || user.getRole() == User.MODERATOR_ROLE || user.getRole() == User.ADMIN_ROLE)){ %>
            <div id = "comment-<%= current.getComment_id()%>" class = "row media border p-3">
               <img src="/blogstagram/<%= creator.getImage() %>" alt="<%=creator.getFirstname() %> <%= creator.getLastname() %>" class="mr-3 mt-3 rounded-circle" style="width:60px;">
               <div class="media-body col">
                   <h4><%=creator.getFirstname() %> <%= creator.getLastname() %> <small><i> Posted on <%= current.getCommentDate() %></i></small></h4>
                   <p> <%= current.getComment() %></p>
                   <p>Likes: <p id = "likes-count-<%= current.getComment_id() %>"><%= commentDao.getNumberOfLikes(current.getComment_id()) %></p></p>
                </div>
                <div id = "like-container-<%= current.getComment_id() %>" class = "container col">
                  <% if(!commentDao.isLikedByUser(current.getComment_id(), currentUserId)) { %>
                     <button type = "button" class = "btn btn-info" onclick = "likeComment(<%= current.getComment_id()%>)">like</button>
                  <% } else { %>
                    <button type = "button" class = "btn btn-info" onclick = "unlikeComment(<%= current.getComment_id()%>)">unlike</button>
                  <% } %>
                </div>
                <div class = "container col">
                    <button type = "button" class = "btn btn-info" onclick = "deleteComment(<%= current.getComment_id()%>, this)">remove</button>
                </div>
            </div>
        <% } else if (user != null) { %>
            <div id = "comment-<%= current.getComment_id() %>" class = "media border p-3">
                <img src="/blogstagram/<%= creator.getImage() %>" alt="<%=creator.getFirstname() %> <%= creator.getLastname() %>" class="mr-3 mt-3 rounded-circle" style="width:60px;">
                <div class="media-body">
                    <h4><%=creator.getFirstname() %> <%= creator.getLastname() %><small><i>Posted on <%= current.getCommentDate() %></i></small></h4>
                    <p><%= current.getComment() %></p>
                   <p>Likes: <p id = "likes-count-<%= current.getComment_id() %>"><%= commentDao.getNumberOfLikes(current.getComment_id()) %></p></p>
                </div>
                <div id = "like-container-<%= current.getComment_id() %>" class = "container">
                   <% if(!commentDao.isLikedByUser(current.getComment_id(), currentUserId)) { %>
                        <button type = "button" class = "btn btn-info" onclick = "likeComment(<%= current.getComment_id()%>)">like</button>
                   <% } else { %>
                        <button type = "button" class = "btn btn-info" onclick = "unlikeComment(<%= current.getComment_id()%>)">unlike</button>
                   <% } %>
                </div>
            </div>
        <% } else { %>
            <div id = "comment-<%= current.getComment_id() %>" class = "media border p-3">
               <img src="/blogstagram/<%= creator.getImage() %>" alt="<%=creator.getFirstname() %> <%= creator.getLastname() %>" class="mr-3 mt-3 rounded-circle" style="width:60px;">
               <div class="media-body">
                   <h4><%=creator.getFirstname() %> <%= creator.getLastname() %><small><i>Posted on <%= current.getCommentDate() %></i></small></h4>
                   <p><%= current.getComment() %></p>
                   <p>Likes: <p id = "likes-count-<%= current.getComment_id() %>"><%= commentDao.getNumberOfLikes(current.getComment_id()) %></p></p>
               </div>
            </div>

        <% } %>
    <% } %>
</div>
<span class = "text-center bg-info text-white form-control"><h3 id = "likers-lst" >Likes: <span id = "count"><%= blog.getNumLikes() %></span></h3> <a href = "/blogstagram/jsp/likes/blogLike.jsp">See all</a></span>
<div id = "err-comment" class = "text-alert alert">
</div>
</div>
<script>

    function unlikeButton(commentId){
        return `
            <button type = "button" class = "btn btn-info" onclick = "unlikeComment(${commentId})">unlike</button>
        `
    }

    function changeUnlike(commentId){
        let button = document.getElementById(`like-container-${commentId}`);
        button.innerHTML = "";
        button.innerHTML += unlikeButton(commentId);
    }

    function likeComment(commentId){
        $.post("/blogstagram/commentLike", {
            comment_id: String(commentId),
            Like: "Like"
        }).then(response => {
            let fields = JSON.parse(response);
            if(fields.length === 0){
                let tag = document.getElementById(`likes-count-${commentId}`);
                tag.innerText = parseInt(tag.innerText) + 1;
                changeUnlike(commentId);
            } else {
                for(let k = 0; k < fields.length; k++) {
                    console.log(fields[k]);
                    let {errorMessage, variableName} = fields[k];
                    let errms = document.getElementById("err-comment");
                    errms.innerHTML = "";
                    errms.innerHTML += errorMessage;
                }
            }
        }).catch(errs => {
            console.log(errs);
        });
    }

    function likeButton(commentId){
        return `<button type = "button" class = "btn btn-info" onclick = "likeComment(${commentId})">like</button>`;
    }

    function changeLike(commentId){
        let button = document.getElementById(`like-container-${commentId}`);
        button.innerHTML = "";
        button.innerHTML += likeButton(commentId);
    }

    function unlikeComment(commentId){
        $.post("/blogstagram/commentLike", {
            comment_id: String(commentId),
            Like : "Unlike"
        }).then(response => {
            console.log(response);
            let fields = JSON.parse(response);
            if(fields.length === 0){
               let tag = document.getElementById(`likes-count-${commentId}`);
               tag.innerText = parseInt(tag.innerText) - 1;
               changeLike(commentId);
            } else {
                for(let k = 0; k < fields.length; k++) {
                    let {errorMessage, variableName} = fields[k];
                    let errms = document.getElementById("err-comment");
                    errms.innerHTML = "";
                    errms.innerHTML += errorMessage;
                }
            }
        }).catch(errs => {
            console.log(errs);
        })
    }

    function deleteComment(commentId, tag){
        $.post("/blogstagram/commentServlet", {
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
