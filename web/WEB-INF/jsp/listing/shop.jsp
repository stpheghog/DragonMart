<%-- 
    Document   : shop
    Created on : Nov 21, 2024, 9:34:58 AM
    Author     : Stephanie
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Shop</title>
        <link rel="stylesheet" href='css/lightmode.css'>
    </head>

    <jsp:include page="../../../include/header.jsp" />

    <body>

        <jsp:include page="../../../include/toggle.jsp" />

        <a href='index.jsp' class='message-button'>Home</a>

        <c:choose>
            <c:when test="${not empty param.errmsg}">
                <p class = 'error'>${param.errmsg}<p>
                </c:when>
            </c:choose>

        <div class='sidebysideshopcontainer'>
            <div class='categorycontainer'>
                <h2>Filter listings... <br><br>
                    Order by:<br>
                    <a href = 'do.category?type=date_time&order=ASC'>Newest → Oldest</a><br>
                    <a href = 'do.category?type=date_time&order=DESC'>Oldest → Newest</a><br>
                    <a href = 'do.category?type=price&order=ASC'>Cheap → Expensive</a><br>
                    <a href = 'do.category?type=price&order=DESC'>Expensive → Cheap</a><br>
                    <a href = 'do.category?type=alphabet&order=ASC'>A → Z</a><br>
                    <a href = 'do.category?type=alphabet&order=DESC'>Z → A</a><br>

                    <br>Filter using tags: <br></h2>

                <div class='tagscontainer'>
                    <form action = 'do.category' method = 'post'>
                        <c:choose>
                            <c:when test="${not empty tagList}">
                                <c:forEach var = "i" items = "${tagList}">
                                    <label><input type="checkbox" name="tag" value="${i.getName()}"> ${i.getName()} </label><br>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                <p>No tags currently in the shop</p>
                            </c:otherwise>
                        </c:choose>

                </div>
                <input type = submit value = 'Filter listings...'>
                </form>
            </div>
            
            <%-- mobile filtering --%>

            <div id="mobile-filter">
                <h2 class = "filter-head">Filter listings... </h2>
                <div id="mobfilter-list"><h2>
                        Order by:<br>

                        <c:choose>
                            <c:when test="${not empty query}">
                                <a href = 'do.category?query=${query}&type=date_time&order=ASC'>Newest → Oldest</a><br>
                                <a href = 'do.category?query=${query}&type=date_time&order=DESC'>Oldest → Newest</a><br>
                                <a href = 'do.category?query=${query}&type=price&order=ASC'>Cheap → Expensive</a><br>
                                <a href = 'do.category?query=${query}&type=price&order=DESC'>Expensive → Cheap</a><br>
                                <a href = 'do.category?query=${query}&type=alphabet&order=ASC'>A → Z</a><br>
                                <a href = 'do.category?query=${query}&type=alphabet&order=DESC'>Z → A</a><br>
                            </c:when>
                            <c:otherwise>
                                <a href = 'do.category?type=date_time&order=ASC'>Newest → Oldest</a><br>
                                <a href = 'do.category?type=date_time&order=DESC'>Oldest → Newest</a><br>
                                <a href = 'do.category?type=price&order=ASC'>Cheap → Expensive</a><br>
                                <a href = 'do.category?type=price&order=DESC'>Expensive → Cheap</a><br>
                                <a href = 'do.category?type=alphabet&order=ASC'>A → Z</a><br>
                                <a href = 'do.category?type=alphabet&order=DESC'>Z → A</a><br>
                            </c:otherwise>
                        </c:choose>
                        <br>Filter using tags: <br></h2>

                        <div class='tagscontainer'>
                            <form action = 'do.category' method = 'post'>
                                <if test="${not empty query}">
                                    <input type ="hidden" value ="${query}" name ="query" id="query">
                                </if>
                                <c:choose>
                                    <c:when test="${not empty tagList}">
                                        <c:forEach var="i" items="${tagList}">
                                            <c:set var="isChecked" value="false" />
                                            <c:forEach var="j" items="${checkedTag}">
                                                <c:if test="${j == i.getName()}">
                                                    <c:set var="isChecked" value="true" />
                                                </c:if>
                                            </c:forEach>
                                            <label>
                                                <input type="checkbox" name="tag" value="${i.getName()}" 
                                                       <c:if test="${isChecked}">checked="checked"</c:if>>
                                                ${i.getName()}
                                            </label>
                                            <br>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <p>No tags currently in the shop</p>
                                    </c:otherwise>
                                </c:choose>
                        </div>
                        <input type = submit value = 'Filter listings...'>
                        </form>
                    </div>
            </div>

            <%-- mobile filtering --%>

            <div class='shopcontainer'>
                <c:choose>
                    <c:when test="${not empty availableListings}">
                        <div class='product-list'>
                            <c:forEach var = "i" items = "${availableListings}">
                                <div class='product-item'>
                                    <h2>${i.getTitle()}</h2>

                                    <img 
                                        src="${pageContext.request.contextPath}/images/listings/${i.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                                        alt="Uploaded Photo" 
                                        onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg';" 
                                        width="50%" />
                                    <br>
                                    <br><a href = 'user/do.otherprofile?id=${i.getOwnerid()}'> Owner: ${i.getOwnername()}</a>
                                    <p>Price: ₱ <fmt:formatNumber value="${i.getPrice()}" pattern="0.00" /> </p>
                                    <c:choose> 
                                        <c:when test="${not empty i.getTags()}">
                                            <p>Genre/s: ${i.getTags()} </p>
                                        </c:when>
                                        <c:otherwise> 
                                            <p>Genre/s: No genres found.</p>
                                        </c:otherwise>
                                    </c:choose>
                                    <p><b> ${i.getStock()} left in stock </b></p>

                                    <br><p>Listing posted on <fmt:formatDate value="${i.getDateTime()}" pattern="MMMM dd, yyyy" /></p>

                                    <div class='message-link'>
                                        <a href='do.itemview?id=${i.getId()}' class='message-button'>View Listing</a>
                                    </div>
                                    
                                    <c:if test="${not empty sessionScope.loggedin}">
                                        <c:choose>
                                            <c:when test="${sessionScope.loggedin.getId() != i.getOwnerid()}">
                                                <div class='message-link'>
                                                    <a href='user/do.purchaserequest?listingid=${i.getId()}' class='message-button'>Purchase Item</a>
                                                </div>
                                            </c:when>
                                        </c:choose>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class = 'container'>
                            <h1> Sorry! No items available in the shop. </h1>
                        </div>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>

        <script>
            document.getElementById("mobile-filter").addEventListener("click", function (event) {
                if (!event.target.closest('.tagscontainer')) {
                    this.classList.toggle("open");
                    document.querySelector("#mobfilter-list").classList.toggle("open");
                }
            });
        </script>

    </body>

    <jsp:include page="../../../include/footer.jsp" />

</html>
