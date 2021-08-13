<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "org.blogstagram.models.Blog" %>

<%
    Blog blog = (Blog) request.getAttribute("blog");
    Integer creatorId;
    if(blog == null) creatorId = (Integer)request.getSession().getAttribute("currentUserID");
    else creatorId = blog.getUser_id();
%>
<div id="searchModeratorsModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title text-center">Search for users</h4>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <input type="text" id="search-moderators-query" class="form-control" placeholder="Search...">
                    <div class="overflow-auto" id="search-moderators-field">

                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>

        </div>
    </div>



    <script>

        function createSearchedUserField(user){
            if(user.id == <%= creatorId %>) return "";
            return `
                <div class="media border py-2 my-2">
                    <img class="ml-2" width="70px" height="70px" style="object-fit:cover; border-radius:50%" src="/blogstagram/${user.image}" alt="Generic placeholder image">
                    <div class="media-body text-center">
                        <h5 class="pt-4">
                            <div class = "container row">
                                <h6 class = "col">${user.firstname} ${user.lastname} (${user.nickname})</h6>
                                <button class = "form-control btn btn-info" onclick = "addModerator('${user.nickname}')">Add</button>
                            </div>
                        </h5>
                  </div>
                </div>
            `
        }



        $(document).ready(function(){
            let usersField = document.getElementById("search-moderators-field");
            let inputField = document.getElementById("search-moderators-query");
            inputField.addEventListener("input",(e) => {
                const query = inputField.value;
                $.post("/blogstagram/search/user",{
                    query
                }).then(response => {
                    usersField.innerHTML = "";
                    response = JSON.parse(response);
                    let moderators = document.getElementById("blogModerators");
                    moderators.removeAttribute("readonly");
                    for(let i=0; i<response.length; i++){
                        let user = response[i];
                        console.log(user);
                        let field = createSearchedUserField(user);
                        if(field === "") continue;
                        usersField.innerHTML += field;
                    }
                    moderators.setAttribute("readonly", "");
                }).catch(error => {
                    console.log(error);
                })
            })
        })
    </script>