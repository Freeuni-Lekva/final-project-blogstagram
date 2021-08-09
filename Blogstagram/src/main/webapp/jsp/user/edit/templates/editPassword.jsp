<%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 13/07/2021
  Time: 22:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form id="edit-password-form">
    <div class="row mb-4">
        <div class="col-md-6">
            <div class="form-group">
                <label for="old_password">Old Password</label>
                <input  type="password" class="form-control" name="old_password" id="old_password" required/>
                <div id="error-old_password" class="text-danger">

                </div>
            </div>

            <div class="form-group">
                <label for="new_password">New Password</label>
                <input type="password" class="form-control" name="new_password" id="new_password" required/>
                <div id="error-new_password" class="text-danger">

                </div>
            </div>
            <div class="form-group">
                <label for="new_password_confirmation">Confirm Password</label>
                <input type="password" class="form-control" name="new-password_confirmation" id="new_password_confirmation" required/>
                <div id="error-new_password_confirmation" class="text-danger">

                </div>
            </div>
        </div>
        <div class="col-md-6">
            <p class="mb-2">Password requirements</p>
            <p class="small text-muted mb-2">To create a new password, you have to meet all of the following requirements:</p>
            <ul class="small text-muted pl-4 mb-0">
                <li>Minimum 8 character</li>
                <li>At least one uppercase character</li>
                <li>At least one lowercase character</li>
                <li>At least one digit</li>
                <li>Can’t be the same as a previous password</li>
            </ul>
        </div>
    </div>
    <hr class="my-4"/>
    <div class="container text-center">
        <button type="submit" class="btn btn-primary">Save Changes</button>
    </div>
    <div id="passwordSuccess" class="text-success text-center">

    </div>
    <script>
        let arePasswordListenersAdded = false;
        const passwordFields = ["old_password","new_password","new_password_confirmation"];


        function addPasswordFieldChangeListeners(){
            for(let field of passwordFields){
                document.getElementById(field).addEventListener('input',(e) => {
                    document.getElementById(`error-${field}`).textContent = '';
                    document.getElementById(field).classList.remove('is-invalid');
                });
            }
        }
        function refreshPasswordFieldMessages(){
            for(let field of passwordFields){
                document.getElementById(`error-${field}`).textContent = '';
                document.getElementById(field).classList.remove('is-invalid');
            }
        }

        $(document).ready(function(){
            $("#edit-password-form").submit(function(e){
                e.preventDefault();

                if(!arePasswordListenersAdded){
                    addPasswordFieldChangeListeners();
                    arePasswordListenersAdded = true;
                }

                const status = "EditPassword";

                let old_password = document.getElementById("old_password").value;
                let new_password = document.getElementById("new_password").value;
                let new_password_confirmation = document.getElementById("new_password_confirmation").value;

                $.post("/blogstagram/edit/profile",{
                    old_password,new_password,new_password_confirmation,status
                    }
                ).then((rawResponse) => {
                    refreshPasswordFieldMessages();
                    if(rawResponse.length === 0){
                        document.getElementById("passwordSuccess").innerText = "Your password has been updated successfully!";
                        setTimeout(() => {
                            location.reload();
                        },1500);
                    } else {
                        let errors = JSON.parse(rawResponse);
                        if(errors.status === 'status incorrect'){
                            console.log("Wrong status for editing profile");
                        } else {
                            for(let error of errors){

                                let {variableName, errorMessage} = error;
                                let errorID = "error-"+variableName;
                                document.getElementById(errorID).innerText += errorMessage+"\n";
                                document.getElementById(variableName).classList.add('is-invalid');
                            }
                        }

                    }
                })

            })
        })

    </script>