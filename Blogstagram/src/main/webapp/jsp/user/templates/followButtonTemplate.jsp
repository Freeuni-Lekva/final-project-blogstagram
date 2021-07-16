<%@ page import="org.blogstagram.models.User" %>
<%@ page import="org.blogstagram.followSystem.api.StatusCodes" %><%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 13/07/2021
  Time: 15:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = (User)request.getAttribute("User");
    String currentUserNickname = (String)request.getSession().getAttribute("currentUserNickname");

    Integer followStatus = (Integer)request.getAttribute("FollowStatus");

    boolean isCurrentUser = currentUserNickname != null && (currentUserNickname.equals(user.getNickname()));
    boolean isUserLoggedIn = (currentUserNickname != null);

%>

<% if(!isCurrentUser && isUserLoggedIn) { %>

    <form id="follow-form" class="m-0 p-0" toID="<%= user.getId() %>">
        <% if(followStatus.equals(StatusCodes.notFollowed)) { %>
                <button id="follow-form-submission-button" type="submit" class="btn btn-primary" >Follow</button>
        <% } else if(followStatus.equals(StatusCodes.requestSent)) { %>
                <button id="follow-form-submission-button" type="submit" class="btn btn-outline-dark" >Requested</button>
        <% } else { %>
                <button id="follow-form-submission-button" type="submit" class="btn btn-outline-dark" >Following</button>
        <% } %>
    </form>
<% } %>

<script>
    $(document).ready(function(){

        const followButton = document.getElementById("follow-form-submission-button");
        const followersTag = document.getElementById("followers-count");
        $("#follow-form").submit(function(e){
            e.preventDefault();
            $.post("/current/user/id").then(rawCurrentUserResponse => {
                const currentUserID = JSON.parse(rawCurrentUserResponse);
                if(currentUserID == null)
                    return;

                const to_id = String(document.getElementById("follow-form").getAttribute("toID"));
                $.post("/followSystem",
                    {to_id}
                ).then(rawFollowResponse => {
                    const followResponse = JSON.parse(rawFollowResponse);
                    console.log(followResponse);

                    switch(followResponse.status[0]){
                        case 1:
                            followButton.className="btn btn-outline-dark";
                            followButton.innerText = "Following";
                            followersTag.innerText = (Number(followersTag.innerText)+1);
                            break;
                        case 2:
                            followButton.className="btn btn-primary";
                            followButton.innerText = "Follow"
                            followersTag.innerText = (Number(followersTag.innerText)-1);
                            break;
                        default:
                            console.log("Unknown status");
                    }

                })
            })
        })
    })
</script>
