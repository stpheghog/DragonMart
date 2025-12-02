<%-- 
    Document   : viewuserrequest
    Created on : 25 Nov 2024, 12:34:19 am
    Author     : blonyagoncillo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Pending User Delete Requests</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
        <style>
            table { width: 100%; border-collapse: collapse; }
            th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
            th { background-color: #f2f2f2; }
        </style>
    </head>
    <body>
        <jsp:include page="../../../include/adminheader.jsp" />

        <h2>Pending User Delete Requests</h2>

        <c:choose>
            <c:when test="${not empty updateRequests}">
                <table>
                    <thead>
                        <tr>
                            <th>Request ID</th>
                            <th>Admin</th>
                            <th>User ID</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="request" items="${updateRequests}">
                            <tr>
                                <td>${request.request_id}</td>
                                <td>${request.admin_id}</td>
                                <td>
                                    <a href="do.adminother?id=${request.user_id}">
                                        ${request.user_id}
                                    </a>
                                </td>
                                <td>
                                    <form action="do.resolveuserrequest" method="POST" style="display:inline;">
                                        <input type="hidden" name="requestId" value="${request.request_id}" />
                                        <input type="hidden" name="userId" value="${request.user_id}" />
                                        <button type="submit" name="action" value="approve">Approve</button>
                                    </form>
                                    <form action="do.resolveuserrequest" method="POST" style="display:inline;">
                                        <input type="hidden" name="requestId" value="${request.request_id}" />
                                        <input type="hidden" name="userId" value="${request.user_id}" />
                                        <button type="submit" name="action" value="deny">Deny</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p>No pending delete requests.</p>
            </c:otherwise>
        </c:choose>

        <p><a href="do.homepage"><b>Back to Home</b></a></p>

        <jsp:include page="../include/footer.jsp" />
    </body>
</html>

