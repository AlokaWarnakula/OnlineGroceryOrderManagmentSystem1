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
            background: url('https://img.freepik.com/premium-photo/blank-price-board-mock-up-advertising-board-supermarket-restaurant_795881-18994.jpg?ga=GA1.1.1475331235.1731237648&semt=ais_hybrid&w=740') no-repeat center center fixed;
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
            border-radius: 30px;
        }
        .forms {
            background: white;
            border-radius: 10px;
            padding: 60px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 100%;
            text-align: center;
            box-sizing: border-box;
        }
        .login-form {
            text-align: center;
            width: 100%;
        }
        .title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 20px;
            color: #333;
            border-bottom: 2px solid #28a745;
            display: inline-block;
            padding-bottom: 5px;
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
            font-size: 16px;
        }
        .input-box input {
            width: 100%;
            padding: 12px 12px 12px 40px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            outline: none;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        .input-box input:focus {
            border-color: #28a745;
        }
        .button input {
            background: #28a745;
            color: white;
            border: none;
            padding: 12px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            width: 100%;
            text-transform: uppercase;
            letter-spacing: 1px;
            transition: background 0.3s;
        }
        .button input:hover {
            background: #218838;
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
            margin-bottom: 15px;
            font-size: 14px;
        }
        .requirements {
            font-size: 12px;
            color: #666;
            text-align: left;
            margin-top: 5px;
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
            margin-right: 5px;
            color: #ccc;
        }
        .requirements li.valid i {
            color: #28a745;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="forms">
        <div class="login-form">
            <div class="title">Reset Password</div>
            <%
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
            <p class="message" style="color: red;"><%= error %></p>
            <%
                }
                String success = (String) request.getAttribute("success");
                if (success != null) {
            %>
            <p class="message" style="color: green;"><%= success %></p>
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
                    <div class="button input-box">
                        <input type="submit" value="Submit">
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

    passwordInput.addEventListener('input', validatePassword);
    confirmPasswordInput.addEventListener('input', validateConfirmPassword);
    form.addEventListener('submit', handleSubmit);

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

    function handleSubmit(event) {
        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;
        const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$!%*?&])[A-Za-z\d@#$!%*?&]{8,}$/;

        if (!passwordRegex.test(password)) {
            event.preventDefault();
            alert('Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character (@#$!%*?&).');
        } else if (password !== confirmPassword) {
            event.preventDefault();
            alert("Passwords don't match.");
        }
    }
</script>
</body>
</html>//