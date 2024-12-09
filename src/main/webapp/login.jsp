<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link
        rel="stylesheet"
        type="text/css"
        href="<%= request.getContextPath() %>/Resources/css/login.css"
    />
    <link
        rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
    />
    <title>Login</title>
</head>
<body>
    <div class="overlay">
        <!-- LOGIN FORM -->
        <form action="CheckLoginServlet" method="post">
            <!--   con = Container  for items in the form-->
            <div class="con">
                <!-- Start header Content -->
                <header class="head-form">
                    <h2>Log In</h2>
                    <!-- A welcome message or an explanation of the login form -->
                    <p>Login here using your username and password</p>
                </header>
                <!-- End header Content -->
                <br />
                <div class="field-set">
                    <!-- Username -->
                    <span class="input-item">
                        <i class="fa fa-user-circle"></i>
                    </span>
                    <!-- Username Input -->
                    <input
                        class="form-input"
                        id="txt-input"
                        type="text"
                        placeholder="UserName"
                        name="username"
                        required
                    />
                    <br />
                    <!-- Password -->
                    <span class="input-item">
                        <i class="fa fa-key"></i>
                    </span>
                    <!-- Password Input -->
                    <input
                        class="form-input"
                        type="password"
                        placeholder="Password"
                        id="pwd"
                        name="password"
                        required
                    />

                    <!-- Show/hide password -->
                    <span>
                        <i class="fa fa-eye" aria-hidden="true" type="button" id="eye"></i>
                    </span>

                    <br />
                    <% String errorMessage = (String) request.getAttribute("errorMessage"); 
            		if (errorMessage != null) { %>
                    <div style="color: red; font-weight: bold"><%= errorMessage %></div>
                    <%
                    } 
                    %>
                    <!-- Log In button -->
                    <button type="submit" class="log-in">Log In</button>
                </div>
            </div>
            <!-- End Container -->
        </form>
    </div>
    <script src="<%= request.getContextPath() %>/Resources/js/login.js"></script>
</body>
</html>
