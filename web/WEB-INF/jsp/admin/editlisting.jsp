<%-- 
    Document   : editlisting
    Created on : 29 Nov 2024, 11:14:26 am
    Author     : blonyagoncillo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Edit Listing</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    </head>
    <body>
        <div class="container">
           
            <jsp:include page="../../../include/adminheader.jsp" />

            <h2>Edit Listing</h2>

            <!-- Form to edit listing -->
            <form action="do.savelisting" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${listing.id}">
                <input type="hidden" name="ogtitle" value="${listing.title}">

                <label for="title">Title:</label>
                <input type="text" id="title" name="title" value="${listing.title}" required><br><br>

                <!-- Displaying the image -->
                <img 
                    src="${pageContext.request.contextPath}/images/listings/${listing.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                    alt="Uploaded Photo" 
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg?timestamp=${System.currentTimeMillis()}';" 
                    width="50%" />


                <br><label for="owner">Owner: ${listing.ownername}</label>
                <input type="hidden" id="owner" name="owner" value="${listing.ownername}"><br><br>


                <p>Price: ₱${listing.price}</p>


                <label for="desc">Description:</label>
                <textarea id="desc" name="desc" required>${listing.desc}</textarea><br>

                <c:choose> 
                    <c:when test="${not empty listing.getTags()}">
                        <p>Genre/s: ${listing.getTags()} </p>
                    </c:when>
                    <c:otherwise> 
                        <p>Genre/s: No genres found.</p>
                    </c:otherwise>
                </c:choose>

                <p>Status: ${listing.status}</p>


                <input type="submit" value="Save Changes">
            </form>
        </div>


        <jsp:include page="../include/footer.jsp" />
    </body>
</html>


