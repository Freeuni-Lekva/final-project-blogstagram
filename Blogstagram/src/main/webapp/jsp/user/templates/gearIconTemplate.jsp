<%@ page import="org.blogstagram.models.User" %>
<%@ page import="org.blogstagram.dao.AdminDAO" %>
<%@ page import="org.blogstagram.validators.AdminValidator" %>

<%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 13/07/2021
  Time: 15:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%
    User user = (User)request.getAttribute("User");
    String currentUserNickname = (String)request.getSession().getAttribute("currentUserNickname");
    boolean isCurrentUser = currentUserNickname != null && (currentUserNickname.equals(user.getNickname()));
    boolean isUserLoggedIn = (currentUserNickname != null);

    Integer user_id = (Integer)request.getSession().getAttribute("currentUserID");
    AdminDAO adminDAO = (AdminDAO)request.getSession().getAttribute("AdminDAO");
    AdminValidator adminValidator = new AdminValidator();

    boolean isAdmin = false;
    boolean isModerator = false;
    boolean currentUserIsModerator = adminDAO.isModerator(user.getId());

    adminValidator.setAdminDAOUser(adminDAO, user_id, true);
    if(adminValidator.validate()){
        isAdmin = true;
        isModerator = true;
    }else{
        adminValidator.setAdminDAOUser(adminDAO, user_id, false);
        if(adminValidator.validate()){
            isModerator = true;
        }
    }
%>

<% if(isUserLoggedIn) { %>
<button type="button" class="btn" data-toggle="modal" data-target="#settingsModal">
    <i class="fa fa-gear fa-spin text-dark mt-2" style="font-size:20px;"></i>
</button>
<!-- The Modal -->
<div class="modal" id="settingsModal">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title"><%= user.getFirstname() %> <%= user.getLastname() %> (<%= user.getNickname()%>)</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal body -->
            <div class="modal-body">
                <% if(!isCurrentUser){ %>
                <a href="#" class="modal-link">
                    <button type="button" class="btn btn-outline-danger btn-block my-2">Report</button>
                </a>
                <% } else { %>
                <a href="/edit/profile" class="modal-link">
                    <button type="button" class="btn btn-outline-info btn-block my-2">Edit Profile</button>
                </a>
                <a href="/logout" class="modal-link">
                    <button type="button" class="btn btn-outline-secondary btn-block my-2">Logout</button>
                </a>

                <% } %>

                <% if(isModerator){ %>
                    <div class="modal-body">
                        <div id="deleteUserID" style="display:none"><%= user.getId()%></div>
                        <button type="button" class="btn btn-danger" id="userDeleteButton">Delete User</button>
                    </div>
                <% }%>

                <% if(isAdmin && !currentUserIsModerator){ %>
                  <div class="modal-body">
                    <div id="OperationType" style="display:none"> "MakeModer" </div>
                    <div id="deleteUserID" style="display:none"> user_id </div>
                       <button type="button" class="btn btn-danger" id="userChangeRoleButton">Make Moderator</button>
                  </div>
                <% }else if(isAdmin && currentUserIsModerator){ %>
                    <div class="modal-body">
                      <div id="OperationType" style="display:none"> "MakeUser" </div>
                      <div id="deleteUserID" style="display:none"> user_id </div>
                        <button type="button" class="btn btn-danger" id="userChangeRoleButton">Take user privileges</button>
                    </div>
                <% }%>

            </div>

            <!-- Modal footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
            </div>

        </div>
    </div>
</div>
<% } %>

<script>
    $("#userDeleteButton").click(function(e){
        const deleteUserID = document.getElementById("deleteUserID").innerText;
        $.post("/delete/user",{deleteUserID}).then(response => {
            let responseJSON = JSON.parse(response);
            location.reload();
        })
    })

    $("#userChangeRoleButton").click(function(e){
            const user_id = document.getElementById("user_id").innerText;
            $.post("/changeRole/user",{user_id}).then(response => {
                let responseJSON = JSON.parse(response);
                location.reload();
            })
        })


</script>

