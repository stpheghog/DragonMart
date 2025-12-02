<%-- 
    Document   : login
    Created on : Nov 17, 2024, 7:59:02 PM
    Author     : Stephanie
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login</title>
        <link rel="stylesheet" href="css/lightmode.css">
    </head>
    <jsp:include page="include/header.jsp" />

    <body>

        <jsp:include page="include/toggle.jsp" />

    <c:if test="${not empty param.error}">
        <p class="error">${param.error}</p>
    </c:if>

    <a href='index.jsp' class='message-button'>Home</a>
    <div class='container'>

        <h1>Login</h1>

        <form action="do.auth" method="POST">
            <label for="username">Username:</label><br>
            <input type="text" id="username" name="username" required><br><br>

            <label for="password">Password:</label><br>
            <input type="password" id="password" name="password" required><br><br>

            <input type="submit" name="submit" value="Login"><br><br>

            <br>Don't have an account? <a href="signup.jsp">Register here</a><br>
            <a href="index.jsp">Back to Home</a>
        </form>
    </div>

</body>

<jsp:include page="include/footer.jsp" />

</html>
