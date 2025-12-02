<%-- 
    Document   : header
    Created on : 5 Oct 2024, 6:02:22 pm
    Author     : blonyagoncillo
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <header>
        <nav class="topnav">
            <ul class="nav-list">
                <li class="logo">
                    <img class="logo-img" src="${pageContext.request.contextPath}/images/logo.png" alt="logo">
                    <p class="logo-text">DragonMart</p>
                </li>
                <li class="main-links"><a href="${pageContext.request.contextPath}/index.jsp" class="home">Home</a>
                    <a href="${pageContext.request.contextPath}/do.shop">Shop</a>
                <div class="search-container">
                    <form action="${pageContext.request.contextPath}/do.category" class="search-form">
                        <input type="text" placeholder="Search.." name="query" class="search-input">
                        <button type="submit" class="search-button">
                            <img src="${pageContext.request.contextPath}/images/search-icon.png" alt="Search" class="search-icon">
                        </button>
                    </form>
                </div>
                </li>
                <li class="auth-links">
                    <c:choose>
                        <c:when test="${empty sessionScope.loggedin || sessionScope.loggedin == null}">
                            <a href="${pageContext.request.contextPath}/login.jsp" class="rightnav">Login</a>
                            <a href="${pageContext.request.contextPath}/signup.jsp" class="rightnav">Sign up</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/user/userprofile.jsp" class="rightnav">${sessionScope.loggedin.getUsername()}</a>
                            <a href="${pageContext.request.contextPath}/user/do.logout" class="rightnav">Logout</a>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </nav>

        <%--MOBILE ADAPTIVE NAB STARTS HERE--%>
        <div id="mobile-nav">
            <div class="logo">
                <img class="logo-img" src="${pageContext.request.contextPath}/images/logo.png" alt="logo">
            </div>
            <div class="search-container">
                <form action="${pageContext.request.contextPath}/do.category" class="search-form">
                    <input type="text" placeholder="Search.." name="query" class="search-input">
                    <button type="submit" class="search-button">
                        <img src="${pageContext.request.contextPath}/images/search-icon.png" alt="Search" class="search-icon">
                    </button>
                </form>
            </div>

            <div id="hamburger-nav">
                <div class="bar1"></div>
                <div class="bar2"></div>
                <div class="bar3"></div>

                <ul class="mobile-menu">
                    <li><a href="${pageContext.request.contextPath}/index.jsp" class="home">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/do.shop">Shop</a></li>
                        <c:choose>
                            <c:when test="${empty sessionScope.loggedin || sessionScope.loggedin == null}">
                            <li><a href="${pageContext.request.contextPath}/login.jsp" class="rightnav">Login</a></li>
                            <li><a href="${pageContext.request.contextPath}/signup.jsp" class="rightnav">Sign up</a></li>
                            </c:when>
                            <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/user/userprofile.jsp" class="rightnav">${sessionScope.loggedin.getUsername()}</a></li>
                            <li><a href="${pageContext.request.contextPath}/user/do.logout" class="rightnav">Logout</a></li>
                            </c:otherwise>
                        </c:choose>
                </ul>
            </div>
        </div>
    </header>

    <script>
        document.getElementById("hamburger-nav").addEventListener("click", function () {
            this.classList.toggle("open"); // toggle the hamburger icon animation
            document.querySelector(".mobile-menu").classList.toggle("open"); // toggle the mobile menu visibility
        });
    </script>

</html>
