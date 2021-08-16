<%@ page import="org.blogstagram.models.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%
    User user = (User)request.getAttribute("User");
    String currentUserNickname = (String)request.getSession().getAttribute("currentUserNickname");
    boolean isUserLoggedIn = (currentUserNickname != null);
%>
<html>
    <head>
        <title>Blogstagram</title>

        <jsp:include page="/jsp/templates/bootstrap.jsp" />
        <!-- Link for icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <style>
            .user-profile-picture{
                width: 150px;
                height: 150px;
                border-radius:50%;
                border: 2px solid lightgray;
                padding: 5px;
                background-color: whitesmoke;
            }
            .modal-link:hover{
                text-decoration: none;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/jsp/templates/nav.jsp" />

        <div class="container my-5">
            <div class="container border border-dark mt-2" style="width:80%;">
                <div class="row m-2">
                    <div class="col-2">
                        <img src="/blogstagram/<%= user.getImage() %>" class="user-profile-picture mr-5"/>
                    </div>
                    <div class="col-10">
                        <div class="d-flex ml-5 mt-4">
                            <h4 class="mr-2"><%= user.getFirstname() %> <%= user.getLastname() %> (<%= user.getNickname()%>)</h4>
                            <!-- Follow Button Template -->
                            <jsp:include page="/jsp/user/templates/followButtonTemplate.jsp" />

                            <!-- Gear Icon Template (Including Modal) -->
                            <jsp:include page="/jsp/user/templates/gearIconTemplate.jsp" />

                        </div>

                        <!-- User General Info Template -->
                        <jsp:include page="/jsp/user/templates/userGeneralInfoTemplate.jsp" />


                    </div>
                </div>
            </div>
            <div class="container border-dark border-left border-bottom border-right" style="width:80%;">
                <div class="row">
                    <div class="col-4 border text-center p-2">
                        <span class="font-weight-bold mr-1"><%= request.getAttribute("BlogsCount") %></span>blogs
                    </div>
                    <div class="col-4 border text-center p-2">
                        <span class="font-weight-bold mr-1" id="followers-count"><%= request.getAttribute("FollowersCount") %></span>
                        <a href="/blogstagram/user/<%= user.getNickname() %>/followers" class="text-dark">followers</a>
                    </div>
                    <div class="col-4 border text-center p-2">
                        <span class="font-weight-bold mr-1" id="following-count"><%= request.getAttribute("FollowingCount") %></span>
                        <a href="/blogstagram/user/<%= user.getNickname() %>/following" class="text-dark">following</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bio Template -->
        <jsp:include page="/jsp/user/templates/userBioTemplate.jsp" />
        <jsp:include page="/jsp/user/templates/blogsTemplate.jsp" />

        <jsp:include page="/jsp/templates/footer.jsp" />
    </body>
</html>
