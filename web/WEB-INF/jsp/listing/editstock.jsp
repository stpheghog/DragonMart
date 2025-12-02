<%-- 
    Document   : editstock
    Created on : Nov 30, 2024, 5:26:27 PM
    Author     : Stephanie
--%>

<%-- 
    Document   : editlisting
    Created on : Nov 27, 2024, 5:28:03 PM
    Author     : lucyallysongarcia
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Modify Listing</title>
        <link rel="stylesheet" href="../css/lightmode.css">
    </head>
    <body>
        <jsp:include page="../../../include/header.jsp" />
        <jsp:include page="../../../include/toggle.jsp" />

        <div class ="sidebysidecontainer">
            <a href='userprofile.jsp' class='message-button'>Go back to Profile</a>
            <a href='../index.jsp' class='message-button'>Home</a>
        </div>

        <div class='container'>
            <h2>Edit Stock for ${listing.title}</h2>
            <c:if test="${not empty param.msg}">
                <p class="error">${param.msg} </p>
            </c:if>

            <form action='do.savestock' method='POST'>
                
                <img 
                    src="${pageContext.request.contextPath}/images/listings/${listing.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                    alt="Uploaded Photo" 
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg';" 
                    width="50%" />
                <br>
                <input type='hidden' name='id' value="${listing.id}" required>
                
                <br><label for='stock'>Stock:</label><br>
                <input type='number' id='stock' name='stock' value="${listing.stock}" required><br><br>
                <input type='submit' value='Save Changes'>
                
            </form>
        </div>

        <jsp:include page="../../../include/footer.jsp" />

    </body>
</html>
