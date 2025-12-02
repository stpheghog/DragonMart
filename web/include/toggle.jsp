<%-- 
    Document   : toggle
    Created on : Oct 7, 2024, 7:38:33 PM
    Author     : lucyallysongarcia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Toggle JSP</title>

        <link id="theme-stylesheet" 
              rel="stylesheet" 
              href="${pageContext.request.contextPath}/${sessionScope.mode == 'light' ? 'css/lightmode.css' : 'css/darkmode.css'}">

        <style>
            #theme-toggle { 
                position: fixed;
                left: 20px;
                bottom: 20px;
                width: 50px;
                height: 50px;
                border-radius: 50%;
                background-color: darkred;
                border: none;
                cursor: pointer;
                transition: background-color 0.3s ease;
                background-size: cover;
                z-index: 9999;
            }

            #theme-toggle.light {
                background-image: url('${pageContext.request.contextPath}/images/lightmode.png');
            }

            #theme-toggle.dark {
                background-image: url('${pageContext.request.contextPath}/images/darkmode.png');
            }

            #theme-toggle:hover {
                background-color: gray;
            }
        </style>

    </head>
    <body>
        
        <!-- Correctly set form action relative to the application root -->
        <form id="toggleForm" action="${pageContext.request.contextPath}/do.toggle" method="POST">
            <input type="hidden" id="toggleValue" name="toggle" value="${sessionScope.mode == 'light' ? 'true' : 'false'}">
            <button type="button" id="theme-toggle" class="${sessionScope.mode == 'light' ? 'dark' : 'light'}" onclick="toggle()"> </button>
        </form>

        <script>
            const toggleButton = document.getElementById('theme-toggle');
            const toggleValue = document.getElementById('toggleValue');
            let isLightMode = ${sessionScope.mode == 'light'};

            toggleButton.addEventListener('click', () => {
                if (isLightMode) {
                    document.getElementById('theme-stylesheet').href = '${pageContext.request.contextPath}/css/darkmode.css';
                    toggleButton.classList.remove('light');
                    toggleButton.classList.add('dark');
                    toggleValue.value = 'false'; 
                    isLightMode = false;
                } else {
                    document.getElementById('theme-stylesheet').href = '${pageContext.request.contextPath}/css/lightmode.css';
                    toggleButton.classList.remove('dark');
                    toggleButton.classList.add('light');
                    toggleValue.value = 'true'; 
                    isLightMode = true;
                }

                // Submit the form to the servlet
                document.getElementById('toggleForm').submit();
            });
        </script>
        
    </body>
</html>
