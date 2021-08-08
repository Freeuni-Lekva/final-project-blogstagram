<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Integer currentUserID = (Integer)request.getSession().getAttribute("currentUserID");
%>

<!-- Button trigger modal -->
<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteModal">
    Delete
</button>

<!-- Modal -->
<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Are you sure you want to delete your account?</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="alert alert-warning">
                    Your account will be deleted permanently from our website. You will not be able to recover your account.

                </div>
            </div>
            <div class="modal-footer">
                <div id="deleteUserID" style="display:none"><%= currentUserID %></div>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-danger" id="userDeleteButton">Delete</button>
            </div>
        </div>
    </div>
</div>

<script>
    $("#userDeleteButton").click(function(e){
        const deleteUserID = document.getElementById("deleteUserID").innerText;
        $.post("/delete/user",{deleteUserID}).then(response => {
            let responseJSON = JSON.parse(response);
            location.reload();
        })
    })
</script>
