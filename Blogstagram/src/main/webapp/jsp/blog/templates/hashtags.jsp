<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import = "org.blogstagram.models.Blog" %>
<%@ page import = "org.blogstagram.models.HashTag" %>
<%@ page import = "java.util.List" %>
<%
   Blog blog = (Blog) request.getAttribute("blog");
%>
<div class = "input-group-prepend">
    <span class = "bg bg-info input-group-text form-control text-light text-center">Hashtags</span>
</div>
<textarea id = "hashtags" class = "form-control" rows="8" readonly><%
        String result = "\n";
        if(blog != null) {
            List <HashTag> hashtags = blog.getHashTagList();
            for(Integer k = 0; k < hashtags.size(); k++){
                HashTag current = hashtags.get(k);
                result += current.getHashTag() + "\n";
            }
        }
    %><%= result %>
</textarea>
