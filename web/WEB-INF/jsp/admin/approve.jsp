<%-- 
    Document   : approve
    Created on : 26 Nov 2024, 12:27:01 am
    Author     : blonyagoncillo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Approving...</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    </head>

    <jsp:include page="../../../include/adminheader.jsp"/>

    <body>

        <div class = "container">

            <h1>Action Completed Successfully</h1>

            <c:choose>
                <c:when test="${action == 'Approve'}">
                    <h2>Approved Users:</h2>

                    <c:forEach var="user" items="${approveUserList}">
                        ${user.getUsername()} has been approved. <br>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:forEach var="user" items="${approveUserList}">
                        ${user.getUsername()} has been denied. <br>
                    </c:forEach>
                </c:otherwise>
            </c:choose>

        </div>

        <div class='message-link'>
            <a href="do.approvelist" class='message-button'>Back to Approval List</a>
        </div>

        <div class='message-link'>
            <a href="do.homepage" class='message-button'>Home</a>
        </div>

    </body>
</html>

