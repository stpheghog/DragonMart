<%-- 
    Document   : itemview
    Created on : Nov 24, 2024, 4:25:45 PM
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
        <title>${listing.getTitle()}</title>
        <link rel="stylesheet" href='css/lightmode.css'>
    </head>

    <jsp:include page="../../../include/header.jsp" />

    <body>

        <jsp:include page="../../../include/toggle.jsp" />

        <div class="sidebysidecontainer">
            <a href="do.shop" class="message-button">Go to Shop</a>
            <a href="index.jsp" class="message-button">Home</a>
        </div>
        <br>
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

        <div class = 'allitemview'>
            <div class='item-view'>
                <div class='item-image'>
                    <img 
                    src="${pageContext.request.contextPath}/images/listings/${listing.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                    alt="Uploaded Photo" 
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg';" 
                    width="50%" />
                             
                             
                </div>
                <div class='item-details'>
                    <h2>${listing.getTitle()}</h2>
                    <a href = 'user/do.otherprofile?id=${listing.getOwnerid()}'> Owner: ${listing.getOwnername()}</a>
                    <p>Price: ₱ <fmt:formatNumber value="${listing.getPrice()}" pattern="0.00" /> </p>
                    <c:choose> 
                        <c:when test="${not empty listing.getTags()}">
                            <p>Genre/s: ${listing.getTags()} </p>
                        </c:when>
                        <c:otherwise> 
                            <p>Genre/s: No genres found.</p>
                        </c:otherwise>
                    </c:choose>
                    <p><b> ${listing.getStock()} left in stock </b></p>
                    <p> Description: ${listing.getDesc()} </p>
                    <br><p>Listing posted on <fmt:formatDate value="${listing.getDateTime()}" pattern="MMMM dd, yyyy" /></p>

                    <c:choose> 
                        <c:when test="${not empty sessionScope.loggedin && sessionScope.loggedin.getId() != listing.getOwnerid()}">
                            <div class='message-link'>
                                <a href='user/do.purchaserequest?listingid=${listing.getId()}' class='message-button'>Purchase Item</a>
                            </div>
                        </c:when>
                    </c:choose>
                </div>
            </div>

            <div class = 'allcommentview'>
                <div class = 'comments-container'>
                    <h3>Comments</h3>

                    <c:choose>
                        <c:when test="${!comment.isEmpty()}"> 
                            <c:forEach var = "c" items = "${comment}">
                                <div class="comment-action">
                                    <c:choose>
                                        <c:when test="${c.getUserid() == sessionScope.loggedin.getId()}">
                                            <a href='user/do.removecomment?id=${c.getId()}&listingid=${listing.getId()}' class='remove-link'>Remove</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href='user/do.reportabuse?id=${c.getId()}&listingid=${listing.getId()}' class='report-link'>Report Abuse</a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class = 'comment'>
                                    <p><b>${c.getUsername()}:</b> ${c.getComment()}</p>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class = 'container'>
                                <h3> This listing has no comments </h3>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class='comments-input'>
                    <form method='POST' action='user/do.savecomment'>
                        <input type='hidden' name='id' value='${listing.getId()}'>
                        <input type='hidden' name='listing' value='${listing.getTitle()}'>
                        <div class='comment-textfield'>
                            <textarea name='comment' placeholder='Write a comment...'></textarea>
                        </div>
                        <input type='submit' value='Comment'>
                    </form>
                </div>
            </div>
        </div>
    </body>

    <jsp:include page="../../../include/footer.jsp" />

</html>
