<%@ page import="org.blogstagram.models.User" %><%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 13/07/2021
  Time: 22:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = (User)request.getAttribute("CurrentUser");
%>
<form id="edit-picture-form">
    <div class="container text-center">
        <img id="upload-picture-file" src="<%= user.getImage() %>" style="width: 200px; height: 200px; margin: 15px; border-radius:50%"/>
    </div>
    <div class="custom-file">
        <input type="file" class="custom-file-input" id="customFile" required/>
        <label class="custom-file-label" for="customFile">Choose file</label>
    </div>
    <div class="container text-center mt-3">
        <button type="submit" class="btn btn-primary">Submit</button>
    </div>

</form>
<script>
    $(document).ready(function(){
        document.getElementById("customFile").addEventListener("input",(e) => {
            const pictureFile = e.target.files[0];
            const pictureSource = URL.createObjectURL(pictureFile);
            document.getElementById("upload-picture-file").setAttribute("src",pictureSource);
        });
    })
</script>