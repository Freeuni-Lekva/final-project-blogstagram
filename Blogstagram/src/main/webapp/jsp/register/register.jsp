<%@ page import="org.blogstagram.errors.VariableError" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 25/06/2021
  Time: 23:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

    <head>
        <title>Blogstagram</title>
        <jsp:include page="/jsp/templates/bootstrap.jsp" />
    </head>
    <body>
        <jsp:include page="/jsp/templates/nav.jsp" />
        <div class="container my-4">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header">Register</div>

                        <div class="card-body">
                            <form id="register-form" method="POST" action="/register">

                                <div class="form-group row">
                                    <label for="firstname" class="col-md-4 col-form-label text-md-right">Firstname</label>

                                    <div class="col-md-6">
                                        <input id="firstname" type="text" class="form-control " name="firstname" required />
                                        <div id="error-firstname" class="text-danger">

                                        </div>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="lastname" class="col-md-4 col-form-label text-md-right">Lastname</label>

                                    <div class="col-md-6">
                                        <input id="lastname" type="text" class="form-control" name="lastname" required />
                                        <div id="error-lastname" class="text-danger">

                                        </div>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="nickname" class="col-md-4 col-form-label text-md-right">Nickname</label>
                                    <div class="col-md-6">
                                        <input id="nickname" type="text" class="form-control" name="lastname" required  />
                                        <div id="error-nickname" class="text-danger">

                                        </div>
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="email" class="col-md-4 col-form-label text-md-right">E-Mail Address</label>

                                    <div class="col-md-6">
                                        <input id="email" type="email" class="form-control" name="email" required />
                                        <div id="error-email" class="text-danger">

                                        </div>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="birthday" class="col-md-4 col-form-label text-md-right">Birthday</label>
                                    <div class="col-md-6">
                                        <input id="birthday" type="date" class="form-control" name="birthday" required />
                                        <div id="error-birthday" class="text-danger">

                                        </div>
                                    </div>

                                </div>

                                <div class="form-group row">
                                    <label for="gender" class="col-md-4 col-form-label text-md-right">Gender</label>
                                    <div class="col-md-6">
                                        <select id="gender" name="gender" class="form-control" required>
                                            <option value="" selected>Select</option>
                                            <option value="0">Male</option>
                                            <option value="1">Female</option>
                                        </select>
                                        <div id="error-gender" class="text-danger">

                                        </div>
                                    </div>

                                </div>

                                <div class="form-group row">
                                    <label for="privacy" class="col-md-4 col-form-label text-md-right">Privacy</label>
                                    <div class="col-md-6">
                                        <select  id="privacy" name="privacy" class="form-control">
                                            <option value="0" selected>Public</option>
                                            <option value="1">Private</option>
                                        </select>
                                        <div id="error-privacy" class="text-danger">

                                        </div>
                                    </div>

                                </div>

                                <div class="form-group row">
                                    <label for="password" class="col-md-4 col-form-label text-md-right">Password</label>

                                    <div class="col-md-6">
                                        <input id="password" type="password" class="form-control" name="password" required=""  />
                                        <div id="error-password" class="text-danger">

                                        </div>
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="password_confirmation" class="col-md-4 col-form-label text-md-right">Confirm Password</label>

                                    <div class="col-md-6">
                                        <input id="password_confirmation" type="password" class="form-control" name="password_confirmation" required />
                                        <div id="error-password_confirmation" class="text-danger">

                                        </div>
                                    </div>
                                </div>

                                <div class="form-group row my-2">
                                    <div class="col-md-4"></div>
                                    <div class="col-md-6">
                                        <button type="submit" class="btn btn-primary w-100">
                                            Register
                                        </button>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <div class="col-md-4"></div>
                                    <div class="col-md-6">
                                        <button class="btn btn-secondary w-100">
                                            <a class="text-white" href="#">Already Signed Up? Log In!</a>
                                        </button>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <div class="col-md-4"></div>
                                    <div class="col-md-6">
                                        <div id="success" class="text-success">

                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/jsp/templates/footer.jsp" />
        <script>
            const fields = ["firstname","lastname","nickname","birthday","email","gender","privacy","password","password_confirmation"];

            function addFieldChangeListeners(){
                for(let field of fields){
                    document.getElementById(field).addEventListener('input',(e) => {
                        document.getElementById(`error-${field}`).textContent = '';
                        document.getElementById(field).classList.remove('is-invalid');
                    });
                }
            }
            addFieldChangeListeners();

            function refreshFieldMessages(){
                for(let field of fields){
                    document.getElementById(`error-${field}`).textContent = '';
                    document.getElementById(field).classList.remove('is-invalid');
                }
            }
            let formInput = document.getElementById("register-form");
            formInput.addEventListener('submit',(e) => {
                e.preventDefault();

                let firstname = document.getElementById("firstname").value;
                let lastname = document.getElementById("lastname").value;
                let nickname = document.getElementById("nickname").value;
                let email = document.getElementById("email").value;
                let birthday = document.getElementById("birthday").value;
                let gender = document.getElementById("gender").value;
                let privacy = document.getElementById("privacy").value;
                let password = document.getElementById("password").value;
                let password_confirmation = document.getElementById("password_confirmation").value;
                $.post("/register",{
                    firstname,lastname,nickname,email,birthday,gender,privacy,password,password_confirmation
                }).then(rawResponse => {
                    refreshFieldMessages();

                    if(rawResponse.length === 0){
                        document.getElementById("success").innerText = "You have been registered successfully!";
                    } else {
                        let errors = JSON.parse(rawResponse);
                        for(let error of errors){
                            let {variableName, errorMessage} = error;
                            let errorID = "error-"+variableName;
                            console.log(errorID,errorMessage);
                            document.getElementById(errorID).innerText = errorMessage+"\n";
                            document.getElementById(variableName).classList.add('is-invalid');
                        }
                    }

                }).catch(error => {
                    console.log(error);
                });
            });
        </script>

    </body>
</html>
