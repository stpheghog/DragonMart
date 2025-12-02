<%-- 
    Document   : adminheader
    Created on : 12 Nov 2024, 5:17:40 pm
    Author     : blonyagoncillo
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <header>

        <nav class="topnav">
            <div class="logo">
                <img class="logo-img" src="../images/logo.png" alt="logo">
                <p class="logo-text">DragonMart</p>
            </div>
            <ul class="nav-list">
                <li><a href="do.homepage" class="home">Home</a></li>
                <li><a href="do.viewlistings">Listings</a></li>


            </ul>
            <div class="auth-links">
                <c:choose>
                    <c:when test = "${empty sessionScope.admin || sessionScope.admin == null}">
                        <a href="login.jsp" class="rightnav">Login</a>
                    </c:when>
                    <c:otherwise>
                        <a href="do.homepage" class="rightnav"> ${sessionScope.admin.getEmail()}</a>
                        <a href="do.logout" class="rightnav">Logout</a>
                    </c:otherwise>
                </c:choose>    
            </div>

        </nav>

        <%--MOBILE ADAPTIVE NAB STARTS HERE--%>
        <div id="mobile-nav">
            <div class="logo">
                <img class="logo-img" src="${pageContext.request.contextPath}/images/logo.png" alt="logo">
            </div>

            <div id="hamburger-nav">
                <div class="bar1"></div>
                <div class="bar2"></div>
                <div class="bar3"></div>

                <ul class="mobile-menu">
                    <li><a href="do.homepage" class="home">Home</a></li>
                    <li><a href="do.viewlistings">Listings</a></li><br>
                        <c:choose>
                            <c:when test = "${empty sessionScope.admin || sessionScope.admin == null}">
                                <li><a href="login.jsp" class="rightnav">Login</a></li>
                                </c:when>
                                <c:otherwise>
                                <li>Logged into: ${sessionScope.admin.getEmail()}</li><br>
                                <li><a href="do.logout" class="rightnav">Logout</a></li>
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
