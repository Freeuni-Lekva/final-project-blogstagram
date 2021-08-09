<%@ page import="org.blogstagram.models.User" %><%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 13/07/2021
  Time: 22:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = (User)request.getAttribute("CurrentUser");
    String country = user.getCountry();
    if(country == null) country = "";
    String city = user.getCity();
    if(city == null) city = "";
    String website = user.getWebsite();
    if(website == null) website = "";
    String bio = user.getBio();
    if(bio == null) bio = "";

%>
<form id="edit-profile-general-form">
    <div class="form-row">
        <div class="form-group col-md-6">
            <label for="firstname">Firstname</label>
            <input type="text" class="form-control" id="firstname" placeholder="Fyodor" value="<%= user.getFirstname() %>" required/>
            <div id="error-firstname" class="text-danger">

            </div>
        </div>
        <div class="form-group col-md-6">
            <label for="lastname">Lastname</label>
            <input type="text"  class="form-control" id="lastname" placeholder="Dostoevsky" value="<%= user.getLastname() %>" required/>
            <div id="error-lastname" class="text-danger">

            </div>
        </div>
    </div>
    <div class="form-group">
        <label for="email">Email</label>
        <input type="email" class="form-control" id="email" placeholder="test@test.com" value="<%= user.getEmail() %>" required/>
        <div id="error-email" class="text-danger">

        </div>
    </div>
    <div class="form-group">
        <label for="nickname">Nickname</label>
        <input type="text" class="form-control" id="nickname" placeholder="el.nicknamo" value="<%= user.getNickname() %>" required/>
        <div id="error-nickname" class="text-danger">

        </div>
    </div>

    <div class="form-group">
        <label for="birthday">Birthday</label>
        <input id="birthday" type="date" class="form-control" name="birthday" value="<%= user.getBirthday() %>" required />
        <div id="error-birthday" class="text-danger">

        </div>
    </div>
    <div class="form-group">
        <label for="privacy">Privacy</label>
        <select name="privacy" id="privacy" class="form-control">
            <% if(user.getPrivacy() == 0) { %>
                <option value="0" selected>Public</option>
                <option value="1">Private</option>
            <% } else { %>
                <option value="0">Public</option>
                <option value="1" selected>Private</option>
            <% } %>

        </select>
        <div id="error-privacy" class="text-danger">

        </div>
    </div>
    <div class="form-group">
        <label for="gender">Gender</label>
        <select name="gender" id="gender" class="form-control">

            <% if(user.getGender() == 0) { %>
                <option value="0" selected>Male</option>
                <option value="1">Female</option>
            <% } else { %>
                <option value="0">Male</option>
                <option value="1" selected>Female</option>
            <% } %>

        </select>
        <div id="error-gender" class="text-danger">

        </div>
    </div>
    <div class="form-group">
        <label for="country">Country</label>
        <input type="text"  class="form-control" class="form-control" id="country" placeholder="Georgia / USA / Germany" value="<%= country %>" />
        <div id="error-country" class="text-danger">

        </div>
    </div>
    <div class="form-group">
        <label for="city">City</label>
        <input type="text"  class="form-control" class="form-control" id="city" placeholder="Tbilisi / Batumi / New York" value="<%= city %>"/>
        <div id="error-city" class="text-danger">

        </div>
    </div>
    <div class="form-group">
        <label for="website">Website</label>
        <input type="text"  class="form-control" class="form-control" id="website" placeholder="www.testweb.ge" value="<%= website %>"/>
        <div id="error-website" class="text-danger">

        </div>
    </div>
    <div class="form-group">
        <label for="bio">Bio</label>
        <textarea name="bio" id="bio" class="form-control" style="resize:none; height:150px;" placeholder="Tell people what you think!"><%= bio %></textarea>
        <div class="container text-muted text-center">Bio shouldn't be longer than 255 symbols</div>
        <div id="error-bio" class="text-danger">

        </div>
    </div>
    <hr class="my-4" />
    <div class="container text-center">
        <button type="submit" class="btn btn-primary">Save Changes</button>
    </div>
    <div id="generalSuccess" class="text-success text-center">

    </div>
</form>

<script>
    let areGeneralListenersAdded = false;
    const generalFields = ["firstname","lastname","nickname","birthday","email","gender","privacy","country","city","website","bio"];

    function addGeneralFieldChangeListeners(){
        for(let field of generalFields){
            document.getElementById(field).addEventListener('input',(e) => {
                document.getElementById(`error-${field}`).textContent = '';
                document.getElementById(field).classList.remove('is-invalid');
            });
        }
    }

    function refreshGeneralFieldMessages(){
        for(let field of generalFields){
            document.getElementById(`error-${field}`).textContent = '';
            document.getElementById(field).classList.remove('is-invalid');
        }
    }

    let generalFormInput = document.getElementById("edit-profile-general-form");
    generalFormInput.addEventListener('submit',(e) => {
        e.preventDefault();
        if(!areGeneralListenersAdded){
            addGeneralFieldChangeListeners()
            areGeneralListenersAdded = true;
        }

        const status = "EditGeneral";

        let firstname = document.getElementById("firstname").value;
        let lastname = document.getElementById("lastname").value;
        let nickname = document.getElementById("nickname").value;
        let email = document.getElementById("email").value;
        let birthday = document.getElementById("birthday").value;
        let gender = document.getElementById("gender").value;
        let privacy = document.getElementById("privacy").value;
        let country = document.getElementById("country").value;
        let city = document.getElementById("city").value;
        let website = document.getElementById("website").value;
        let bio = document.getElementById("bio").value;
        $.post("/blogstagram/edit/profile",{
            firstname,lastname,nickname,email,birthday,gender,privacy,country,city,website,bio,status
        }).then(rawResponse => {
            refreshGeneralFieldMessages();

            if(rawResponse.length === 0){
                document.getElementById("generalSuccess").innerText = "Your general info has been updated successfully!";
                setTimeout(() => {
                    location.reload();
                },1500);
            } else {
                let errors = JSON.parse(rawResponse);
                for(let error of errors){
                    let {variableName, errorMessage} = error;
                    let errorID = "error-"+variableName;
                    document.getElementById(errorID).innerText += errorMessage+"\n";
                    document.getElementById(variableName).classList.add('is-invalid');
                }
            }

        }).catch(error => {
            console.log(error);
        });
    });
</script>