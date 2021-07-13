<%@ page import="org.blogstagram.models.User" %><%--
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
                <a href="#" class="modal-link">
                    <button type="button" class="btn btn-outline-info btn-block my-2">Edit Profile</button>
                </a>
                <a href="/logout" class="modal-link">
                    <button type="button" class="btn btn-outline-secondary btn-block my-2">Logout</button>
                </a>
                <% } %>

            </div>

            <!-- Modal footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
            </div>

        </div>
    </div>
</div>
<% } %>