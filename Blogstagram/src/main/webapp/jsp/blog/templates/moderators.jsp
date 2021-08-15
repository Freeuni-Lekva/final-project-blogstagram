<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "org.blogstagram.models.Blog" %>
<%@ page import = "org.blogstagram.models.User" %>
<%@ page import = "java.util.List" %>

<%
    Blog blog = (Blog) request.getAttribute("blog");
%>


<div class = "input-group-prepend">
    <span class = "bg bg-info input-group-text form-control text-light text-center">Moderators</span>
</div>
<textarea id = "blogModerators" class = "form-control" rows = "8" readonly><%
        String result = "";
        if(blog != null){
            List <User> blogModerators = blog.getBlogModerators();
            for(Integer k = 0; k < blogModerators.size(); k++){
                result += blogModerators.get(k).getNickname() + "\n";
            }
        }
    %><%= result %></textarea>
<div id = "err-moderator" class = "container text-danger">
</div>
