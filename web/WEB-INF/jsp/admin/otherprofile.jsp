<%-- 
    Document   : otherprofile
    Created on : 27 Nov 2024, 1:26:13 am
    Author     : blonyagoncillo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>View Other Profile</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
        <style>
            table { width: 100%; border-collapse: collapse; }
            th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
            th { background-color: #f2f2f2; }
        </style>
    </head>
    <body>
        <jsp:include page="../../../include/adminheader.jsp" />

        <div class="sidebysidecontainer">
            <a href="do.viewlistings" class="message-button">Go back to Listings</a>
            <a href="do.homepage" class="message-button">Home</a>
        </div>

        <div class="container">
            <c:choose>
                <c:when test="${empty error}">
                    <c:if test="${not empty user}">
                        <h1>${user.username}'s Profile</h1>

                        <p>path ${pageContext.request.contextPath}</p>
                        <img 
                    src="${pageContext.request.contextPath}/images/user/${user.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                    alt="Uploaded Photo" 
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg';" 
                    width="50%" />

                        <p><b>Name:</b> ${user.firstname} ${user.lastname}</p>
                        <p class="profilelabel">Birthday: </p>${user.getDob()}<br><br>
                        <p class="profilelabel"><b>Email Address:</b> ${user.email}</p><br>
                        <p class="profilelabel"><b>About Me:</b> ${user.aboutme}</p><br>
                        <p class="profilelabel"><b>Contact Information:</b> ${user.contact}</p><br>

                        <a href="do.editprofile?userId=${user.id}" class="message-button">Edit Information</a>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <h1>${error}</h1>
                </c:otherwise>
            </c:choose>
        </div>

        <jsp:include page="../include/footer.jsp" />
    </body>
</html>

