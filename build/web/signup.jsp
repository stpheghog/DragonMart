<%-- 
    Document   : signup
    Created on : Sep 29, 2024, 10:17:57 PM
    Author     : Stephanie
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration</title>
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

            <h1> Create an Account </h1>
            <form action="do.register" method = POST>

                First Name: <br>
                <input type = "text" name = "fn" required><br><br>
                Last Name: <br>
                <input type = "text" name = "ln" required><br><br>
                Username: <br>
                <input type = "text" name = "username"required><br><br>
                Email address: <br>
                <input type = "text" name = "email"required><br><br>
                Date of Birth: <br>
                <input type = "date" name = "dob"><br><br>
                Password: <br>
                <input type = "password" name = "password"required><br><br>
                Confirm Password: <br>
                <input type = "password" name = "check"required><br><br>
                
                <input type = "submit" name = "submit" value = "Register"><br><br>
                
            </form>
        </div> 
    </body>
</html>
