<%-- 
    Document   : viewotherlisting
    Created on : Nov 24, 2024, 7:20:12 PM
    Author     : Stephanie
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${sessionScope.loggedin.getUsername()}'s Listings</title>
        <link rel="stylesheet" href='css/lightmode.css'>
    </head>

    <jsp:include page="../../../include/header.jsp" />

    <body>

        <jsp:include page="../../../include/toggle.jsp" />

        <div class="sidebysidecontainer">
            <a href="../do.shop" class="message-button">Go to Shop</a>
            <a href="../index.jsp" class="message-button">Home</a>
        </div>

        <h1>${otherListings[1].getOwnername()}'s Listings</h1>

        <div class='product-list'>

            <c:choose>
                <c:when test ="${!otherListings.isEmpty()}">
                    <c:forEach var = "otherListing" items = "${otherListings}">
                        <div class='product-item'>
                            <h2>${otherListing.getTitle()}</h2>
                            <br>
                            <img 
                    src="${pageContext.request.contextPath}/images/listings/${otherListing.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                    alt="Uploaded Photo" 
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg';" 
                    width="50%" />
                            <br>
                            <br><a href = 'user/do.otherprofile?id=${otherListing.getOwnerid()}'> Owner: ${otherListing.getOwnername()}</a>
                            <p>Price: ₱ <fmt:formatNumber value="${otherListing.getPrice()}" pattern="0.00" /> </p>
                            <c:choose> 
                                <c:when test="${not empty otherListing.getTags()}">
                                    <p>Genre/s: ${otherListing.getTags()} </p>
                                </c:when>
                                <c:otherwise> 
                                    <p>Genre/s: No genres found.</p>
                                </c:otherwise>
                            </c:choose>

                            <p><b> ${otherListing.getStock()} left in stock </b></p>

                            <br><p>Listing posted on <fmt:formatDate value="${otherListing.getDateTime()}" pattern="MMMM dd, yyyy" /></p>

                            <div class='message-link'>
                                <a href='../do.itemview?id=${otherListing.getId()}' class='message-button'>View Listing</a>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class='product-list'>
                        <div class='product-item'>
                            <p>No listings found for ${otherListings[1].getOwnername()}</p>
                            <div class='message-link'>
                                <a href='../../../user/userprofile.jsp' class='message-button'>Go back to Profile</a>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

    </body>

    <jsp:include page="../../../include/footer.jsp" />

</html>
