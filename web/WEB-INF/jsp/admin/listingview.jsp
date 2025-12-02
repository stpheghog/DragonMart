<%-- 
    Document   : listingview
    Created on : 26 Nov 2024, 7:52:07 pm
    Author     : blonyagoncillo
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
    <head>
        <title>View Item</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    </head>
    <body>
        <jsp:include page="../../../include/adminheader.jsp" />

        <h1>Listing Details</h1>

        <c:if test="${not empty listing}">

            <div class="allitemview">
                <div class="item-view">

                    <!-- Item Image -->
                    <div class="item-image">
                        <<img 
                    src="${pageContext.request.contextPath}/images/listings/${listing.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                    alt="Uploaded Photo" 
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg';" 
                    width="50%" />
                    </div>

                    <!-- Item Details -->
                    <div class="item-details">
                        <h2>${listing.title}</h2>
                        <a href="do.adminother?id=${listing.ownerID}">Owner: ${listing.ownername}</a>
                        <p>Stock: ${listing.stock} left in stock</p>
                        <p>Price: ₱ ${listing.price}</p>
                        <p>Description: ${listing.desc}</p>

                        <!-- Tags -->
                        <c:choose> 
                            <c:when test="${not empty listing.getTags()}">
                                <p>Genre/s: ${listing.getTags()} </p>
                            </c:when>
                            <c:otherwise> 
                                <p>Genre/s: No genres found.</p>
                            </c:otherwise>
                        </c:choose>

                        <!-- Posting Date -->
                        <p>Listing posted on ${listing.dateTime}</p>
                    </div>
                </div>

                <!-- Comments Section -->
                <c:if test="${not empty comments}">
                    <div class="allcommentview">
                        <div class="comments-container">
                            <h3>Comments</h3>
                            <c:forEach var="comment" items="${comments}">
                                <div class="comment">
                                    <!-- Remove Comment Option for Admin -->
                                    <c:if test="${admin != null}">
                                        <a href="do.removecomment?id=${comment.id}&listingid=${listing.id}" 
                                           style="color: #AD2831; text-decoration: none; margin-left: auto;">
                                            Remove
                                        </a>
                                    </c:if>
                                    <p><b>${comment.username}:</b> ${comment.comment}</p>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>

                <!-- No Comments -->
                <c:if test="${empty comments}">
                    <div class="container">
                        <p>This listing has no comments.</p>
                    </div>
                </c:if>
            </div>
        </c:if>

        <a href="do.homepage" class="message-button">Go back to Home</a>

        <jsp:include page="../include/footer.jsp" />
    </body>
</html>
