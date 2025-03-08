<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Add favicon -->
    <link rel="icon" type="image/icon" href="./indexCJI/Images/favicon.PNG">

    <meta charset="UTF-8">
    <title>Online Grocery Order Management System</title>
    <!-- Font Awesome library for adding icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <!-- CSS file link -->
    <link rel="stylesheet" type="text/css" href="indexCJI/styles.css">
    <!-- HTML meta tags -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- SwiperJS site for product slider -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" />
</head>
<body>
<!-- Header Section -->
<header class="Header">
    <a class="logo"> <i class="fa-solid fa-basket-shopping"> Grocery</i></a> <!-- Removed unnecessary rel="#" -->
    <!-- Navigation Bar -->
    <nav class="navbar">
        <a href="#home">Home</a>
        <a href="#features">Features</a>
        <a href="#Deals">Deals</a> <!-- Updated from "Products" to "Deals" -->
        <a href="#categories">Categories</a>
    </nav>
    <!-- Icons for search, cart, login -->
    <div class="icons">
        <div class="fa-solid fa-bars" id="menu-btm"></div>
        <div class="fa-solid fa-magnifying-glass" id="search-btn"></div>
        <div class="fa-solid fa-cart-shopping" id="cart-btn"></div>
        <div class="fa-solid fa-user" id="login-btm"></div>
    </div>
    <!-- Search Form -->
    <form class="search-form">
        <input type="text" id="search-box" name="" placeholder="Search Here">
        <label for="search-box" class="fa-solid fa-magnifying-glass"></label>
    </form>

    <!-- Shopping Cart -->
    <div class="shopping-cart">
        <div class="box">
            <i class="fa-solid fa-trash"></i>
            <img src="./indexCJI/Images/cart-img-1.png" alt="Fresh Orange">
            <div class="content">
                <h3>Fresh Orange</h3> <!-- Corrected from "Watermelon" -->
                <span class="price">$5.99/-</span>
                <span class="quantity">Qty: 1</span>
            </div>
        </div>

        <div class="box">
            <i class="fa-solid fa-trash"></i>
            <img src="./indexCJI/Images/cart-img-2.png" alt="Fresh Onion">
            <div class="content">
                <h3>Fresh Onion</h3> <!-- Corrected from "Watermelon" -->
                <span class="price">$4.99/-</span>
                <span class="quantity">Qty: 1</span>
            </div>
        </div>

        <div class="box">
            <i class="fa-solid fa-trash"></i>
            <img src="./indexCJI/Images/cart-img-3.png" alt="Fresh Chicken">
            <div class="content">
                <h3>Fresh Chicken</h3> <!-- Corrected from "Watermelon" -->
                <span class="price">$7.99/-</span>
                <span class="quantity">Qty: 1</span>
            </div>
        </div>

        <div class="total"> Total: $19.99/-</div>
        <a href="#" class="btn">Checkout</a>
    </div>

    <!-- User Login Form -->
    <form action="#" class="login-form">
        <h3>Login Now</h3>
        <input type="email" name="" placeholder="Your Email" class="box">
        <input type="password" name="" placeholder="Your Password" class="box">
        <p>Forgot Your Password <a href="#">Click Here</a></p>
        <p>Do not Have An Account <a href="#">Create Now</a></p>
        <input type="submit" value="Login Now" class="btn">
    </form>
</header>

<!-- Banner Section -->
<section class="home" id="home">
    <div class="content">
        <h3>Fresh and <span>Organic</span> Product For You</h3>
        <p>"Enjoy fresh, organic produce straight from local farms naturally grown, nutrient-rich, and delivered to your door for a healthy, sustainable lifestyle."</p>
        <a href="#" class="btn">Shop Now</a>
    </div>
</section>

<!-- Features Section -->
<section class="features" id="features">
    <h1 class="heading">Our <span>Features</span></h1>
    <div class="box-container">
        <div class="box">
            <img src="./indexCJI/Images/feature-img-1.png">
            <h3>Fresh And Organic</h3>
            <p>"Savor the crisp, nutrient-rich taste of our fresh, organic vegetables, grown naturally for your health and the planet."</p>
            <a href="#" class="btn">Read More</a>
        </div>

        <div class="box">
            <img src="./indexCJI/Images/feature-img-2.png">
            <h3>Free Delivery</h3>
            <p>"Savor the crisp, nutrient-rich taste of our fresh, organic vegetables, grown naturally for your health and the planet."</p>
            <a href="#" class="btn">Read More</a>
        </div>

        <div class="box">
            <img src="./indexCJI/Images/feature-img-3.png">
            <h3>Easy Payment</h3>
            <p>"Savor the crisp, nutrient-rich taste of our fresh, organic vegetables, grown naturally for your health and the planet."</p>
            <a href="#" class="btn">Read More</a>
        </div>
    </div>
</section>

