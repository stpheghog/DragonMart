<%-- 
    Document   : editprofile
    Created on : Nov 21, 2024, 6:29:23 AM
    Author     : Stephanie
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Edit Profile</title>
        <link rel="stylesheet" href='css/lightmode.css'>
        <style>
            textarea { width: 400px; height: 150px; resize: none; }
        </style>
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

            <h2>Edit Profile</h2><br>

            <form action="../user/do.saveprofile" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${sessionScope.loggedin.getId()}">
                <input type="hidden" name="oguser" value="${sessionScope.loggedin.getUsername()}">

                <br><br><b>Current Profile Photo: </b><br>
                <img 
                    src="${pageContext.request.contextPath}/images/user/${sessionScope.loggedin.id}_image.jpg?timestamp=${System.currentTimeMillis()}"
                    alt="Uploaded Photo" 
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/empty.jpg';" 
                    width="50%" />
                
                <br><br><label for="photo">Profile Photo: </label>
                <input type="file" id="photo" name="photo"><br><br>
                
                <label for="username">Username: </label>
                <input type="text" id="username" name="username" value="${sessionScope.loggedin.getUsername()}" required><br>

                <br><br><label for="email">Email: </label>
                <input type="text" id="email" name="email" value="${sessionScope.loggedin.getEmail()}" required><br>

                <br><br><label for="firstname">First Name: </label>
                <input type="text" id="firstname" name="firstname" value="${sessionScope.loggedin.getFirstname()}" required><br>

                <br><br><label for="lastname">Last Name: </label>
                <input type="text" id="lastname" name="lastname" value="${sessionScope.loggedin.getLastname()}" required><br>

                <br><br>Date of Birth:
                ${sessionScope.loggedin.getDob()}<br>
                <p><i>Birthdate cannot be edited</i></p>

                <br><br><label for="aboutme">About Me:</label><br>
                <textarea id="aboutme" name="aboutme" required>${sessionScope.loggedin.getAboutme()}</textarea><br>

                <br><br><label for="contact">Message Link: (Facebook, Instagram, etc.) </label>
                <input type="text" id="contact" name="contact" value="${sessionScope.loggedin.getContact()}" required><br>

                <br><br><input type="submit" value="Update Information">

            </form>

            <div class="message-link">
                <br> <br> <a href="user.jsp"> Go back </a><br>
            </div>

        </div>
    </body>

    <jsp:include page="../include/footer.jsp" />

</html>
