<%-- 
    Document   : canceledsale
    Created on : Nov 27, 2024, 9:18:29 AM
    Author     : Stephanie
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Canceled Sales</title>
        <link rel="stylesheet" href="css/lightmode.css">
    </head>
    <jsp:include page="../../../include/header.jsp" />

    <body>
        <jsp:include page="../../../include/toggle.jsp" />

        <div class="sidebysidecontainer">
            <a href="transactionview.jsp" class="message-button">Go back to Transactions</a>
            <a href="../index.jsp" class="message-button">Home</a>
        </div>

        <div class="container">

            <c:choose>
                <c:when test="${not empty param.errmsg}">
                    <p class = 'error'>${param.errmsg}<p>
                    </c:when>
                </c:choose>
            <h2>Viewing Canceled Sales</h2>
            <div class = 'container'>
                <c:forEach var="i" items="${cancList}">
                    <div class='approveitem'>
                        <br>ID: ${i.getId()}<h2>
                            Seller name: ${i.getOwnerName()}
                            <br> Buyer name: ${i.getBuyerName()}<br>
                            <br>Listing: <a href='../do.itemview?id=${i.getListingId()}'>${i.getListingName()}</a></h2>
                    </div>
                </c:forEach>
            </div>
        </div>
    </body>

    <jsp:include page="../../../include/footer.jsp" />
</html>
