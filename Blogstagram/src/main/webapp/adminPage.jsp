<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.blogstagram.models.User" %>
<%@ page import="org.blogstagram.dao.AdminDAO" %>
<%@ page import="org.blogstagram.validators.AdminValidator" %>

<%
    Integer user_id = (Integer)request.getSession().getAttribute("currentUserID");
    boolean isNotLoggedIn = (user_id == null);
    AdminDAO adminDAO;
    AdminValidator adminValidator;
    boolean currentUserIsModerator = false;
    if(!isNotLoggedIn){
        adminDAO = (AdminDAO)request.getSession().getAttribute("AdminDAO");
        adminValidator = new AdminValidator();
        adminValidator.setAdminDAOUser(adminDAO, user_id, false);
        currentUserIsModerator = adminValidator.validate();
    }

%>

<% if(isNotLoggedIn || !currentUserIsModerator) { %>
 <p><a href="login.jsp">Only admins can enter this page!</a></p>
<% }else{ %>
<html>
    <head>

        <title>Admin Page</title>
        <jsp:include page="/jsp/templates/bootstrap.jsp" />
        <jsp:include page="/jsp/templates/nav.jsp" />

     <li class="nav-item">
         <!-- Trigger the modal with a button -->
         <a class="nav-link" data-toggle="modal" data-target="#searchModal">Search Users</a>

         <!-- Modal -->
         <div id="searchModal" class="modal fade" role="dialog">
             <div class="modal-dialog">
                 <!-- Modal content-->
                 <div class="modal-content">
                     <div class="modal-header">
                         <h4 class="modal-title text-center">Search for users</h4>
                         <button type="button" class="close" data-dismiss="modal">&times;</button>
                     </div>
                     <div class="modal-body">
                         <input type="text" id="search-query" class="form-control" placeholder="Search...">
                         <div class="overflow-auto" id="users-modal-field">

                         </div>
                     </div>
                     <div class="modal-footer">
                         <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                     </div>
                 </div>

             </div>
         </div>
     </li>

     <script>
         function createSearchedUserField(user){
             return `
                 <div class="media border py-2 my-2">
                     <img class="ml-2" width="70px" height="70px" style="object-fit:cover; border-radius:50%" src="${/blogstagram/user.image}" alt="Generic placeholder image">
                     <div class="media-body text-center">
                         <h5 class="pt-4">
                             <a href="/blogstagram/user/${user.nickname}">${user.firstname} ${user.lastname} ( ${user.nickname} )</a>
                         </h5>
                   </div>
                 </div>
             `
         }
         $(document).ready(function(){
             let usersField = document.getElementById("users-modal-field");
             let inputField = document.getElementById("search-query");
             inputField.addEventListener("input",(e) => {
                 const query = inputField.value;
                 $.post("/search/user",{
                     query
                 }).then(response => {
                     usersField.innerHTML = "";
                     response = JSON.parse(response);
                     for(let i=0; i<response.length; i++){
                         let user = response[i];
                         console.log(user);
                         usersField.innerHTML += createSearchedUserField(user);
                     }
                 }).catch(error => {
                     console.log(error);
                 })
             })
         })
     </script>

    </head>

    <body>


    </body>

</html>
<% } %>