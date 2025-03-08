// Dynamically load products based on category from URL
async function loadProductsForCategory(category) {
    try {
        console.log(`Attempting to load products for category: ${category}`);
        const productModule = await import(`./${category}/product.js`);
        console.log(`Successfully loaded products for ${category}:`, productModule.default);
        return productModule.default;
    } catch (error) {
        console.error(`Failed to load products for ${category}:`, error);
        return [];
    }
}

// Load cart state from localStorage
let cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];

function initializeCart() {
    updateCartUI();
    updateCartCount();
    updateTotalPrice();

    // Get the current category from URL or default to Produce
    const urlParams = new URLSearchParams(window.location.search);
    const category = urlParams.get('category') || document.body.dataset.category || 'Produce';
    console.log(`Current category: ${category}`);

    // Load products for the current category
    loadProductsForCategory(category).then(products => {
        if (!products || products.length === 0) {
            console.warn(`No products found for category: ${category}`);
            return;
        }
        const productContent = document.querySelector('.product-content');
        const productDetail = document.querySelector('.product-detail');
        
        if (productContent) {
            console.log(`Found .product-content, loading ${products.length} products`);
            loadProducts(products, category); // Pass category explicitly
        } else if (productDetail) {
            console.log(`Found .product-detail, loading product detail`);
            loadProductDetail(products);
        } else {
            console.error('No .product-content or .product-detail found in the DOM');
        }
    }).catch(error => {
        console.error('Error loading products:', error);
    });
}

// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', initializeCart);

function loadProducts(products, category) {
    const productContent = document.querySelector('.product-content');
    if (!productContent) {
        console.error('.product-content element not found');
        return;
    }
    productContent.innerHTML = ''; // Clear previous products
    products.forEach(product => {
        console.log(`Rendering product: ${product.title}`);
        const productBox = document.createElement('div');
        productBox.classList.add('product-box');
        productBox.innerHTML = `
            <div class="img-box">
                <img src="${product.image}" alt="${product.title}">
            </div>
            <h2 class="product-title">${product.title}</h2>
            <div class="price-and-cart">
                <span class="price">$${product.price}</span>
                <i class="ri-shopping-cart-line add-cart" data-id="${product.id}"></i>
            </div>
        `;
        productContent.appendChild(productBox);

        const imgBox = productBox.querySelector('.img-box');
        if (imgBox) {
            imgBox.addEventListener('click', () => {
                console.log(`Navigating to product-detail.jsp for ${product.title} (ID: ${product.id}, Category: ${category})`);
                window.location.href = `product-detail.jsp?category=${category}&id=${product.id}`;
            });
        } else {
            console.error(`.img-box not found for product: ${product.title}`);
        }
    });

    document.querySelectorAll('.add-cart').forEach(button => {
        button.addEventListener('click', () => {
            const productId = button.getAttribute('data-id');
            addToCart(productId, products);
        });
    });
}

function loadProductDetail(products) {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = parseInt(urlParams.get('id'));
    const product = products.find(p => p.id === productId);
    const productInfo = document.querySelector('.product-info');

    if (!productInfo) {
        console.error('.product-info element not found');
        return;
    }

    if (product) {
        console.log(`Loading detail for product: ${product.title}`);
        productInfo.innerHTML = `
            <img src="${product.image}" alt="${product.title}" class="detail-img">
            <h2 class="product-name">${product.title}</h2>
            <p class="product-price">$${product.price.toFixed(2)}</p>
            <p class="product-description">${product.description}</p>
            <div class="detail-buttons">
                <button class="add-cart-btn" data-id="${product.id}">Add To Cart</button>
                <button class="checkout-btn">Check Out</button>
            </div>
        `;
        productInfo.querySelector('.add-cart-btn').addEventListener('click', () => {
            addToCart(productId, products);
        });
    } else {
        console.warn(`Product with ID ${productId} not found`);
    }

    const backBtn = document.querySelector('#back-btn');
    if (backBtn) {
        backBtn.addEventListener('click', () => {
            const category = urlParams.get('category') || 'Produce';
            window.location.href = `cartIndex.jsp?category=${category}`;
        });
    }
}

const cartIcon = document.querySelector("#cart-icon");
const cart = document.querySelector(".cart");
const cartClose = document.querySelector("#cart-close");

if (cartIcon) {
    cartIcon.addEventListener("click", () => {
        cart.classList.add("active");
        document.body.classList.add("cart-active");
    });
}

if (cartClose) {
    cartClose.addEventListener("click", () => {
        cart.classList.remove("active");
        document.body.classList.remove("cart-active");
    });
}

function addToCart(productId, products) {
    const product = products.find(p => p.id == productId);
    const existingItem = cartItems.find(item => item.id === product.id);

    if (existingItem) {
        alert("This Item Already In The Cart");
        return;
    }

    cartItems.push({ ...product, quantity: 1 });
    saveCart();
    updateCartUI();
    updateCartCount();
    updateTotalPrice();
}

function updateCartUI() {
    const cartContent = document.querySelector(".cart-content");
    if (!cartContent) return;

    cartContent.innerHTML = '';
    cartItems.forEach(item => {
        const cartBox = document.createElement("div");
        cartBox.classList.add("cart-box");
        cartBox.innerHTML = `
            <img src="${item.image}" class="cart-img">
            <div class="cart-details">
                <h2 class="cart-product-title">${item.title}</h2>
                <span class="cart-price">$${item.price}</span>
                <div class="cart-quantity">
                    <button class="decrement">-</button>
                    <span class="number">${item.quantity}</span>
                    <button class="increment">+</button>
                </div>
            </div>
            <i class="ri-delete-bin-line cart-remove"></i>
        `;
        cartContent.appendChild(cartBox);

        cartBox.querySelector(".cart-remove").addEventListener("click", () => {
            cartItems = cartItems.filter(i => i.id !== item.id);
            saveCart();
            updateCartUI();
            updateCartCount();
            updateTotalPrice();
        });

        const decrement = cartBox.querySelector(".decrement");
        const increment = cartBox.querySelector(".increment");
        const number = cartBox.querySelector(".number");

        decrement.addEventListener("click", () => {
            if (item.quantity > 1) {
                item.quantity--;
                number.textContent = item.quantity;
                saveCart();
                updateTotalPrice();
            }
        });

        increment.addEventListener("click", () => {
            item.quantity++;
            number.textContent = item.quantity;
            saveCart();
            updateTotalPrice();
        });
    });
}

function updateTotalPrice() {
    const totalPriceElement = document.querySelector(".total-price");
    if (totalPriceElement) {
        const total = cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0);
        totalPriceElement.textContent = `$ ${total}`;
    }
}

function updateCartCount() {
    const cartItemCountBadge = document.querySelector(".cart-item-count");
    if (cartItemCountBadge) {
        const count = cartItems.length;
        cartItemCountBadge.style.visibility = count > 0 ? "visible" : "hidden";
        cartItemCountBadge.textContent = count > 0 ? count : "";
    }
}

const buyNowButton = document.querySelector(".btn-buy");
if (buyNowButton) {
    buyNowButton.addEventListener("click", () => {
        if (cartItems.length === 0) {
            alert("Your cart is empty. Please add items to your cart before buying.");
            return;
        }
        cartItems = [];
        saveCart();
        updateCartUI();
        updateCartCount();
        updateTotalPrice();
        alert("Thank you for your purchase!");
    });
}

function saveCart() {
    localStorage.setItem('cartItems', JSON.stringify(cartItems));
}