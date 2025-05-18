<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/icon" href="./indexCJI/Images/favicon.PNG">
    <meta charset="UTF-8">
    <title>Online Grocery Order Management System</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" />
    <link rel="stylesheet" href="css/index.css">
    <style>
        body {
            background: #f4f4f4;
            font-family: 'Open Sans', sans-serif;
        }
        .header {
            display: flex;
            align-items: center;
            justify-content: flex-end;
            padding: 10px 20px;
            background: #333;
        }
        .icon-link {
            color: #fff;
            margin: 0 10px;
            font-size: 20px;
            text-decoration: none;
        }
        .icon-link:hover {
            color: #ddd;
        }
        #animation-container {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.8);
            z-index: 1000;
            justify-content: center;
            align-items: center;
        }
        .loop-wrapper {
            margin: 0 auto;
            position: relative;
            display: block;
            width: 600px;
            height: 250px;
            overflow: hidden;
            border-bottom: 3px solid #fff;
            color: #fff;
        }
        .mountain {
            position: absolute;
            right: -900px;
            bottom: -20px;
            width: 2px;
            height: 2px;
            box-shadow:
                    0 0 0 50px #4DB6AC,
                    60px 50px 0 70px #4DB6AC,
                    90px 90px 0 50px #4DB6AC,
                    250px 250px 0 50px #4DB6AC,
                    290px 320px 0 50px #4DB6AC,
                    320px 400px 0 50px #4DB6AC;
            transform: rotate(130deg);
            animation: mtn 20s linear infinite;
        }
        .hill {
            position: absolute;
            right: -900px;
            bottom: -50px;
            width: 400px;
            border-radius: 50%;
            height: 20px;
            box-shadow:
                    0 0 0 50px #4DB6AC,
                    -20px 0 0 20px #4DB6AC,
                    -90px 0 0 50px #4DB6AC,
                    250px 0 0 50px #4DB6AC,
                    290px 0 0 50px #4DB6AC,
                    620px 0 0 50px #4DB6AC;
            animation: hill 4s 2s linear infinite;
        }
        .tree, .tree:nth-child(2), .tree:nth-child(3) {
            position: absolute;
            height: 100px;
            width: 35px;
            bottom: 0;
            background: url(https://s3-us-west-2.amazonaws.com/s.cdpn.io/130015/tree.svg) no-repeat;
        }
        .rock {
            margin-top: -17%;
            height: 2%;
            width: 2%;
            bottom: -2px;
            border-radius: 20px;
            position: absolute;
            background: #ddd;
            animation: rock 4s -0.530s linear infinite;
        }
        .truck, .wheels {
            transition: all ease;
            width: 85px;
            margin-right: -60px;
            bottom: 0px;
            right: 50%;
            position: absolute;
            background: #eee;
        }
        .truck {
            background: url(https://s3-us-west-2.amazonaws.com/s.cdpn.io/130015/truck.svg) no-repeat;
            background-size: contain;
            height: 60px;
            animation: truck 4s 0.080s ease infinite;
        }
        .truck:before {
            content: " ";
            position: absolute;
            width: 25px;
            box-shadow:
                    -30px 28px 0 1.5px #fff,
                    -35px 18px 0 1.5px #fff;
            animation: wind 1.5s 0s ease infinite;
        }
        .wheels {
            background: url(https://s3-us-west-2.amazonaws.com/s.cdpn.io/130015/wheels.svg) no-repeat;
            height: 15px;
            margin-bottom: 0;
            animation: truck 4s 0.001s ease infinite;
        }
        .tree  { animation: tree 3s 0s linear infinite; }
        .tree:nth-child(2)  { animation: tree2 2s 0.15s linear infinite; }
        .tree:nth-child(3)  { animation: tree3 8s 0.05s linear infinite; }

        @keyframes tree {
            0%   { transform: translate(1350px); }
            100% { transform: translate(-50px); }
        }
        @keyframes tree2 {
            0%   { transform: translate(650px); }
            100% { transform: translate(-50px); }
        }
        @keyframes tree3 {
            0%   { transform: translate(2750px); }
            100% { transform: translate(-50px); }
        }
        @keyframes rock {
            0%   { right: -200px; }
            100% { right: 2000px; }
        }
        @keyframes truck {
            6%   { transform: translateY(0px); }
            7%   { transform: translateY(-6px); }
            9%   { transform: translateY(0px); }
            10%  { transform: translateY(-1px); }
            11%  { transform: translateY(0px); }
        }
        @keyframes wind {
            50%  { transform: translateY(3px); }
        }
        @keyframes mtn {
            100% {
                transform: translateX(-2000px) rotate(130deg);
            }
        }
        @keyframes hill {
            100% {
                transform: translateX(-2000px);
            }
        }
    </style>
</head>
<body style="
background: rgb(255,255,255);
background: radial-gradient(circle, rgba(255,255,255,1) 0%, rgba(244,255,240,1) 100%);
">


<header class="Header">
    <a href="#" class="logo"><i class="fa-solid fa-basket-shopping"></i> Grocery</a>
    <nav class="navbar">
        <a href="#Banner">Home</a>
        <a href="#features">Features</a>
        <a href="#Deals">Deals</a>
        <a href="#categories">Categories</a>
    </nav>
    <div class="icons">
        <div class="fa-solid fa-bars" id="menu-btn"></div>
        <%
            User loggedInUser = (User) session.getAttribute("user");
            if (loggedInUser != null) {
        %>
        <a href="${pageContext.request.contextPath}/UserProfileServlet" class="icon-link">
            <i class="fa-solid fa-user profile-icon"></i>
        </a>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="icon-link">
            <i class="fa-solid fa-sign-out-alt logout-icon"></i>
        </a>
        <% } else { %>
        <a href="#" class="icon-link" onclick="showAnimation()">
            <i class="fa-solid fa-user login-icon"></i>
        </a>
        <% } %>
    </div>
    <div id="animation-container">
        <div class="loop-wrapper">
            <div class="mountain"></div>
            <div class="hill"></div>
            <div class="tree"></div>
            <div class="tree"></div>
            <div class="tree"></div>
            <div class="rock"></div>
            <div class="truck"></div>
            <div class="wheels"></div>
        </div>
    </div>

    <script>
        function showAnimation() {
            const animationContainer = document.getElementById('animation-container');
            animationContainer.style.display = 'flex';
            setTimeout(() => {
                animationContainer.style.display = 'none';
                window.location.href = '${pageContext.request.contextPath}/userLogin/login.jsp';
            }, 2000);
        }
    </script>


</header>

<!-- Home Banners -->
<section class="Banner" id="Banner">
    <div class="swiper home-slider">
        <div class="swiper-wrapper">
            <!-- Slide 1 -->
            <div class="swiper-slide slide" style="background: url('https://essstr.blob.core.windows.net/uiimg/Carousel/FreshVegetables/FreshVegetablesWebBanner.png') no-repeat center/cover;">
                <div class="content">
                    <h3>Fresh <span>Vegetables</span></h3>
                    <p>Discover the best organic produce for your healthy lifestyle.</p>
                    <a href="${pageContext.request.contextPath}/CartServlet?category=Produce" class="btn">Shop Now</a>
                </div>
            </div>
            <!-- Slide 2 -->
            <div class="swiper-slide slide" style="background: url('https://essstr.blob.core.windows.net/uiimg/Carousel/slide1.jpg') no-repeat center/cover;">
                <div class="content">
                    <h3>Exclusive <span>Deals</span></h3>
                    <p>Save big on your favorite grocery items this week!</p>
                    <a href="#categories" class="btn">Shop Now</a>
                </div>
            </div>
            <!-- Slide 3 -->
            <div class="swiper-slide slide" style="background: url('https://t3.ftcdn.net/jpg/06/14/08/90/360_F_614089075_9zP2Ybcr5fwsnHCzGsPNLLkpThUru9Zq.jpg') no-repeat center/cover;">
                <div class="content">
                    <h3>Fresh <span>Proteins</span> Products</h3>
                    <p>Save big on your favorite grocery items this week!</p>
                    <a href="${pageContext.request.contextPath}/CartServlet?category=Proteins" class="btn">Shop Now</a>
                </div>
            </div>
            <!-- Slide 4 -->
            <div class="swiper-slide slide" style="background: url('https://media.istockphoto.com/id/1471438213/photo/dairy-products-bottles-of-milk-cheese-cottage-cheese-yogurt-butter-on-meadow-of-cows.jpg?s=612x612&w=0&k=20&c=hIUSgarP7-7h1KDF4AuPzzMNCPbJ5h5ofPF30G0rGhc=') no-repeat center/cover;">
                <div class="content">
                    <h3>Fresh <span>Dairy</span> Products</h3>
                    <p>Save big on your favorite grocery items this week!</p>
                    <a href="${pageContext.request.contextPath}/CartServlet?category=Dairy" class="btn">Shop Now</a>
                </div>
            </div>
            <!-- Slide 5 -->
            <div class="swiper-slide slide" style="background: url('https://t4.ftcdn.net/jpg/06/27/46/27/360_F_627462785_DyaFl6hi7cAmpmB4obBFewgFrM6A488N.jpg') no-repeat center/cover;">
                <div class="content">
                    <h3>Fresh <span>Bakery</span> Products</h3>
                    <p>Save big on your favorite grocery items this week!</p>
                    <a href="${pageContext.request.contextPath}/CartServlet?category=Bakery" class="btn">Shop Now</a>
                </div>
            </div>
            <!-- Slide 6 -->
            <div class="swiper-slide slide" style="background: url('https://media.istockphoto.com/id/1227210244/photo/flat-lay-view-at-kitchen-table-full-with-non-perishable-foods-spase-for-text.jpg?s=612x612&w=0&k=20&c=yoKYTbSTaHdBtRjgOUsDYBSB_0B10QxrR6lKH_36Hps=') no-repeat center/cover;">
                <div class="content">
                    <h3>Fresh <span>Pantry</span> Products</h3>
                    <p>Save big on your favorite grocery items this week!</p>
                    <a href="${pageContext.request.contextPath}/CartServlet?category=Pantry" class="btn">Shop Now</a>
                </div>
            </div>
            <!-- Slide 7 -->
            <div class="swiper-slide slide" style="background: url('https://as1.ftcdn.net/jpg/03/68/66/94/1000_F_368669476_Cl7gGRuBWRYnPLwwY8pBgmeH1lGvpQ1r.jpg') no-repeat center/cover;">
                <div class="content">
                    <h3>Tasty <span>Snacks</span></h3>
                    <p>Save big on your favorite grocery items this week!</p>
                    <a href="${pageContext.request.contextPath}/CartServlet?category=Snacks" class="btn">Shop Now</a>
                </div>
            </div>
        </div>
        <!-- Pagination -->
        <div class="swiper-pagination"></div>
        <!-- Navigation buttons -->
        <div class="swiper-button-next"></div>
        <div class="swiper-button-prev"></div>
    </div>
</section>

<%--features section--%>
<section class="features" id="features">
    <h1 class="heading">Our <span>Features</span></h1>
    <div class="box-container">
        <div class="box">
            <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwMUypuoJ7jxCE6Ltc7o0v9mHbWFLyHj55VA&s">
            <h3>Fresh And Organic</h3>
            <p>Enjoy nutrient-rich, organic produce sourced from local farmers for a healthier, sustainable lifestyle.</p>
            <a href="${pageContext.request.contextPath}/ourFeatures/freshAndOrganic.jsp" class="btn">Read More</a>
        </div>
        <div class="box">
            <img src="https://png.pngtree.com/png-clipart/20230211/original/pngtree-free-delivery-truck-icon-png-image_8951758.png">
            <h3>Free Delivery</h3>
            <p>Get your groceries delivered to your door for free on orders above a minimum amount.</p>
            <a href="${pageContext.request.contextPath}/ourFeatures/freeDelivery.jsp" class="btn">Read More</a>
        </div>
        <div class="box">
            <img src="https://img.freepik.com/free-vector/hands-holding-credit-card-mobile-phone-with-banking-app-person-paying-with-bank-card-transferring-money-shopping-online-flat-vector-illustration-payment-finance-concept_74855-24760.jpg">
            <h3>Easy Payment</h3>
            <p>Checkout effortlessly with secure, flexible payment options tailored for your convenience.</p>
            <a href="${pageContext.request.contextPath}/ourFeatures/easyPayment.jsp" class="btn">Read More</a>
        </div>
    </div>
</section>

<%--deals section--%>
<section class="Deals" id="Deals">
    <h1 class="heading">New <span>Deals</span></h1>
    <div class="swiper product-slider">
        <div class="swiper-wrapper">

            <div class="swiper-slide box">
                <h1>No Deals Available</h1>
            </div>

        </div>
        <div class="swiper-pagination"></div>
    </div>
</section>

<%--category section--%>
<section class="categories" id="categories">
    <h1 class="heading">Product <span>Category</span></h1>
    <div class="box-container">
        <div class="box">
            <img src="https://t4.ftcdn.net/jpg/00/51/27/87/360_F_51278711_lK9RGYczSSU1GXlIwjwyM4QzVmPfuLSV.jpg">
            <h3>Produce</h3>
            <p>Upto 15% Off</p>
            <a href="${pageContext.request.contextPath}/CartServlet?category=Produce" class="btn">Shop Now</a>
        </div>
        <div class="box">
            <img src="https://media.istockphoto.com/id/1185677996/photo/group-of-raw-seafood-isolated-on-white-background.jpg?s=612x612&w=0&k=20&c=dS10THUgufhCZPdO1Kn5LOhtpc2ZSlHXKmI9SdfXpoQ=">
            <h3>Proteins</h3>
            <p>Upto 15% Off</p>
            <a href="${pageContext.request.contextPath}/CartServlet?category=Proteins" class="btn">Shop Now</a>
        </div>
        <div class="box">
            <img src="https://img.freepik.com/premium-photo/different-fresh-dairy-products-isolated-white-background-isometry_660230-86599.jpg">
            <h3>Dairy</h3>
            <p>Upto 15% Off</p>
            <a href="${pageContext.request.contextPath}/CartServlet?category=Dairy" class="btn">Shop Now</a>
        </div>
        <div class="box">
            <img src="https://s1.1zoom.me/big0/970/Pastry_Buns_Bread_White_background_547126_1280x938.jpg">
            <h3>Bakery</h3>
            <p>Upto 15% Off</p>
            <a href="${pageContext.request.contextPath}/CartServlet?category=Bakery" class="btn">Shop Now</a>
        </div>
        <div class="box">
            <img src="https://media.istockphoto.com/id/504102317/photo/stack-of-different-sprouting-seeds-growing-in-a-glass-jar.jpg?s=612x612&w=0&k=20&c=_tG4Xl41hHrBBKo_sHDbZ3xYQRMebdUyiPb9txj_IfQ=">
            <h3>Pantry</h3>
            <p>Upto 15% Off</p>
            <a href="${pageContext.request.contextPath}/CartServlet?category=Pantry" class="btn">Shop Now</a>
        </div>
        <div class="box">
            <img src="https://img.freepik.com/premium-photo/assorted-american-junk-food-assortment-isolated-white-background-isometry_660230-86599.jpg">
            <h3>Snacks</h3>
            <p>Upto 15% Off</p>
            <a href="${pageContext.request.contextPath}/CartServlet?category=Snacks" class="btn">Shop Now</a>
        </div>
    </div>
</section>

<section class="home" id="home">
    <div class="content">
        <h3>Fresh and <span>Organic</span> Product For You</h3>
        <p>Enjoy fresh, organic produce straight from local farms naturally grown, nutrient-rich, and delivered to your door for a healthy, sustainable lifestyle.</p>
        <a href="${pageContext.request.contextPath}/CartServlet?category=Produce" class="btn">Shop Now</a>
    </div>
</section>

<section class="footer" id="footer">
    <div class="box-container">
        <div class="box">
            <h3><i class="fa-solid fa-basket-shopping"></i> Grocery</h3>
            <p>Your one-stop shop for fresh, affordable, organic groceries delivered fast.</p>
            <div class="share">
                <a href="#" class="fa-brands fa-facebook-f"></a>
                <a href="#" class="fa-brands fa-x-twitter"></a>
                <a href="#" class="fa-brands fa-instagram"></a>
                <a href="#" class="fa-brands fa-linkedin-in"></a>
            </div>
        </div>
        <div class="box">
            <h3>Contact Info</h3>
            <a href="#" class="links"> <i class="fa-solid fa-phone"></i> +94 711641638</a>
            <a href="#" class="links"> <i class="fa-solid fa-phone"></i> +94 704563428</a>
            <a href="#" class="links"> <i class="fa-solid fa-envelope"></i> example@gmail.com</a>
            <a href="#" class="links"> <i class="fa-solid fa-location-dot"></i> Sliit, Sri Lanka - 10115</a>
        </div>
        <div class="box">
            <h3>Quick Links</h3>
            <a href="#Banner" class="links"><i class="fa-solid fa-arrow-right"></i> Home</a>
            <a href="#features" class="links"><i class="fa-solid fa-arrow-right"></i> Features</a>
            <a href="#Deals" class="links"><i class="fa-solid fa-arrow-right"></i> Deals</a>
            <a href="#categories" class="links"><i class="fa-solid fa-arrow-right"></i> Categories</a>
        </div>
        <div class="box">
            <h3>News Letter</h3>
            <p>Subscribe For Latest Grocery Update</p>
            <input type="email" placeholder="Your Email" class="email">
            <input type="submit" value="Subscribe" class="btn">
            <img src="https://www.shutterstock.com/image-vector/rivne-ukraine-may-25-2023-260nw-2308151527.jpg" class="payment-img">
        </div>
    </div>
    <div class="credit">PGNO-<span>278</span> | All Rights Reserved</div>
</section>

<script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
<script src="js/index.js"></script>
</body>
</html>//