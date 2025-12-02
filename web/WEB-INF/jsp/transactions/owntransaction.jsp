<%-- 
    Document   : owntransaction
    Created on : Nov 27, 2024, 9:18:06 AM
    Author     : Stephanie
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Viewing Sales</title>
        <link rel="stylesheet" href="css/lightmode.css">
    </head>

    <jsp:include page="../../../include/header.jsp" />

    <body>

        <jsp:include page="../../../include/toggle.jsp" />

        <div class="sidebysidecontainer">
            <a href="transactionview.jsp" class="message-button">Go back to Transactions</a>
            <a href="../index.jsp" class="message-button">Home</a>
        </div>
        <div class='container'>
            <h1>These are all your sales!</h1>
            <p>In this page you can see all the items other dragons want to buy from you!</p>
            <p>If you want to make a sale, first accept the request, deliver the item, receive your payment and mark it as paid, and the sale will be completed</p>
            <p>However, if you change your mind you can cancel the sale at any time!</p>
        </div>

        <c:if test="${not empty param.errmsg}">
            <p class = 'error'>${param.errmsg}<p>
            </c:if>

            <c:if  test="${not empty param.msg}">
            <p class = 'error'>${param.msg}<p>
            </c:if>

        <div class='sidebysidecontainer'>
            <div class='transcontainer'>
                <h2>Step 1: Accept or Deny Sale Request</h2>
                <c:forEach var="i" items="${reqList}">
                    <div class='approveitem'>
                        <br>ID: ${i.getId()}<h2>
                            Seller name: ${i.getOwnerName()}
                            <br> Buyer name: ${i.getBuyerName()}<br>
                            <br>Listing: <a href='../do.itemview?id=${i.getListingId()}'>${i.getListingName()}</a></h2>
                    </div>
                    <form method='post' action='do.updatetransaction'>
                        <input type='hidden' value='Sale' name='type'>
                        <input type='hidden' value='${i.getId()}' name='id'>
                        <input type='hidden' value='Requested' name='viewaction'>
                        <div class='sidebysidecontainer'>
                            <input type='submit' value='Accept' name='action'>
                            <input type='submit' value='Deny' name='action'>
                        </div>
                    </form><br>
                </c:forEach>
            </div>
            <div class='transcontainer'>
                <h2>Step 2: Mark the Sale as "Payment Received"</h2>
                <c:forEach var="i" items="${pendList}">
                    <div class='approveitem'>
                        <br>ID: ${i.getId()}<h2>
                            Seller name: ${i.getOwnerName()}
                            <br>Listing: <a href='../do.itemview?id=${i.getListingId()}'>${i.getListingName()}</a></h2>
                    </div>
                    <form method='post' action='do.updatetransaction'>
                        <input type='hidden' value='Sale' name='type'>
                        <input type='hidden' value='${i.getId()}' name='id'>
                        <input type='hidden' value='Pending' name='viewaction'>
                        <div class='sidebysidecontainer'>
                            <c:choose>
                                <c:when test="${!i.isIsPaid()}">
                                    <input type='submit' value='Paid' name='action'>
                                    <input type='submit' value='Cancel' name='action'>
                                </c:when>
                                <c:otherwise>
                                    Waiting for item to be received by buyer...<br>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </form><br>
                </c:forEach>
            </div>
            <div class='transcontainer'>
                <h2>Step 3: Completed Sales</h2>
                <c:forEach var="i" items="${compList}">
                    <div class='approveitem'>
                        <br>ID: ${i.getId()}<h2>
                            Seller name: ${i.getOwnerName()}
                            <br>Listing: <a href='../do.itemview?id=${i.getListingId()}'>${i.getListingName()}</a></h2>
                    </div>
                </c:forEach>
            </div>
        </div>
    </body>

    <jsp:include page="../../../include/footer.jsp" />
</html>