<!-- Deals Section (Updated from Product Section, id="Deals") -->
<section class="Deals" id="Deals">
    <h1 class="heading">New <span>Deals</span></h1>
    <div class="swiper product-slider">
        <div class="swiper-wrapper">
            <div class="swiper-slide box">
                <img src="./indexCJI/Images/product-1.png">
                <h1>Fresh Orange</h1>
                <div class="price">$4.99/- - $8.99/-</div>
                <div class="starts">
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star-half-stroke"></i>
                </div>
                <a href="#" class="btn">Add To Cart</a>
            </div>

            <div class="swiper-slide box">
                <img src="./indexCJI/Images/product-2.png">
                <h1>Fresh Onion</h1>
                <div class="price">$2.99/- - $5.99/-</div>
                <div class="starts">
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star-half-stroke"></i>
                </div>
                <a href="#" class="btn">Add To Cart</a>
            </div>

            <div class="swiper-slide box">
                <img src="./indexCJI/Images/product-3.png">
                <h1>Fresh Meat</h1>
                <div class="price">$9.99/- - $14.99/-</div>
                <div class="starts">
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star-half-stroke"></i>
                </div>
                <a href="#" class="btn">Add To Cart</a>
            </div>

            <div class="swiper-slide box">
                <img src="./indexCJI/Images/product-4.png">
                <h1>Fresh Cabbage</h1>
                <div class="price">$3.99/- - $6.99/-</div>
                <div class="starts">
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star-half-stroke"></i>
                </div>
                <a href="#" class="btn">Add To Cart</a>
            </div>

            <div class="swiper-slide box">
                <img src="./indexCJI/Images/product-5.png">
                <h1>Fresh Potato</h1>
                <div class="price">$2.99/- - $5.99/-</div>
                <div class="starts">
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star-half-stroke"></i>
                </div>
                <a href="#" class="btn">Add To Cart</a>
            </div>

            <div class="swiper-slide box">
                <img src="./indexCJI/Images/product-6.png">
                <h1>Fresh Avocado</h1>
                <div class="price">$3.99/- - $4.99/-</div>
                <div class="starts">
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star-half-stroke"></i>
                </div>
                <a href="#" class="btn">Add To Cart</a>
            </div>

            <div class="swiper-slide box">
                <img src="./indexCJI/Images/product-7.png">
                <h1>Fresh Carrot</h1>
                <div class="price">$3.99/- - $6.99/-</div>
                <div class="starts">
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star-half-stroke"></i>
                </div>
                <a href="#" class="btn">Add To Cart</a>
            </div>

            <div class="swiper-slide box">
                <img src="./indexCJI/Images/product-8.png">
                <h1>Fresh Lemon</h1>
                <div class="price">$0.99/- - $2.99/-</div>
                <div class="starts">
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star-half-stroke"></i>
                </div>
                <a href="#" class="btn">Add To Cart</a>
            </div>
        </div>
        <div class="swiper-pagination"></div>
    </div>
</section>

<!-- Product Category Sections -->
<section class="categories" id="categories">
    <h1 class="heading">Product <span>Category</span></h1>
    <div class="box-container">
        <div class="box">
            <img src="./indexCJI/Images/cat-1.png">
            <h3>Vegetables</h3>
            <p>Upto 15% Off</p>
            <a href="#" class="btn">Shop Now</a>
        </div>

        <div class="box">
            <img src="./indexCJI/Images/cat-2.png">
            <h3>Fresh Fruits</h3>
            <p>Upto 15% Off</p>
            <a href="#" class="btn">Shop Now</a>
        </div>

        <div class="box">
            <img src="./indexCJI/Images/cat-3.png">
            <h3>Dairy Products</h3> <!-- Corrected "Diary" to "Dairy" -->
            <p>Upto 15% Off</p>
            <a href="#" class="btn">Shop Now</a>
        </div>

        <div class="box">
            <img src="./indexCJI/Images/cat-4.png">
            <h3>Fresh Meat</h3>
            <p>Upto 15% Off</p>
            <a href="#" class="btn">Shop Now</a>
        </div>
    </div>
</section>

<!-- Banner Section -->
<section class="home" id="home">
    <div class="content">
        <h3>Fresh and <span>Organic</span> Product For You</h3>
        <p>"Enjoy fresh, organic produce straight from local farms naturally grown, nutrient-rich, and delivered to your door for a healthy, sustainable lifestyle."</p>
        <a href="cartPath/cartIndex.jsp" class="btn">Shop Now</a>
    </div>
</section>

<!-- Footer Section -->
<section class="footer" id="footer">
    <div class="box-container">
        <div class="box">
            <h3><i class="fa-solid fa-basket-shopping"></i> Grocery</h3>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque pretium lacus porttitor placerat malesuada. Curabitur ut maximus arcu. Aliquam et faucibus Sed nec vestibulum.</p>
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
            <a href="#" class="links"><i class="fa-solid fa-arrow-right"></i> Home</a>
            <a href="#" class="links"><i class="fa-solid fa-arrow-right"></i> Features</a>
            <a href="#" class="links"><i class="fa-solid fa-arrow-right"></i> Deals</a> <!-- Updated to "Deals" -->
            <a href="#" class="links"><i class="fa-solid fa-arrow-right"></i> Categories</a>
        </div>

        <div class="box">
            <h3>News Letter</h3>
            <p>Subscribe For Latest Grocery Update</p>
            <input type="email" placeholder="Your Email" class="email">
            <input type="submit" value="Subscribe" class="btn">
            <img src="./indexCJI/Images/payment.png" class="payment-img">
        </div>
    </div>

    <div class="credit">PGNO-<span>278</span> | All Rights Reserved</div>
</section>

<!-- JS file links -->
<script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
<script src="indexCJI/Script.js"></script>
</body>
</html>