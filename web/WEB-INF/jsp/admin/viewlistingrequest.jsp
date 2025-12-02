<%-- 
    Document   : viewlistingrequest
    Created on : 25 Nov 2024, 12:34:09 am
    Author     : blonyagoncillo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Pending Delete Listing Requests</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        button {
            margin: 0 5px;
        }
    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css"/>
</head>
<body>
    <jsp:include page="../../../include/adminheader.jsp" />
    <h2>Pending Delete Listing Requests</h2>

    <c:if test="${not empty error}">
        <div style="color: red;">
            <p>${error}</p>
        </div>
    </c:if>

    <table>
        <thead>
            <tr>
                <th>Request ID</th>
                <th>Admin</th>
                <th>Listing IDs</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="request" items="${deleteRequests}">
                <tr>
                    <td>${request.requestId}</td>
                    <td>${request.adminId}</td>
                    <td>
                        <c:forEach var="listingId" items="${request.listingIds}">
                            <a href="do.listingview?id=${listingId}">${listingId}</a><br />
                        </c:forEach>
                    </td>
                    <td>
                        <form action="do.resolvelistingrequest" method="POST" style="display:inline;">
                            <input type="hidden" name="requestId" value="${request.requestId}" />
                            <input type="hidden" name="listingId" value="${request.listingIds[0]}" />
                            <button type="submit" name="action" value="approve">Approve</button>
                        </form>
                        <form action="do.resolvelistingrequest" method="POST" style="display:inline;">
                            <input type="hidden" name="requestId" value="${request.requestId}" />
                            <input type="hidden" name="listingId" value="${request.listingIds[0]}" />
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

