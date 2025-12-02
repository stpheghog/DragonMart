<%-- 
    Document   : editlisting
    Created on : Nov 27, 2024, 5:28:03 PM
    Author     : lucyallysongarcia
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Modify Listing</title>
        <link rel="stylesheet" href="../css/lightmode.css">
    </head>
    <body>
        <jsp:include page="../../../include/header.jsp" />
        <jsp:include page="../../../include/toggle.jsp" />

        <div class ="sidebysidecontainer">
            <a href='userprofile.jsp' class='message-button'>Go back to Profile</a>
            <a href='../index.jsp' class='message-button'>Home</a>
        </div>

        <div class='container'>
            <h2>Edit Listing</h2>
            <c:if test="${not empty param.msg}">
                <p class="error">${param.msg} </p>
            </c:if>

            <form action='do.savelisting' method='POST' enctype='multipart/form-data'>
                <label for='title'>Title:</label>
                <input type='text' id='title' name='title' value="${listing.title}" required><br><br>


                <p>Original image:</p>
                
                <img 
                    src="${pageContext.request.contextPath}/images/listings/${listing.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                    alt="Uploaded Photo" 
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg';" 
                    width="50%" />
                <br>
                <input type='hidden' name='id' value="${listing.id}" required>

                <br><label for='photo'>Photo:</label>
                <input type='file' id='photo' name='photo'><br><br>
                
                Stock:
                ${listing.stock}<br><br>

                <label for='price'>Price:</label>
                ₱ <input type='number' step=\"0.01\" id='price' name='price' value="${listing.price}" required><br><br>

                <label for='desc'>Description:</label>
                <textarea id='desc' name='desc' required> ${listing.desc} </textarea><br><br>

                <label>Suggested Tags:</label><br>


                <c:forEach var="i" items="${genre}">
                    <c:set var="tagFound" value="false" scope="page"/>

                    <c:forEach var="tagss" items="${tags}">

                        <c:if test="${i eq tagss }"> 
                            <c:set var="tagFound" value="true" scope="page"/>
                        </c:if>                        
                    </c:forEach>

                    <c:if test="${tagFound}"> 
                        <input type="checkbox" name="genre" value="${i}" checked> ${i}<br> <br>
                    </c:if>

                    <c:if test="${!tagFound}"> 
                        <input type="checkbox" name="genre" value="${i}"> ${i}<br> <br>
                    </c:if>

                </c:forEach>

                <c:forEach var="ctags" items="${tags}">
                    <c:if test="${!fn:contains(fn:join(genre, ','),ctags)}">
                        <input type="checkbox" name="genre" value="${ctags}" checked> ${ctags}<br> <br>
                    </c:if>
                </c:forEach>

                Add other tags:<br>(separate by commas if multiple eg. Tag1,Tag2,etc.):<br>
                <input type="text" name="customTags"><br><br>

                <input type='submit' value='Save Changes'>
            </form>
        </div>

        <jsp:include page="../../../include/footer.jsp" />

    </body>
</html>
