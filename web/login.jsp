<%-- 
    Document   : login
    Created on : Sep 29, 2024, 10:17:46 PM
    Author     : Stephanie
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" href="lightmode.css">
    </head>
    <body>

        <nav class="topnav">
            <div class="logo">
                <img class="logo-img" src="images/logo.png" alt="logo">
                <p class="logo-text">DragonMart</p>
            </div>
            <ul class="nav-list">
                <li><a href="index.jsp" class="home">Home</a></li>
                <li><a href="shop.jsp">Shop</a></li>
                <li><a href="#contact">Contact</a></li>
                <li><a href="#about">About</a></li>
            </ul>
            <div class="auth-links">
                <a href="login.jsp" class="registration">Login</a>
                <a href="signup.jsp" class="registration">Sign up</a>
            </div>
        </nav>

        <div class = "registration-container">
            
            <h1> Login </h1>
            <form action="do.login" method = POST>

                Email address: <br>
                <input type = "text" name = "email"><br><br>
                Password: <br>
                <input type = "password" name = "password"><br><br>
                <input type = "submit" name = "submit" value = "Login"><br><br>

            </form>
            
        </div>

    </body>
</html>
