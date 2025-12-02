<%-- 
    Document   : transactionview
    Created on : Nov 27, 2024, 8:59:40 AM
    Author     : Stephanie
--%>

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
            <a href="userprofile.jsp" class="message-button">Go back to Profile</a>
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
                <a href='purchasesview.jsp' class='message-button'>View Ongoing Transactions</a>
                <br><br><a href='cancelledview.jsp' class='message-button'>View Canceled Transactions</a>
                </div>
        </div>
    </body>

    <jsp:include page="../include/footer.jsp" />
</html>
