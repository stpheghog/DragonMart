<%-- 
    Document   : viewlistings
    Created on : 25 Nov 2024, 12:31:35 am
    Author     : blonyagoncillo
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>View All Listings</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    </head>
    <body>

        <jsp:include page="../../../include/adminheader.jsp" />

        <h1>All Shop Listings:</h1>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>

        <c:choose>
            <c:when test="${not empty ownerListings}">
                <form method="post" action="do.removelisting">
                    <div class="product-list">
                        <c:forEach var="listing" items="${ownerListings}">
                            <div class="product-item">
                                <input type="checkbox" name="removeListing" value="${listing.id}" />
                                <h2>${listing.title}</h2>

                                <img 
                                    src="${pageContext.request.contextPath}/images/listings/${listing.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                                    alt="Uploaded Photo"
                                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg';" 
                                    width="50%" />

                                <p>Owner: <a href="do.adminother?id=${listing.ownerID}">${listing.ownername}</a></p>
                                <p>Price: ₱${listing.price}</p>
                                <p>Description: ${listing.desc}</p>
                                <p>Genres: <c:out value="${listing.tags}" /></p>
                                <p><b>${listing.stock} left in stock</b></p>
                                <p>Listing posted on ${listing.dateTime}</p>

                                <a href="do.listingview?id=${listing.id}" class="message-button">View Listing</a>
                                <a href="do.editlisting?id=${listing.id}" class="message-button">Modify Listing</a>
                            </div>
                        </c:forEach>
                    </div>
                    <input type="submit" value="Remove Listings" class="remove-button" />
                </form>
            </c:when>
            <c:otherwise>
                <p>No listings found.</p>
                <a href="do.homepage" class="message-button">Go back to Profile</a>
            </c:otherwise>
        </c:choose>

        <a href="do.homepage" class="message-button">Home</a>

        <jsp:include page="../include/footer.jsp" />
    </body>
</html>
