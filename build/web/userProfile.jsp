<%-- 
    Document   : userProfile
    Created on : Sep 29, 2024, 7:10:41 PM
    Author     : Stephanie
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> ${pageContext.session.getAttribute("username")} </title>
    </head>
    <body>
        
        <img src = ${pageContext.session.getAttribute("photo")}><br>
            <p> ${pageContext.session.getAttribute("aboutme")} </p><br>
            
        <h1>Hello World!</h1>
    </body>
</html>
