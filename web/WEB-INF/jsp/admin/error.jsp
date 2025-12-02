<%-- 
    Document   : error
    Created on : 24 Nov 2024, 12:17:25 am
    Author     : blonyagoncillo
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <jsp:include page="../../../include/adminheader.jsp"/>
    <h1>Error Occurred</h1>
    <p class="error">${error}</p>
    
    <c:if test="${not empty path}">
        <a href="${path}">Go back</a>
    </c:if>
    
    <a href="do.homepage">Go to Admin Home</a>
        
</body>
</html>

