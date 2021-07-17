<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Welcome</title>
    </head>

    <body>
        <form id = "test" action = "/commentServlet" method = "post" >
            Comment ID: <input type = "text" name = "comment_id"/><br>
            Comment: <input type = "text" name = "Comment"/><br>
            CommentRequestType: <input type = "text" name = "CommentAction"/><br>
            Blog ID: <input type = "text" name = "blog_id"/>
            <input type = "submit" value="Login">
        </form>
        <p><a href="hello.jsp">Create New Account</a></p>
    </body>

</html>
