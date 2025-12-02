<%-- 
    Document   : purchasesview
    Created on : Nov 27, 2024, 9:15:14 AM
    Author     : Stephanie
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Transaction View</title>
        <link rel="stylesheet" href="css/lightmode.css">
    </head>
    <jsp:include page="../include/header.jsp" />

    <body>
        <jsp:include page="../include/toggle.jsp" />

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
            <h2>Viewing Transaction Functions</h2>
            <div class = 'container'>
                <a href='do.viewtransaction?action=Canceled&type=Purchase' class='message-button'>View Canceled Purchases</a>
                <br><br><a href='do.viewtransaction?action=Canceled&type=Sale' class='message-button'>View Canceled Sales</a>
            </div>
        </div>
    </body>

    <jsp:include page="../include/footer.jsp" />
</html>
