<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Detail</title>
    <link rel="stylesheet" href="style.css">
    <link href="https://cdn.jsdelivr.net/npm/remixicon@4.5.0/fonts/remixicon.css" rel="stylesheet" />
</head>
<body>
    <!-- Header with Back button, Home link, and cart icon -->
    <header>
        <button class="back-btn" id="back-btn">Back</button> <!-- Back button in header -->
        <a href="cartIndex.jsp" class="logo">Home</a> <!-- Home link -->
        <div class="cart-icon" id="cart-icon">
            <i class="ri-shopping-cart-line"></i>
            <span class="cart-item-count">0</span>
        </div>
    </header>

    <!-- Cart Function (shared with cartIndex.jsp) -->
    <div class="cart">
        <h2 class="cart-title">Your Cart</h2>
        <div class="cart-content"></div>
        <div class="total">
            <div class="total-title">Total</div>
            <div class="total-price">$0</div>
            <button class="btn-buy">Buy Now</button>
            <i class="ri-close-line" id="cart-close"></i>
        </div>
    </div>

    <!-- Product Detail Section -->
    <section class="product-detail">
        <h1 class="detail-title">PRODUCT DETAIL</h1> <!-- Centered title -->
        <div class="product-info">
            <!-- Product details will be added here dynamically by cart.js -->
        </div>
    </section>

    <script src="cart/cart.js" type="module"></script>

</body>
</html>