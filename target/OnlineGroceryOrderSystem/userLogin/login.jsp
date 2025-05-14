<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <title>Login and Registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/logIn.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        .popup {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%) scale(0.8);
            background: rgba(255, 255, 255, 0.98);
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.3);
            max-width: 400px;
            width: 90%;
            text-align: center;
            z-index: 1000;
            border: 3px solid #3E7B27;
            opacity: 0;
            transition: opacity 0.3s ease, transform 0.3s ease;
        }
        .popup.active {
            display: block;
            opacity: 1;
            transform: translate(-50%, -50%) scale(1);
        }
        .popup-content {
            position: relative;
        }
        .popup-icon {
            color: #3E7B27;
            font-size: 24px;
            margin-bottom: 15px;
        }
        .popup-content p {
            margin: 0 0 20px;
            font-size: 15px;
            color: #721c24;
            line-height: 1.5;
        }
        .popup-content .ok-btn {
            background: #5CB338;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 1px;
            transition: background 0.3s, transform 0.2s;
        }
        .popup-content .ok-btn:hover {
            background: #3E7B27;
            transform: translateY(-2px);
        }
        .overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.6);
            z-index: 999;
            opacity: 0;
            transition: opacity 0.3s ease;
        }
        .overlay.active {
            display: block;
            opacity: 1;
        }
    </style>
</head>
<body style="background: url('https://img.freepik.com/free-photo/shopping-cart-full-products-inside-supermarket_123827-28165.jpg?ga=GA1.1.1475331235.1731237648&semt=ais_hybrid&w=740') no-repeat center center fixed; background-size: cover">
<div class="overlay" id="popupOverlay"></div>
<div class="popup" id="errorPopup">
    <div class="popup-content">
        <i class="fas fa-exclamation-circle popup-icon"></i>
        <p id="popupMessage"></p>
        <button class="ok-btn" onclick="closePopup()">OK</button>
    </div>
</div>
<div class="container">
    <input type="checkbox" id="flip">
    <div class="cover">
        <div class="front">
            <img src="https://img.freepik.com/free-photo/delivery-concept-handsome-african-american-delivery-man-carrying-package-box-grocery-food-drink-from-store-isolated-grey-studio-background-copy-space_1258-1230.jpg?ga=GA1.1.1475331235.1731237648&w=740" alt="">
            <div class="text">
                <span class="text-1">Every new friend is a <br> new adventure</span>
                <span class="text-2">Let's get connected</span>
            </div>
        </div>
        <div class="back">
            <img src="https://img.freepik.com/free-photo/woman-holding-glass-juice-showing-phone-mock-up_23-2148332116.jpg?ga=GA1.1.1475331235.1731237648&semt=ais_hybrid&w=740">
            <div class="text">
                <span class="text-1">Complete miles of journey <br> with one step</span>
                <span class="text-2">Let's get started</span>
            </div>
        </div>
    </div>
    <div class="forms">
        <div class="form-content">
            <div class="login-form">
                <div class="title">Login</div>
                <%
                    String error = (String) request.getAttribute("error");
                    if (error != null) {
                %>
                <p style="color: red;"><%= error %></p>
                <%
                    }
                    String loginError = request.getParameter("error");
                    if ("notLoggedIn".equals(loginError)) {
                %>
                <p style="color: red;">Login first before shopping</p>
                <%
                    }
                %>
                <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
                    <div class="input-boxes">
                        <div class="input-box">
                            <i class="fas fa-envelope"></i>
                            <input type="email" name="email" placeholder="Enter your email" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-lock"></i>
                            <input type="password" name="password" placeholder="Enter your password" required>
                        </div>
                        <div class="text"><a href="${pageContext.request.contextPath}/userLogin/forgotPassword.jsp">Forgot password?</a></div>
                        <div class="button input-box">
                            <input type="submit" value="Submit">
                        </div>
                        <div class="text sign-up-text">Don't have an account? <label for="flip">Signup now</label></div>
                    </div>
                </form>
            </div>
            <div class="signup-form">
                <div class="title">Signup</div>
                <form id="signupForm" action="${pageContext.request.contextPath}/RegisterServlet" method="post">
                    <div class="input-boxes">
                        <div class="input-box">
                            <i class="fas fa-user"></i>
                            <input type="text" name="fullName" placeholder="Enter your full name" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-envelope"></i>
                            <input type="email" name="email" placeholder="Enter your email" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-phone"></i>
                            <input type="text" name="phoneNumber" placeholder="Enter your phone number" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-home"></i>
                            <input type="text" name="address" placeholder="Enter your address" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-lock"></i>
                            <input type="password" id="password" name="password" placeholder="Enter your password" required>
                        </div>
                        <div class="button input-box">
                            <input type="submit" value="Submit">
                        </div>
                        <div class="text sign-up-text">Already have an account? <label for="flip">Login now</label></div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    const signupForm = document.getElementById('signupForm');
    const passwordInput = document.getElementById('password');
    const popup = document.getElementById('errorPopup');
    const popupMessage = document.getElementById('popupMessage');
    const popupOverlay = document.getElementById('popupOverlay');

    signupForm.addEventListener('submit', handleSubmit);
    popupOverlay.addEventListener('click', closePopup);

    function showPopup(message) {
        popupMessage.textContent = message;
        popup.classList.add('active');
        popupOverlay.classList.add('active');
    }

    function closePopup() {
        popup.classList.remove('active');
        popupOverlay.classList.remove('active');
    }

    function handleSubmit(event) {
        const password = passwordInput.value;
        const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$!%*?&])[A-Za-z\d@#$!%*?&]{8,}$/;

        if (!passwordRegex.test(password)) {
            event.preventDefault();
            showPopup('Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character (@#$!%*?&).');
            return false;
        }
        return true;
    }
</script>
</body>
</html>