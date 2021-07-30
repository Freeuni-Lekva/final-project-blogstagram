<%@ page import="org.blogstagram.models.User" %><%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 13/07/2021
  Time: 22:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = (User) request.getAttribute("CurrentUser");
%>
<html>
    <head>
        <title>Blogstagram</title>
        <jsp:include page="/jsp/templates/bootstrap.jsp" />
        <style>
            .ghost{
                visibility: hidden;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/jsp/templates/nav.jsp" />
        <div class="container" style="margin:100px auto;">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">

                        <div class="card-header">
                            <div class="container">
                                <div class="text-center"><h3 class="text-info" id="form-header">Edit General</h3></div>
                            </div>
                            <div class="row mt-3">
                                <div class="col-4 text-center">
                                    <button type="button" class="btn btn-primary" id="editGeneralButton" onClick="loadForm(this,'editGeneral');">Edit General</button>
                                </div>
                                <div class="col-4 text-center">
                                    <button type="button" class="btn btn-secondary" id="editPictureButton" onClick="loadForm(this,'editPicture');">Edit Picture</button>
                                </div>
                                <div class="col-4 text-center">
                                    <button type="button" class="btn btn-secondary" id="editPasswordButton" onClick="loadForm(this,'editPassword');">Edit Password</button>
                                </div>
                            </div>
                        </div>
                        <div class="card-body" id="edit-form-wrapper">
                            <div id="editGeneralWrapper">
                                <jsp:include page="/jsp/user/edit/templates/editGeneral.jsp" />
                            </div>
                            <div id="editPictureWrapper">
                                <jsp:include page="/jsp/user/edit/templates/editPicture.jsp" />
                            </div>
                            <div id="editPasswordWrapper">
                                <jsp:include page="/jsp/user/edit/templates/editPassword.jsp" />
                            </div>
                        </div>
                        <div class="card-footer">
                            <div class="row text-center">
                                <div class="col-4"><hr /></div>
                                <div class="col-4"><button type="button" class="btn btn-danger">Delete Profile</button></div>
                                <div class="col-4"><hr /></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="/jsp/templates/footer.jsp" />
        <script>

            let currentForm = "editGeneral";
            const formIDList = ["editGeneral","editPicture","editPassword"];
            const formNameList = ["Edit General", "Edit Picture", "Edit Password"];

            function loadForm(e,formAddressName){
                /*
                    Something Is Wrong
                 */
                document.getElementById(`${currentForm}Button`).className = "btn btn-secondary";
                document.getElementById(`${currentForm}Wrapper`).style.display = "none";

                document.getElementById(`${formAddressName}Button`).className = "btn btn-primary";
                document.getElementById(`${formAddressName}Wrapper`).style.display = "block";

                $("#form-header").text(formNameList[formIDList.indexOf(formAddressName)]);

                currentForm = formAddressName;
            }

            $(document).ready(function(){
                document.getElementById(`editPictureWrapper`).style.display = "none";
                document.getElementById(`editPasswordWrapper`).style.display = "none";

            })
        </script>
    </body>
</html>
