<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "org.blogstagram.models.Blog" %>
<%@ page import = "org.blogstagram.models.User" %>
<%@ page import = "org.blogstagram.models.Comment" %>
<%@ page import = "org.blogstagram.dao.UserDAO" %>
<%@ page import = "java.util.List" %>
<%@ page import = "org.blogstagram.validators.CommentLikeValidator" %>


<%
    Integer currentUserId = (Integer)request.getSession().getAttribute("currentUserID");
    Blog blog = (Blog) request.getAttribute("blog");
    CommentDAO commentDAO = (CommentDAO) request.getSession().getAttribute("CommentDAO");
    Comment comment = (Comment) request.getAttribute("comment");
    List<User> commentLikeUsers = commentDAO.getCommentLikeUsers(comment.getComment_id());
    String commentString = comment.getComment();
    int comment_author_id = comment.getUser_id();
    Connection connection = (Connection) context.getAttribute("dbConnection");
    CommentLikeValidator val = new CommentLikeValidator();
    val.setConnection(connection);
    boolean userHasLikedComment = val.validate(comment_id, currentUserID);
%>

<html>
    <title>
      <h1>Blogstagram</h1>
    </title>

    <body>

    <h3>Comment</h3>

    <div class = "comment">
        <h5 id="curCom"><%commentString%></h5>
    </div>

    <% if(currentUserId == comment_author_id){ %>
        <div class="modal-body">
          <button type="button" class="btn btn-danger" onclick="changeEditButton()" id="commentEditButton">Edit Comment</button>
          <button type="button" style="display: none;" class="btn btn-danger" onclick="changeEditButton()" id="commentEditCloseButton">Done</button>
	    </div>

        <div class="modal-body">
            <button type="button" class="btn btn-danger" onclick="deleteComment()" id="commentDeleteButton">Delete Comment</button>
        </div>

    <% } %>

    <% if(userHasLikedComment){ %>
        <div class="modal-body">
            <button type="button" class="btn btn-danger" onclick="deleteLikeComment()" id="LikeButton">Like Comment</button>
        </div>
    <% }else {%>
        <div class="modal-body">
            <button type="button" class="btn btn-danger" onclick="deleteUnlikeComment()" id="UnlikeButton">Unlike Comment</button>
        </div>
    <% } %>

    <script>

        function changeEditButton() {
            var myobj = document.getElementById("commentEditButton");
            myobj.innerHTML = "Save Comment Changes";
            myobj.setAttribute("id","commentSaveChanges");
        }

        function changeSaveButton() {
            var myobj = document.getElementById("commentSaveChanges");
            myobj.innerHTML = "Edit Comment";
            myobj.setAttribute("id","commentEditButton");

            <text
        }

        function deleteLikeComment() {
            var myobj = document.getElementById("LikeButton");
            myobj.innerHTML = "Unlike Comment";
            myobj.setAttribute("id","UnlikeButton");
        }

        function deleteUnlikeComment() {
            var myobj = document.getElementById("UnlikeButton");
            myobj.innerHTML = "Like Comment";
            myobj.setAttribute("id","deleteMakeUser");
        }

        $("#commentDeleteButton").click(function(e){
            const comment_id = <%comment_id.getComment_id()%>;
            const CommentAction = "DeleteComment";
            $.post("/commentServlet",{comment_id, CommentAction}).then(response => {
                let responseJSON = JSON.parse(response);
                location.reload();
            })
        })

        $("#commentSaveChanges").click(function(e){
            const comment_id = <%comment_id.getComment_id()%>;
            const CommentAction = "DeleteComment";
            $.post("/commentServlet",{comment_id, CommentAction}).then(response => {
                let responseJSON = JSON.parse(response);
                location.reload();
            })
            var myobj = document.getElementById("comment");
            myobj.innerHTML = ;
            changeSaveButton()
        })

        /* esaa buttonebis washale es komentari */

        const paragraph = document.getElementById("curCom");
        const edit_button = document.getElementById("commentEditButton");
        const end_button = document.getElementById("commentEditCloseButton");

        edit_button.addEventListener("click", function() {
          paragraph.contentEditable = true;
          paragraph.style.backgroundColor = "#dddbdb";
          myFunction();
        } );

        end_button.addEventListener("click", function() {
          paragraph.contentEditable = false;
          paragraph.style.backgroundColor = "#ffe44d";
          myFunction();
        } )

        function myFunction() {
            var x = document.getElementById("commentEditCloseButton");
            if (x.style.display === "none") {
              x.style.display = "block";
            } else {
              x.style.display = "none";
            }
          }

    </script>

    </body>



</html>

