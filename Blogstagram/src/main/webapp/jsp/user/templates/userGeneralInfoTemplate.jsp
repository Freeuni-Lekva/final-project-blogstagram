<%@ page import="org.blogstagram.models.User" %>
<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: Gigi
  Date: 13/07/2021
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = (User)request.getAttribute("User");
    Date birthday = user.getBirthday();
    Integer gender = user.getGender();
    String country = user.getCountry();
    String city = user.getCity();
    String website = user.getWebsite();
%>

<div class="ml-5">

    <% if(birthday != null) { %>
        <span class="font-italic">Birthday:</span>
        <span class="font-weight-light"> <%= birthday %></span><br/>
    <% } %>

    <span class="font-italic">Gender:</span>
        <span class="font-weight-light"><%= (gender == 0) ? "Male" : "Female" %> </span><br/>

    <% if(country != null) { %>
        <span class="font-italic">Country:</span>
        <span class="font-weight-light"><%= country %></span><br/>
    <% } %>

    <% if(city != null) { %>
        <span class="font-italic">City:</span>
        <span class="font-weight-light"><%= city %></span><br/>
    <% } %>

    <% if(website != null) { %>
        <span class="font-italic">Website:</span>
        <span class="font-weight-light"><%= website %></span><br/>
    <% } %>
</div>
