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
        .input-box {
            position: relative;
            width: 100%;
        }
        .input-box i {
            position: absolute;
            top: 50%;
            transform: translateY(-50%);
            font-size: 15px;
        }
        /* Left-aligned icons for email, user, phone, and home */
        .input-box i.fa-envelope,
        .input-box i.fa-user,
        .input-box i.fa-phone,
        .input-box i.fa-home {
            left: 5px;
        }
        /* Right-aligned icon for password (lock) */
        .input-box i.fa-lock {
            left: 5px;
            color: #28a745;
        }
        .input-box input {
            width: 100%;
            padding: 12px 12px 12px 40px; /* Default padding for left-aligned icons */
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            outline: none;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        /* Adjust padding for password fields with right-aligned lock icon */
        .input-box input[type="password"] {
            padding: 12px 40px 12px 12px;
        }
        .input-box input:focus {
            border-color: #28a745;
        }
    </style>
</head>
<body style="background: url('https://img.freepik.com/free-photo/shopping-cart-full-products-inside-supermarket_123827-28165.jpg?ga=GA1.1.1475331235.1731237648&semt=ais_hybrid&w=740') no-repeat center center fixed; background-size: cover">
<div class="container">
    <input type="checkbox" id="flip">
    <div class="cover">
        <div class="front">
            <img src="https://img.freepik.com/free-photo/delivery-concept-handsome-african-american-delivery-man-carrying-package-box-grocery-food-drink-from-store-isolated-grey-studio-background-copy-space_1258-1230.jpg?ga=GA1.1.1475331235.1731237648&w=740" alt="">
            <div class="text">
                <span class="text-1"> <br> Sri Lanka's Freshest </span>
                <span class="text-2">Online Grocery Store</span>
            </div>
        </div>
        <div class="back">
            <img src="https://img.freepik.com/free-photo/woman-holding-glass-juice-showing-phone-mock-up_23-2148332116.jpg?ga=GA1.1.1475331235.1731237648&semt=ais_hybrid&w=740" alt="">
            <div class="text">
                <span class="text-1">24/7 Grocery Store & <br> Supermarket in SriLanka</span>
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
                            <input type="email" name="email" placeholder="Enter your email" required style="margin-left: 2.5%;">

                        </div>
                        <div class="input-box">
                            <i class="fas fa-lock"></i>
                            <input type="password" name="password" placeholder=" password" required style="margin-left: 4.5%;" >
                        </div>
                        <div class="text"><a href="${pageContext.request.contextPath}/forgotPassword">Forgot password?</a></div>
                        <div class="button input-box">
                            <input type="submit" value="Submit">
                        </div>
                        <div class="text sign-up-text">Don't have an account? <label for="flip">Signup now</label></div>
                    </div>
                </form>
            </div>
            <div class="signup-form">
                <div class="title">Signup</div>
                <%
                    error = (String) request.getAttribute("error");
                    if (error != null) {
                %>
                <p style="color: red;"><%= error %></p>
                <%
                    }
                %>
                <form id="signupForm" action="${pageContext.request.contextPath}/RegisterServlet" method="post">
                    <div class="input-boxes">
                        <div class="input-box">
                            <i class="fas fa-user"></i>
                            <input type="text" name="fullName" placeholder="Enter your full name" required>
                        </div>
                        <div class="input-box">
                            <i class="fas fa-envelope"></i>
                            <input type="email" name="email" placeholder="email" required>
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
                            <input type="password" id="password" name="password" placeholder=" password" required style="margin-left: 4.5%;">
                            <div id="passwordRequirements" class="requirements">
                                <ul>

                                </ul>
                            </div>
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
    const lengthReq = document.getElementById('length');
    const uppercaseReq = document.getElementById('uppercase');
    const lowercaseReq = document.getElementById('lowercase');
    const digitReq = document.getElementById('digit');
    const specialReq = document.getElementById('special');

    passwordInput.addEventListener('input', validatePassword);
    signupForm.addEventListener('submit', handleSubmit);

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

    function handleSubmit(event) {
        const password = passwordInput.value;
        const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$!%*?&])[A-Za-z\d@#$!%*?&]{8,}$/;

        if (!passwordRegex.test(password)) {
            event.preventDefault();
            alert('Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character (@#$!%*?&).');
        }
    }
</script>
</body>
</html>