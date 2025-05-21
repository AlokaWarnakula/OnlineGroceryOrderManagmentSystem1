<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Password Reset - Success</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        :root {
            --primary: #2196F3;
            --primary-dark: #1976D2;
            --success: #4CAF50;
            --success-light: #81C784;
            --text-dark: #2C3E50;
            --text-light: #FFFFFF;
            --background: #ffffff;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Poppins', sans-serif;
            background-image: url('https://img.freepik.com/premium-photo/closeup-young-african-american-male-consumer-using-smartphone_274679-41702.jpg?ga=GA1.1.1426601777.1747568572&semt=ais_hybrid&w=740'); /* Example online image URL */
            background-size: cover; /* Scales image to cover the entire element */
            background-position: center; /* Centers the image */
            background-repeat: no-repeat; /* Prevents image tiling */
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
        }

        .loading-container {
            text-align: center;
            padding: 2.5rem;
            background: white;
            border-radius: 1rem;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
            max-width: 500px;
            width: 90%;
            position: relative;
        }

        .success-animation {
            position: relative;
            width: 100px;
            height: 100px;
            margin: 0 auto 2rem;
        }

        .checkmark {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            display: block;
            stroke-width: 2;
            stroke: var(--success);
            stroke-miterlimit: 10;
            box-shadow: inset 0px 0px 0px var(--success);
            animation: fill .4s ease-in-out .4s forwards, scale .3s ease-in-out .9s both;
            position: relative;
            top: 0;
            right: 0;
            margin: 0 auto;
        }

        .checkmark__circle {
            stroke-dasharray: 166;
            stroke-dashoffset: 166;
            stroke-width: 2;
            stroke-miterlimit: 10;
            stroke: var(--success);
            fill: none;
            animation: stroke 0.6s cubic-bezier(0.65, 0, 0.45, 1) forwards;
        }

        .checkmark__check {
            transform-origin: 50% 50%;
            stroke-dasharray: 48;
            stroke-dashoffset: 48;
            animation: stroke 0.3s cubic-bezier(0.65, 0, 0.45, 1) 0.8s forwards;
        }

        @keyframes stroke {
            100% { stroke-dashoffset: 0; }
        }

        @keyframes scale {
            0%, 100% { transform: none; }
            50% { transform: scale3d(1.1, 1.1, 1); }
        }

        @keyframes fill {
            100% { box-shadow: inset 0px 0px 0px 50px var(--success-light); }
        }

        .loading-title {
            color: var(--text-dark);
            font-size: 1.8rem;
            font-weight: 600;
            margin-bottom: 1rem;
        }

        .loading-subtitle {
            color: #666;
            margin-bottom: 2rem;
        }

        .loading-progress {
            height: 8px;
            background: #E0E0E0;
            border-radius: 4px;
            overflow: hidden;
            margin-bottom: 1.5rem;
            position: relative;
        }

        .progress-bar {
            position: absolute;
            height: 100%;
            background: linear-gradient(90deg, var(--success), var(--success-light));
            border-radius: 4px;
            width: 0%;
            animation: progress 2s ease-in-out forwards;
        }

        @keyframes progress {
            0% { width: 0%; }
            100% { width: 100%; }
        }

        .security-features {
            display: flex;
            justify-content: center;
            gap: 2rem;
            margin-top: 2rem;
        }

        .security-item {
            text-align: center;
            animation: fadeIn 0.5s ease-out forwards;
            opacity: 0;
        }

        .security-item:nth-child(1) { animation-delay: 0.2s; }
        .security-item:nth-child(2) { animation-delay: 0.4s; }
        .security-item:nth-child(3) { animation-delay: 0.6s; }

        .security-item i {
            font-size: 2rem;
            color: var(--primary);
            margin-bottom: 0.5rem;
        }

        .security-item span {
            font-size: 0.9rem;
            color: #666;
            display: block;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .redirect-message {
            margin-top: 2rem;
            padding: 1rem;
            background: rgba(33, 150, 243, 0.1);
            border-radius: 0.5rem;
            color: var(--primary);
            font-size: 0.9rem;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
        }

        .redirect-message i {
            animation: bounce 1s infinite;
        }

        @keyframes bounce {
            0%, 100% { transform: translateX(0); }
            50% { transform: translateX(3px); }
        }
    </style>
</head>
<body>
<div class="loading-container">
    <div class="success-animation">
        <svg class="checkmark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 52 52">
            <circle class="checkmark__circle" cx="26" cy="26" r="25" fill="none"/>
            <path class="checkmark__check" fill="none" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
        </svg>
    </div>

    <h1 class="loading-title">Password Reset Successful!</h1>
    <p class="loading-subtitle">Your account security has been updated</p>

    <div class="loading-progress">
        <div class="progress-bar"></div>
    </div>

    <div class="security-features">
        <div class="security-item">
            <i class="fas fa-shield-alt"></i>
            <span>Enhanced<br>Security</span>
        </div>
        <div class="security-item">
            <i class="fas fa-lock"></i>
            <span>Password<br>Updated</span>
        </div>
        <div class="security-item">
            <i class="fas fa-user-shield"></i>
            <span>Account<br>Protected</span>
        </div>
    </div>

    <div class="redirect-message">
        <i class="fas fa-arrow-right"></i>
        Redirecting you to login page...
    </div>
</div>

<script>
    setTimeout(() => {
        window.location.href = "${pageContext.request.contextPath}/userLogin/login.jsp";
    }, 6000); // Redirect after 6 seconds
</script>
</body>
</html>