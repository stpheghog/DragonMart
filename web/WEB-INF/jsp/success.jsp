<%-- 
    Document   : success
    Created on : Nov 17, 2024, 8:25:41 PM
    Author     : Stephanie
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${type} Success</title>
        <link rel="stylesheet" href="css/lightmode.css">
    </head>
    <jsp:include page="../../include/header.jsp" />
    <body>

        <jsp:include page="../../include/toggle.jsp" />

        <div class="container">
            <br><br>

            <h1>${type} Successful!</h1>

            <c:choose>
                <c:when test = "${type == 'Registration'}">
                    <p>Waiting for approval from administrator...</p><br>
                    <p>If approved continue to <a href="../login.jsp"><b>login</b></a></p><br>
                </c:when>
                <c:when test="${type == 'Login'}">
                    <p>No account? <a href="../signup.jsp"><b>Register Here</b></a></p>
                    <p>Already have an account? <a href="../login.jsp"><b>Login</b></a></p>
                </c:when>
                <c:otherwise>
                    <p><a href="${frontLink}"><b>${frontName}</b></a></p>
                </c:otherwise>
            </c:choose>    

            <br><a href="../index.jsp" ><b>Back to Home</b></a>

            <br><br>
        </div>

    </body>

    <jsp:include page="../../include/footer.jsp" />

</html>
