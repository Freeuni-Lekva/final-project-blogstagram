<%--
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
                            <form method="POST" action="/register">

                                <div class="form-group row">
                                    <label for="firstname" class="col-md-4 col-form-label text-md-right">Firstname</label>

                                    <div class="col-md-6">
                                        <input id="firstname" type="text" class="form-control " name="firstname" required />

                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="lastname" class="col-md-4 col-form-label text-md-right">Lastname</label>

                                    <div class="col-md-6">
                                        <input id="lastname" type="text" class="form-control" name="lastname" required />

                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="nickname" class="col-md-4 col-form-label text-md-right">Nickname</label>
                                    <div class="col-md-6">
                                        <input id="nickname" type="text" class="form-control" name="lastname" required  />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="email" class="col-md-4 col-form-label text-md-right">E-Mail Address</label>

                                    <div class="col-md-6">
                                        <input id="email" type="email" class="form-control" name="email" required />
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
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="privacy" class="col-md-4 col-form-label text-md-right">Privacy</label>
                                    <div class="col-md-6">
                                        <select  id="privacy" name="privacy" class="form-control">
                                            <option value="0" selected>Public</option>
                                            <option value="1">Private</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="password" class="col-md-4 col-form-label text-md-right">Password</label>

                                    <div class="col-md-6">
                                        <input id="password" type="password" class="form-control" name="password" required=""  />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="password-confirm" class="col-md-4 col-form-label text-md-right">Confirm Password</label>

                                    <div class="col-md-6">
                                        <input id="password-confirm" type="password" class="form-control" name="password_confirmation" required />
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
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/jsp/templates/footer.jsp" />
        <script type="text/javascript" src="/jsp/register/register.js"></script>

    </body>
</html>
