<%-- 
    Document   : login
    Created on : 24 Nov 2024, 12:17:13 am
    Author     : blonyagoncillo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Admin Login</title>
    <link rel="stylesheet" href="../css/admin.css">
</head>
<body>
    <jsp:include page="../include/adminheader.jsp" />
    
    <div class="container">
        <h1>Login</h1>
        
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        
        <c:if test="${not empty SuccessMessage}">
            <p class="error">${SuccessMessage}</p>
        </c:if>

        <form action="do.auth" method="POST">
            Email address:<br>
            <input type="text" name="email"><br><br>
            Password:<br>
            <input type="password" name="password"><br><br>
            <input type="submit" name="submit" value="Login"><br><br>
        </form>
        
        <a href="../index.jsp">Back to User Home Page</a>
    </div>

    <footer>
        <jsp:include page="../include/footer.jsp" />
    </footer>
</body>
</html>

