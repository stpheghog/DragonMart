<%-- 
    Document   : viewlisting
    Created on : Nov 24, 2024, 6:15:35 PM
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

        <h1>${sessionScope.loggedin.getUsername()}'s Listings</h1>

        <c:choose>
            <c:when test="${not empty param.errmsg}">
                <p class = 'error'>${param.errmsg}<p>
                </c:when>
            </c:choose>

            <c:choose>
                <c:when test="${not empty param.msg}">
                <p class = 'error'>${param.msg}<p>
                </c:when>
            </c:choose>

        <form method='post' action='do.removelisting'>
            <div class='product-list'>

                <c:choose>
                    <c:when test ="${!ownerListings.isEmpty()}">
                        <c:forEach var = "ownerlisting" items = "${ownerListings}">
                            <div class='product-item'>
                                <input type='checkbox' name='removeListing' value='${ownerlisting.getId()}'>
                                <h2>${ownerlisting.getTitle()}</h2>
                                <br>
                                <img 
                    src="${pageContext.request.contextPath}/images/listings/${ownerlisting.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                    alt="Uploaded Photo" 
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg';" 
                    width="50%" />
                                <br>
                                <br><a href = 'do.otherprofile?id=${ownerlisting.getOwnerid()}'> Owner: ${sessionScope.loggedin.getUsername()}</a>
                                <p>Price: ₱ <fmt:formatNumber value="${ownerlisting.getPrice()}" pattern="0.00" /> </p>
                                <c:choose> 
                                    <c:when test="${not empty ownerlisting.getTags()}">
                                        <p>Genre/s: ${ownerlisting.getTags()} </p>
                                    </c:when>
                                    <c:otherwise> 
                                        <p>Genre/s: No genres found.</p>
                                    </c:otherwise>
                                </c:choose>

                                <p><b> ${ownerlisting.getStock()} left in stock </b></p>

                                <br><p>Listing posted on <fmt:formatDate value="${ownerlisting.getDateTime()}" pattern="MMMM dd, yyyy" /></p>

                                <div class='message-link'>
                                    <a href='../do.itemview?id=${ownerlisting.getId()}' class='message-button'>View Listing</a>
                                </div>
                                <div class='message-link'>
                                    <a href='do.editlisting?id=${ownerlisting.getId()}' class='message-button'>Modify Listing</a>
                                </div>
                                <div class='message-link'>
                                    <a href='do.editstock?id=${ownerlisting.getId()}' class='message-button'>Edit Stock</a>
                                </div><br>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class='product-list'>
                            <div class='product-item'>
                                <p>No listings found for ${sessionScope.loggedin.getUsername()}</p>
                                <div class='message-link'>
                                    <a href='../user/userprofile.jsp' class='message-button'>Go back to Profile</a>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test ="${!ownerListings.isEmpty()}">
                    </div>
                    <input type='submit' value='Remove Listings' class='remove-button' />
                </form>
            </c:when>
        </c:choose>
    </div>

</body>

<jsp:include page="../../../include/footer.jsp" />

</html>
