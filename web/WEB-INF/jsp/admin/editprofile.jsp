<%-- 
    Document   : editprofile
    Created on : 27 Nov 2024, 5:31:24 pm
    Author     : blonyagoncillo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Edit Profile</title>
        <link rel=stylesheet href="${pageContext.request.contextPath}/css/admin.css">
        <style>
            textarea { width: 400px; height: 150px; resize: none; }
        </style>
    </head>


    <body>
        <jsp:include page="../../../include/adminheader.jsp"/>  
        <c:choose>
            <c:when test="${not empty errmsg}">
                <p class = 'error'>${errmsg}<p>
                </c:when>
            </c:choose>

        <div class="container">

            <h2>Edit Profile</h2><br>

            <c:if test="${not empty currentUser}">
                <form action='do.saveprofile' method='POST' enctype='multipart/form-data'>
                    <input type='hidden' name='id' value='${userId}'>
                    <input type='hidden' name='oguser' value='${currentUser.username}'>

                    <img 
                    src="${pageContext.request.contextPath}/images/user/${currentUser.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                    alt="Uploaded Photo" 
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg?timestamp=${System.currentTimeMillis()}';" 
                    width="50%" />

                    <br><label for='username'>Username: </label>
                    <input type='text' id='username' name='username' value='${currentUser.username}' required><br>

                    <br><br><label for='email'>Email: </label>
                    <input type='text' id='email' name='email' value='${currentUser.email}' required><br>

                    <br><br><label for='firstname'>First Name: </label>
                    <input type='text' id='firstname' name='firstname' value='${currentUser.firstname}' required><br>

                    <br><br><label for='lastname'>Last Name: </label>
                    <input type='text' id='lastname' name='lastname' value='${currentUser.lastname}' required><br>

                    <br><br>Date of Birth:
                    ${currentUser.dob}<br>
                    <p><i>Birth date cannot be edited</i></p>

                    <br><br><label for='aboutme'>About Me:</label><br>
                    <textarea id='aboutme' name='aboutme' required>${currentUser.aboutme}</textarea><br>

                    <br><br><label for='contact'>Message Link: (Facebook, Instagram, etc.) </label>
                    <input type='text' id='contact' name='contact' value='${currentUser.contact}' required><br>

                    <br><br><input type='submit' value='Update Information'>
                </c:if>
            </form>

            <div class='message-link'>
                <br> <br> <a href="do.viewallusers"> Go back </a><br>
            </div>

        </div>

        <jsp:include page="../include/footer.jsp"/>
    </body>


</html>
