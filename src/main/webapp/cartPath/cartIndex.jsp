<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Grocery</title>
    <link rel="stylesheet" href="style.css">
    <link href="https://cdn.jsdelivr.net/npm/remixicon@4.5.0/fonts/remixicon.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>
<body data-category="Produce">
    <header>
        <a href="../index.jsp" class="logo"><i class="fa-solid fa-basket-shopping"></i> Grocery</a>
        <nav class="navbar">
            <a href="cartIndex.jsp?category=Produce">Produce</a>
            <a href="cartIndex.jsp?category=Proteins">Proteins</a>
            <a href="cartIndex.jsp?category=Dairy">Dairy</a>
            <a href="cartIndex.jsp?category=Bakery">Bakery</a>
            <a href="cartIndex.jsp?category=Pantry">Pantry</a>
            <a href="cartIndex.jsp?category=Snacks">Snacks</a>
        </nav>
        <div class="cart-icon" id="cart-icon">
            <i class="ri-shopping-cart-line"></i>
            <span class="cart-item-count">0</span>
        </div>
    </header>

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

    <section class="shop">
        <h1 class="section-title">Shop <span id="category-title">Produce</span></h1>
        <div class="product-content"></div>
    </section>

    <script src="cart/cart.js" type="module"></script>
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            const category = new URLSearchParams(window.location.search).get('category') || 'Produce';
            document.getElementById('category-title').textContent = category;
        });
    </script>
</body>
</html>