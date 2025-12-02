<%-- 
    Document   : signup
    Created on : Nov 17, 2024, 8:02:50 PM
    Author     : Stephanie
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registration</title>
        <link rel="stylesheet" href="css/lightmode.css">
    </head>
    <jsp:include page="include/header.jsp" />

    <body>
        <jsp:include page="include/toggle.jsp" />
        <a href='index.jsp' class='message-button'>Home</a>
        <div class='container'>

            <h1>Create an Account</h1>

            <form action="do.register" method="POST">
                <label for="fn">First Name:</label><br>
                <input type="text" id="fn" name="fn"><br><br>

                <label for="ln">Last Name:</label><br>
                <input type="text" id="ln" name="ln"><br><br>

                <label for="username">Username:</label><br>
                <input type="text" id="username" name="username"><br><br>

                <label for="email">Email address:</label><br>
                <input type="text" id="email" name="email"><br><br>

                <label for="dob">Date of Birth:</label><br>
                <input type="date" id="dob" name="dob"><br><br>

                <label for="password">Password:</label><br>
                <input type="password" id="password" name="password"><br><br>

                <label for="check">Confirm Password:</label><br>
                <input type="password" id="check" name="check"><br><br>

                <input type="submit" name="submit" value="Register"><br><br>
            </form>

            <p>Already have an account? <a href="do.login">Login</a></p>
            <p><a href="index.jsp">Back to Home</a></p>

    </body>
    <jsp:include page="include/footer.jsp" />
</html>
