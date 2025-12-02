<%-- 
    Document   : approvelist
    Created on : 25 Nov 2024, 12:14:20 am
    Author     : blonyagoncillo
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Approve List</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    </head>
    <body>
        <jsp:include page="../../../include/adminheader.jsp" />

        <div class="container">
            <form method="get" action="do.approvelist">
                <input type="text" name="searchQuery" placeholder="Search username..." value="${searchQuery}" />
                <input type="submit" value="Search" />
            </form>
            <br>

            <c:if test="${not empty userList}">
                <p>Current users waiting for approval...</p>
                <form method="post" action="do.approve">
                    <c:forEach var="user" items="${userList}">
                        <div class="approveitem">
                            <input type="checkbox" name="approveUserId" value="${user.id}" />
                            <p>${user.firstname} ${user.lastname}</p>
                            <p>Username: ${user.username}</p>
                            <p>Email: ${user.email}</p>
                            <p>DOB: ${user.dob}</p>
                        </div>
                        <br>
                    </c:forEach>
                    <input type="submit" value="Approve" name="action" />
                    <input type="submit" value="Deny" name="action" />
                </form>
            </c:if>

            <c:if test="${empty userList}">
                <p>No users waiting to be approved...</p>
            </c:if>
        </div>

        <div class="message-link">
            <a href="do.homepage" class="message-button">Home</a>
        </div>

        <jsp:include page="../include/footer.jsp" />
    </body>
</html>

