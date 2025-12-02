<%-- 
    Document   : viewcommentrequest
    Created on : 25 Nov 2024, 12:16:37 am
    Author     : blonyagoncillo
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Pending Delete Comment Requests</title>
        <style>
            table { width: 100%; border-collapse: collapse; }
            th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
            th { background-color: #f2f2f2; }
        </style>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    </head>
    <body>
        <jsp:include page="../../../include/adminheader.jsp" />
        
        <c:choose>
            <c:when test="${not empty errmsg}">
                <p class = 'error'>${errmsg}<p>
                </c:when>
            </c:choose>
        
        <h2>Pending Delete Comment Requests</h2>
        <table>
            <thead>
                <tr>
                    <th>Request ID</th>
                    <th>Delete Request of Admin</th>
                    <th>Comment ID</th>
                    <th>Comment Content</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="report" items="${deleteReqs}">
                    <tr>
                        <td>${report.request_id}</td>
                        <td>${report.admin_id}</a></td>
                        <td>${report.comment_id}</td>
                        <td>${report.content}</td>
                        <td>
                            <form action="do.resolvecommentrequest" method="POST" style="display:inline;">
                                <input type="hidden" name="requestId" value="${report.request_id}" />
                                <input type="hidden" name="commentId" value="${report.comment_id}" />
                                <button type="submit" name="action" value="approve">Approve</button>
                            </form>
                            <form action="do.resolvecommentrequest" method="POST" style="display:inline;">
                                <input type="hidden" name="requestId" value="${report.request_id}" />
                                <input type="hidden" name="commentId" value="${report.comment_id}" />
                                <button type="submit" name="action" value="deny">Deny</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <p><a href="do.homepage"><b>Back to Home</b></a></p>
    </body>
</html>