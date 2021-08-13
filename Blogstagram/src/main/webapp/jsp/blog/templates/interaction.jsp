<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "org.blogstagram.models.Blog" %>
<%@ page import = "org.blogstagram.models.User" %>
<%@ page import = "org.blogstagram.models.Comment" %>
<%@ page import = "org.blogstagram.dao.UserDAO" %>
<%@ page import = "org.blogstagram.dao.BlogLikeDao" %>
<%@ page import = "java.util.List" %>

<%!
    Boolean isLikedBy(List <User> likers, Integer currentUserId){
        if(currentUserId == null) return false;
        for(User u : likers){
            if(u.getId() == currentUserId) return true;
        }
        return false;
    }
%>

<%
   Blog blog = (Blog) request.getAttribute("blog");
   Integer currentUserId = (Integer)request.getSession().getAttribute("currentUserID");
   UserDAO userdao = (UserDAO) request.getSession().getAttribute("UserDAO");
   User currentUser = null;
   if(currentUserId != null)
        currentUser = userdao.getUserByID(currentUserId);
   List <User> likers = blog.getLikes();
   BlogLikeDao likedao = (BlogLikeDao) request.getSession().getAttribute("BlogLikeDao");
   Boolean contains = isLikedBy(likers, currentUserId);
%>

<% if(currentUser != null) { %>
    <div class = "btn-group btn-group-lg">
        <div id = "like-button-container" class = "container">
        <% if(currentUser != null && !contains) { %>
           <button type = "button" class = "btn btn-info" onclick = "likeBlog()">like</button>
        <% } else { %>
            <button type = "button" class = "btn btn-info" onclick = "unlikeBlog()">unlike</button>
        <% } %>
        </div>
        <div class = "container">
            <button class = "btn btn-info" onclick = "addComment()">addComment</button>
        </div>
    </div>
    <div class = "container mt-2">

        <div>
            <textarea id ="comment-text" class = "form-control"></textarea>
        </div>
    </div>
    <div id = "err-addComment" class = "text-danger alert">
    </div>


<script>

    function addNewComment(commentInfo){
        comment = JSON.parse(commentInfo);
        return `
            <div id = "comment-${comment['comment_id']}" class = "row media border p-3">
                           <img src="/blogstagram/<%= currentUser.getImage() %>" alt="<%=currentUser.getFirstname() %> <%= currentUser.getLastname() %>" class="mr-3 mt-3 rounded-circle" style="width:60px;">
                           <div class="media-body col">
                               <h4><%=currentUser.getFirstname() %> <%= currentUser.getLastname() %> <small><i> Posted on ${comment['comment_creation_date']}</i></small></h4>
                               <p> ${comment['comment']}</p
                               <p>Likes: <p id = "likes-count-${comment['comment_id']}"><%= 0 %></p></p>
                            </div>
                            <div id = "like-container-${comment['comment_id']}" class = "container col">
                                <button type = "button" class = "btn btn-info" onclick = "likeComment(${comment['comment_id']})">like</button>
                            </div>
                            <div class = "container col">
                                <button type = "button" class = "btn btn-info" onclick = "deleteComment(${comment['comment_id']}, this)">remove</button>
                            </div>
                        </div>

        `

    }


    function getUnlikeButton(){
        return '<button type = "button" class = "btn btn-info" onclick = "unlikeBlog()">unlike</button>';
    }

    function getLikeButton(){
        return '<button type = "button" class = "btn btn-info" onclick = "likeBlog()">like</button>';
    }

    function changeToUnlike(){
        let buttonContainer = document.getElementById("like-button-container");
        buttonContainer.innerHTML = "";
        buttonContainer.innerHTML += getUnlikeButton();
    }

    function changeTolike(){
        let buttonContainer = document.getElementById("like-button-container");
        buttonContainer.innerHTML = "";
        buttonContainer.innerHTML += getLikeButton();
    }

    function likeBlog(){
        console.log("here");
        $.post("/blogstagram/blogLike", {
            blog_id: "<%= blog.getId() %>",
            Like : "Like"
        }).then(response => {
            let fields = JSON.parse(response);
            if(fields.length === 0){
                let likes = document.getElementById("count");
                likes.innerText = parseInt(likes.innerText) + 1;
                changeToUnlike();
            } else {
                for(let k = 0; k < fields.length; k++){
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

   function unlikeBlog(){
        $.post("/blogstagram/blogLike", {
            blog_id: "<%= blog.getId() %>",
            Like : "Unlike"
        }).then(response => {
           let fields = JSON.parse(response);
           if(fields.length === 0){
              let likes = document.getElementById("count");
              likes.innerText = parseInt(likes.innerText) - 1;
              changeTolike();
           } else {
               let {errorMessage, variableName} = fields[k];
               let errms = document.getElementById("err-comment");
               errms.innerHTML = "";
               errms.innerHTML += errorMessage;
           }
        }).catch(errs => {
            console.log(errs);
        });
   }

    function addComment(){
        let text = document.getElementById("comment-text").value;
        if(text === "" || text === " ") return;
        $.post("/blogstagram/commentServlet", {
            CommentAction : "AddComment",
            blog_id : "<%= blog.getId() %>",
            Comment : text

        }).then(response => {
            console.log(response);
            let fields = JSON.parse(response);
            let err = fields["errors"];
            let commentInfo = fields["comment"];
            console.log(fields["errors"]);
            let comments = document.getElementById("comment_container");
            console.log(err.length);
            if(err === `[]`){
                comments.innerHTML += addNewComment(commentInfo);
                document.getElementById("comment-text").value = "";
            }else {
                let errors = fields["errors"];
                let errorsJson = JSON.parse(errors);
                for(let k = 0; k < errorsJson.length; k++){
                    let error = errorsJson[k];
                    let {variableName, errorMessage} = error;
                    let errContainer = document.getElementById(`err-${variableName}`);
                    errContainer.innerText += errorMessage + "\n";
                }
            }
        }).catch(err => {
            console.log(err);
        });
    }
</script>
<% } %>
