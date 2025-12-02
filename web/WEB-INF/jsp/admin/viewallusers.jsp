<%-- 
    Document   : viewallusers
    Created on : 25 Nov 2024, 12:16:04 am
    Author     : blonyagoncillo
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>View All Users</title>
        <style>
            table { width: 100%; border-collapse: collapse; }
            th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
            th { background-color: #f2f2f2; }
        </style>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    </head>
    <body>

        <jsp:include page="../../../include/adminheader.jsp" />

        <h2>All Users</h2>
        
        <c:if test="${not empty error}">
                <p class="error">${error}</p>
        </c:if>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>

                <c:forEach var="user" items="${userlist}">
                    <tr>
                        <td><a href="do.adminother?id=${user.id}">${user.id}</a></td>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>
                            <form action="do.editprofile" method="POST" style="display:inline;">
                                <input type="hidden" name="userId" value="${user.id}" />
                                <button type="submit">Edit</button>
                            </form>
                            <form action="do.deleteuser" method="POST" style="display:inline;">
                                <input type="hidden" name="userId" value="${user.id}" />
                                <button type="submit" onclick="return confirm('Are you sure you want to delete this user?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <p><a href="do.homepage"><b>Back to Home</b></a></p>
    </body>
</html>
