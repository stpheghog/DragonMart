<%-- 
    Document   : createlisting
    Created on : 11 25, 24, 9:41:37 AM
    Author     : 221084
--%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Book Listing</title>
    <link rel="stylesheet" href="../css/lightmode.css">
</head>
<body>
    
    <jsp:include page="../../../include/header.jsp" />
    <jsp:include page="../../../include/toggle.jsp" />

    <div class="sidebysidecontainer">
        <a href="userprofile.jsp" class="message-button">Go back to Profile</a>
        <a href="../index.jsp" class="message-button">Home</a>
    </div>

    <div class="container">
        <h1>Create Book Listing</h1>

        <c:if test="${not empty param.error}">
            <p class="error">${param.error} </p>
        </c:if>

        <form action="do.createlisting" method="POST" enctype="multipart/form-data">
            <label for="title">Book Title:</label><br>
            <input type="text" name="title" id="title"><br><br>

            <c:if test="${not empty loggedin}">
                <input type="hidden" name="ownerid" value="${loggedin.id} ">
            </c:if>

            <label for="photo">Image:</label><br>
            <input type="file" id="photo" name="photo"><br><br>

            <label for="price">Price:</label><br>
            P<input type="number" step="0.01" name="price" id="price"><br><br>

            <label for="stock">Stock:</label><br>
            <input type="number" name="stock" id="stock"><br><br>

            <label for="description">Description:</label><br>
            <textarea name="description" id="description"></textarea><br><br>

            <label>Suggested Tags:</label><br>
            <input type="checkbox" name="genre" value="Fiction"> Fiction<br>
            <input type="checkbox" name="genre" value="Non-fiction"> Non-fiction<br>
            <input type="checkbox" name="genre" value="Fantasy"> Fantasy<br>
            <input type="checkbox" name="genre" value="Science Fiction"> Science Fiction<br>
            <input type="checkbox" name="genre" value="Mystery"> Mystery<br>
            <input type="checkbox" name="genre" value="Biography"> Biography<br>
            <input type="checkbox" name="genre" value="Historical"> Historical<br><br>

            <label for="customTags">Add other tags:</label><br>
            <input type="text" name="customTags" id="customTags" placeholder="Tag1,Tag2,Tag3"><br><br>

            <div class="message-link">
                <input type="submit" value="Create Listing" class="message-button"><br><br>
            </div>
        </form>
    </div>

    <jsp:include page="../../../include/footer.jsp" />
</body>
</html>
