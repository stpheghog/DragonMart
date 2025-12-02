<%-- 
    Document   : viewuserreport
    Created on : 25 Nov 2024, 12:18:03 am
    Author     : blonyagoncillo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            table { width: 100%; border-collapse: collapse; }
            th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
            th { background-color: #f2f2f2; }
        </style>

        <title>Pending User Reports</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    </head>
    <body>
        <jsp:include page="../../../include/adminheader.jsp"/>
        <h2>Pending User Reports</h2>

    <c:if test="${empty userReps}">HELLO ITS EMPTY</c:if>
        
        <table>
            <thead>
                <tr>
                    <th>Request ID</th>
                    <th>Complaint by User</th>
                    <th>Comment ID</th>
                    <th>Comment Content</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <c:if test="${not empty userReps}">
                <c:forEach var="userreport" items="${userReps}">
                    <tr>
                        <td>${userreport.request_id}</td>
                        <td><a href="do.adminother?id=${userreport.user_id}">${userreport.user_id}</a></td>
                        <td>${userreport.comment_id}</td>
                        <td>${userreport.content}</td>
                        <td>
                            <form action="do.resolveuserreport" method="POST" style="display:inline;">
                                <input type="hidden" name="requestId" value="${userreport.request_id}" />
                                <input type="hidden" name="commentId" value="${userreport.comment_id}" />
                                <button type="submit" name="action" value="approve">Approve</button>
                            </form>
                            <form action="do.resolveuserreport" method="POST" style="display:inline;">
                                <input type="hidden" name="requestId" value="${userreport.request_id}" />
                                <input type="hidden" name="commentId" value="${userreport.comment_id}" />
                                <button type="submit" name="action" value="deny">Deny</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </c:if>
        </table>

        <p><a href="do.homepage">Back to Home</a></p>
    </body>
</html>

