<%-- 
    Document   : userprofile
    Created on : Nov 21, 2024, 6:45:59 AM
    Author     : Stephanie
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${sessionScope.loggedin.getUsername()}</title>
        <link rel="stylesheet" href='css/lightmode.css'>
    </head>
    <jsp:include page="../include/header.jsp" />
    <body>

        <jsp:include page="../include/toggle.jsp" />

        <div class="sidebysidecontainer">
            <a href="../do.shop" class="message-button">Go to Shop</a>
            <a href="../index.jsp" class="message-button">Home</a>
        </div>

        <c:choose>
            <c:when test="${not empty error}">
                <p class = 'error'>${error}<p>
            </c:when>
        </c:choose>

        <div class="user-container">
            <div class="container">
                <h1>Welcome, ${sessionScope.loggedin.getUsername()}!</h1>

                <img 
                    src="${pageContext.request.contextPath}/images/user/${sessionScope.loggedin.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                    alt="Uploaded Photo" 
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg';" 
                    width="50%" />

                <br><p class="profilelabel">Name: </p>${sessionScope.loggedin.getFirstname()} ${sessionScope.loggedin.getLastname()}<br><br>
                <p class="profilelabel">Birthday: </p>${sessionScope.loggedin.getDob()}<br><br>
                <p class="profilelabel">Email address: </p>${sessionScope.loggedin.getEmail()}<br><br>
                <p class="profilelabel">About me: </p>${sessionScope.loggedin.getAboutme()}<br><br>
                <p class="profilelabel">Contact Information:</p> ${sessionScope.loggedin.getContact()}<br><br>

                <div class="message-link">
                    <a href="editprofile.jsp" class="message-button">Edit Information</a><br>
                </div>
            </div>

            <div class="user-listing">
                <c:choose>
                    <c:when test="${not empty param.errmsg}">
                        <p class = 'error'>${param.errmsg}<p>
                        </c:when>
                    </c:choose>

                <br><br><div class="message-link">
                    <a href="transactionview.jsp" class="message-button">View Transactions</a><br>
                </div>

                <br><br><div class="message-link">
                    <a href="${pageContext.request.contextPath}/user/do.viewlisting" class="message-button">View Own Listings</a><br>
                </div>

                <div class="message-link">
                    <a href="${pageContext.request.contextPath}/user/do.createview" class="message-button">Create Listing</a><br>
                </div>

                <br><br><br><br><div class="message-link">
                    <a href="${pageContext.request.contextPath}/user/do.logout" class="message-button">Log out</a><br>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="../include/footer.jsp" />
</html>
