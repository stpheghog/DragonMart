<%-- 
    Document   : index
    Created on : Nov 17, 2024, 7:51:25 PM
    Author     : Stephanie
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>DragonMart</title>
        <link rel="stylesheet" href="css/lightmode.css">
    </head>

    <jsp:include page="include/header.jsp" />

    <body>

        <jsp:include page="include/toggle.jsp" />

        <div class="container">
            <h1>Welcome to DragonMart</h1>

            <img src="images/uappy.png" alt="uappy" style="width: 100px; height: auto;">

            <p>Looking for books that won’t break the bank? Uappy’s got your back!</p>
            <p>We understand the struggle of being a college student on a budget, facing those sky-high textbook prices.</p>
            <p>So come on in and explore our magical market, where great stories meet great deals!</p>

            <c:choose>
                <c:when test = "${empty sessionScope.loggedin || sessionScope.loggedin == null}">
                    <h2>Begin your adventure by <a href="login.jsp">LOGGING IN</a> or <a href="signup.jsp">REGISTERING</a>!</h2>

                    <h2>Or start exploring by visiting our <a href="do.shop">SHOP</a>!</h2>
                </c:when>
                <c:otherwise>
                    <h2>I can see you have began your adventure!</h2>

                    <h2>Start exploring by visiting our <a href="do.shop">SHOP</a>!</h2>
                </c:otherwise>
            </c:choose>    

        </div>
    </body>

    <jsp:include page="include/footer.jsp" />

</html>
