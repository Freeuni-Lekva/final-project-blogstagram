<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "org.blogstagram.models.Blog" %>
<%@ page import = "org.blogstagram.models.User" %>
<%@ page import = "org.blogstagram.models.Comment" %>
<%@ page import = "org.blogstagram.dao.UserDAO" %>
<%@ page import = "java.util.List" %>

<%
   Blog blog = (Blog) request.getAttribute("blog");
   Integer currentUserId = (Integer)request.getSession().getAttribute("currentUserID");
   UserDAO userdao = (UserDAO) request.getSession().getAttribute("UserDAO");
   User currentUser = null;
   if(currentUserId != null)
        currentUser = userdao.getUserByID(currentUserId);
%>

<% if(currentUser != null) { %>
    <div class = "btn-group btn-group-lg">
        <div class = "container">
            <button class = "btn btn-info">like</button>
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
                           <img src="<%= currentUser.getImage() %>" alt="<%=currentUser.getFirstname() %> <%= currentUser.getLastname() %>" class="mr-3 mt-3 rounded-circle" style="width:60px;">
                           <div class="media-body col">
                               <h4><%=currentUser.getFirstname() %> <%= currentUser.getLastname() %> <small><i> Posted on ${comment['comment_creation_date']}</i></small></h4>
                               <p> ${comment['comment']}</p>
                            </div>
                            <div class = "container col">
                              <button type = "button" class = "btn btn-info" onclick = "like()">like</button>
                            </div>
                            <div class = "container col">
                                <button type = "button" class = "btn btn-info" onclick = "deleteComment(${comment['comment_id']}, this)">remove</button>
                            </div>
                        </div>

        `

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
