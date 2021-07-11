<%--
  Created by IntelliJ IDEA.
  User: tazo
  Date: 04.07.21
  Time: 20:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <jsp:include page="/jsp/templates/bootstrap.jsp" />
</head>
<body>
<jsp:include page="/jsp/templates/nav.jsp" />
<div class="container" style="margin-top:124px; margin-bottom:120px; height:423px;" >

    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">Login</div>

                <div class="card-body">
                    <form id="login-form" method="POST" action="/login">
                        <div class="form-group row">
                            <label for="email" class="col-md-4 col-form-label text-md-right">E-Mail Address</label>
                            <div class="col-md-6">
                                <input id="email" type="email" class="form-control " name="email" autofocus="">
                                <div id="error-email" class="text-danger"></div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="password" class="col-md-4 col-form-label text-md-right">Password</label>
                            <div class="col-md-6">
                                <input id="password" type="password" class="form-control" name="password" required="" autocomplete="current-password">
                                <div id="error-password" class="text-danger"></div>
                            </div>
                        </div>

                        <div class="form-group row my-2">
                            <div class="col-md-4"></div>
                            <div class="col-md-6">
                                <button type="submit" class="btn btn-primary w-100">
                                    Login
                                </button>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-md-4"></div>
                            <div class="col-md-6">
                                <button class="btn btn-secondary w-100">
                                    <a class="text-white" href="/register">Aren't Registered Yet? Sign Up!</a>
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

    const fields = ["email", "password"];
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
    let formInput = document.getElementById("login-form");

    formInput.addEventListener('submit',(e) => {
        e.preventDefault();
        let email = document.getElementById("email").value;
        let password = document.getElementById("password").value;
        $.post("/login",{
            email, password
        }).then(rawResponse => {
            refreshFieldMessages();

            if(rawResponse.length === 0){
                refreshFieldMessages();
                document.getElementById("success").innerText = "You have been logged in successfully! You will be redirected to news feed in 5 seconds...";
                setTimeout(() => {
                    window.location.href = "/register";
                },5000);
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