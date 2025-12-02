<%-- 
    Document   : homepage
    Created on : 24 Nov 2024, 10:35:07 pm
    Author     : blonyagoncillo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Profile</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">

    </head>
    <body>
        <jsp:include page="../../../include/adminheader.jsp" />

        <div class="admin-container">
            <h1>Welcome, Admin!</h1>
            <p>Email: ${sessionScope.admin.email}</p>

            <c:if test="${not empty error || not empty param.error}">
                <p class="error">
                    <c:choose>
                        <c:when test="${not empty error}">${error}</c:when>
                        <c:when test="${not empty param.error}">${param.error}</c:when>
                    </c:choose>
                </p>
            </c:if>

            <c:if test="${not empty SuccessMessage || not empty param.SuccessMessage}">
                <p class="error">
                    <c:choose>
                        <c:when test="${not empty SuccessMessage}">${SuccessMessage}</c:when>
                        <c:when test="${not empty param.SuccessMessage}">${param.SuccessMessage}</c:when>
                    </c:choose>
                </p>
            </c:if>


            <h2>Admin Functions</h2>
            <div class="sidebysidecontainer">

                <div class="container">
                    <div class="message-link">
                        <a href="do.approvelist" class="message-button">Approve Registration Requests</a><br>
                    </div>
                    <div class="message-link">
                        <a href="${pageContext.request.contextPath}/admin/do.viewallusers" class="message-button">View All Users</a><br>
                    </div>
                    <div class="message-link">
                        <a href="${pageContext.request.contextPath}/admin/do.viewlistings" class="message-button">View All Listings</a><br>
                    </div>
                </div>

                <!-- Second set of admin links -->
                <div class="container">
                    <div class="message-link">
                        <a href="${pageContext.request.contextPath}/admin/do.viewlistingrequest" class="message-button">Delete Listing Requests</a><br>
                    </div>
                    <div class="message-link">
                        <a href="${pageContext.request.contextPath}/admin/do.viewuserrequest" class="message-button">Delete User Requests</a><br>
                    </div>
                </div>

                <div class="container">
                    <div class="message-link">
                        <a href="${pageContext.request.contextPath}/admin/do.viewcommentrequest" class="message-button">Delete Comment Requests</a><br>
                    </div>
                    <div class="message-link">
                        <a href="${pageContext.request.contextPath}/admin/do.viewuserreport" class="message-button">User Reported Comments</a><br>
                    </div>
                </div>
            </div>

            <div class="message-link">
                <a href="do.logout" class="message-button">Logout</a><br>
            </div>
        </div>
    </body>
</html>

