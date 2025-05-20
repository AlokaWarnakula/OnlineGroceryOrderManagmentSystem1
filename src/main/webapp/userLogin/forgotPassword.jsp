<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <title>Forgot Password</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/logIn.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            font-family: Arial, sans-serif;
            background: url('https://img.freepik.com/premium-photo/blank-price-board-mock-up-advertising-board-supermarket-restaurant_795881-18994.jpg') no-repeat center center fixed;
            background-size: cover;
        }
        .container {
            display: flex;
            justify-content: center;
            align-items: center;
            width: 100%;
            max-width: 500px;
            padding: 20px;
            box-sizing: border-box;
        }
        .forms {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
            width: 100%;
            text-align: center;
            box-sizing: border-box;
        }
        .login-form {
            text-align: center;
            width: 100%;
        }
        .title {
            font-size: 28px;
            font-weight: bold;
            margin-bottom: 25px;
            color: #333;
            border-bottom: 3px solid #28a745;
            display: inline-block;
            padding-bottom: 8px;
        }
        .input-boxes {
            display: flex;
            flex-direction: column;
            gap: 20px;
            margin-top: 20px;
        }
        .input-box {
            position: relative;
            width: 100%;
        }
        .input-box i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #28a745;
            font-size: 18px;
        }
        .input-box input {
            width: 100%;
            padding: 12px 12px 12px 40px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 15px;
            outline: none;
            box-sizing: border-box;
            transition: border-color 0.3s, box-shadow 0.3s;
        }
        .input-box input:focus {
            border-color: #28a745;
            box-shadow: 0 0 5px rgba(40, 167, 69, 0.3);
        }
        .button input {
            background: #28a745;
            color: white;
            border: none;
            padding: 14px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 16px;
            width: 100%;
            text-transform: uppercase;
            letter-spacing: 1px;
            transition: background 0.3s, transform 0.2s;
        }
        .button input:hover {
            background: #218838;
            transform: translateY(-2px);
        }
        .text.sign-up-text {
            margin-top: 20px;
            font-size: 14px;
            color: #666;
        }
        .text.sign-up-text a {
            color: #28a745;
            text-decoration: none;
            font-weight: bold;
        }
        .text.sign-up-text a:hover {
            text-decoration: underline;
        }
        .message {
            margin: 15px 0;
            font-size: 14px;
            padding: 10px;
            border-radius: 5px;
        }
        .message.error {
            color: #721c24;
            background: #f8d7da;
        }
        .message.success {
            color: #155724;
            background: #d4edda;
        }
        .requirements {
            font-size: 12px;
            color: #666;
            text-align: left;
            margin-top: 10px;
            padding: 10px;
            background: #f9f9f9;
            border-radius: 5px;
        }
        .requirements ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }
        .requirements li {
            margin: 5px 0;
            display: flex;
            align-items: center;
        }
        .requirements li i {
            margin-right: 8px;
            color: #ccc;
            font-size: 14px;
        }
        .requirements li.valid i {
            color: #28a745;
        }
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
            border: 3px solid #28a745;
            opacity: 0;
            transition: opacity 0.3s ease, transform 0.3s ease;
        }
        .popup.active {
            display: block;
            opacity: 1;
            transform: translate(-50%, -50%) scale(1);
        }
        .popup-content p {
            margin: 0 0 20px;
            font-size: 15px;
            color: #721c24;
            line-height: 1.5;
        }
        .popup-content .ok-btn {
            background: #28a745;
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
            background: #218838;
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
<body>
<div class="overlay" id="popupOverlay"></div>
<div class="popup" id="errorPopup">
    <div class="popup-content">
        <p id="popupMessage"></p>
        <button class="ok-btn" onclick="closePopup()">OK</button>
    </div>
</div>
<div class="container">
    <div class="forms">
        <div class="login-form">
            <div class="title">Reset Password</div>
            <%
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
            <p class="message error"><%= error %></p>
            <%
                }
                String success = (String) request.getAttribute("success");
                if (success != null) {
            %>
            <p class="message success"><%= success %></p>
            <%
                }
            %>
            <form id="resetForm" action="${pageContext.request.contextPath}/forgotPassword" method="post">
                <div class="input-boxes">
                    <div class="input-box">
                        <i class="fas fa-envelope"></i>
                        <input type="email" name="email" placeholder="Enter your email" value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>" required>
                    </div>
                    <div class="input-box">
                        <i class="fas fa-lock"></i>
                        <input type="password" id="password" name="password" placeholder="Enter new password" required>
                    </div>
                    <div class="input-box">
                        <i class="fas fa-lock"></i>
                        <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm new password" required>
                    </div>
                    <div class="requirements">
                        <ul>
                            <li id="length"><i class="fas fa-check"></i> At least 8 characters</li>
                            <li id="uppercase"><i class="fas fa-check"></i> At least one uppercase letter</li>
                            <li id="lowercase"><i class="fas fa-check"></i> At least one lowercase letter</li>
                            <li id="digit"><i class="fas fa-check"></i> At least one digit</li>
                            <li id="special"><i class="fas fa-check"></i> At least one special character (@#$!%*?&)</li>
                        </ul>
                    </div>
                    <div class="button input-box">
                        <input type="submit" value="Reset Password">
                    </div>
                    <div class="text sign-up-text">
                        Remember your password? <a href="${pageContext.request.contextPath}/userLogin/login.jsp">Login now</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    const form = document.getElementById('resetForm');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const lengthReq = document.getElementById('length');
    const uppercaseReq = document.getElementById('uppercase');
    const lowercaseReq = document.getElementById('lowercase');
    const digitReq = document.getElementById('digit');
    const specialReq = document.getElementById('special');
    const popup = document.getElementById('errorPopup');
    const popupMessage = document.getElementById('popupMessage');
    const popupOverlay = document.getElementById('popupOverlay');

    passwordInput.addEventListener('input', validatePassword);
    confirmPasswordInput.addEventListener('input', validateConfirmPassword);
    form.addEventListener('submit', handleSubmit);
    popupOverlay.addEventListener('click', closePopup);

    function validatePassword() {
        const password = passwordInput.value;
        const tests = {
            length: password.length >= 8,
            uppercase: /[A-Z]/.test(password),
            lowercase: /[a-z]/.test(password),
            digit: /\d/.test(password),
            special: /[@#$!%*?&]/.test(password)
        };

        lengthReq.classList.toggle('valid', tests.length);
        uppercaseReq.classList.toggle('valid', tests.uppercase);
        lowercaseReq.classList.toggle('valid', tests.lowercase);
        digitReq.classList.toggle('valid', tests.digit);
        specialReq.classList.toggle('valid', tests.special);
    }

    function validateConfirmPassword() {
        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;
        if (confirmPassword && password !== confirmPassword) {
            confirmPasswordInput.setCustomValidity("Passwords don't match");
        } else {
            confirmPasswordInput.setCustomValidity('');
        }
    }

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
        const confirmPassword = confirmPasswordInput.value;
        const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$!%*?&])[A-Za-z\d@#$!%*?&]{8,}$/;

        if (!passwordRegex.test(password)) {
            event.preventDefault();
            showPopup('Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character (@#$!%*?&).');
        } else if (password !== confirmPassword) {
            event.preventDefault();
            showPopup("Passwords don't match.");
        }
    }
</script>
</body>
</html>