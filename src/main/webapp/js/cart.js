// Manages cart interactions and UI on the cart page, interacts with CartServlet
document.addEventListener('DOMContentLoaded', () => {
    // Select DOM elements for cart and dropdown
    const addToCartButtons = document.querySelectorAll('.add-cart, .add-cart-btn');
    const cartIcon = document.getElementById('cart-icon');
    const cart = document.querySelector('.cart');
    const cartClose = document.getElementById('cart-close');
    const dropdownToggle = document.getElementById('dropdown-toggle');
    const dropdownMenu = document.getElementById('dropdown-menu');
    let cartItemCount = 0;
    let totalPrice = 0;

    // Dropdown toggle functionality
    if (dropdownToggle && dropdownMenu) {
        dropdownToggle.addEventListener('click', () => {
            dropdownMenu.classList.toggle('active');
        });

        // Close dropdown when clicking outside
        document.addEventListener('click', (event) => {
            if (!dropdownToggle.contains(event.target) && !dropdownMenu.contains(event.target)) {
                dropdownMenu.classList.remove('active');
            }
        });
    }

    // Handle add-to-cart button clicks
    addToCartButtons.forEach(button => {
        button.addEventListener('click', () => {
            if (button.disabled) return;

            const itemId = button.getAttribute('data-item-id');
            // Send POST request to CartServlet to add item
            fetch(`${window.contextPath}/CartServlet`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=add&itemId=${itemId}`
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        updateCartUI(data.cart, data.totalPrice);
                    } else {
                        console.log(data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        });
    });

    // Show cart sidebar on icon click
    if (cartIcon && cart && !window.location.pathname.includes('/cartAndOrders/checkOut.jsp')) {
        cartIcon.addEventListener('click', () => {
            cart.classList.add('active');
        });
    }

    // Hide cart sidebar on close click
    if (cartClose && cart) {
        cartClose.addEventListener('click', () => {
            cart.classList.remove('active');
        });
    }

    // Update cart UI with items and total price
    function updateCartUI(cart, totalPriceValue) {
        const cartContent = document.querySelector('.cart-content');
        const totalContainer = document.getElementById('total-container');
        const totalPriceElement = document.getElementById('total-price');
        cartContent.innerHTML = '';
        cartItemCount = cart.length;
        totalPrice = parseFloat(totalPriceValue);

        // Display empty cart message or item list
        if (cartItemCount === 0) {
            cartContent.innerHTML = '<p>Your cart is empty.</p>';
            totalContainer.style.display = 'none';
        } else {
            cart.forEach(item => {
                const cartBox = document.createElement('div');
                cartBox.classList.add('cart-box');
                const isAtStockLimit = item.quantity >= item.totalAvailableStock;
                cartBox.innerHTML = `
                    <img src="${item.productImageLink}" alt="${item.productName}" class="cart-img">
                    <div class="cart-details">
                        <div class="cart-product-title">${item.productName}</div>
                        <div class="cart-price">Rs. ${parseFloat(item.productPrice).toFixed(2)}</div>
                        <div class="cart-quantity">
                            <button class="decrease-quantity" data-item-id="${item.productID}">-</button>
                            <div class="number">${item.quantity}</div>
                            <button class="increase-quantity" data-item-id="${item.productID}" ${isAtStockLimit ? 'disabled' : ''}>+</button>
                        </div>
                    </div>
                    <i class="fas fa-trash cart-remove" data-item-id="${item.productID}"></i>
                `;
                cartContent.appendChild(cartBox);
            });
            totalContainer.style.display = 'block';
            totalPriceElement.textContent = `Total Rs. ${totalPrice.toFixed(2)}`;

            // Add event listeners for quantity and remove buttons
            document.querySelectorAll('.decrease-quantity').forEach(button => {
                button.addEventListener('click', () => updateQuantity(button.getAttribute('data-item-id'), -1));
            });
            document.querySelectorAll('.increase-quantity').forEach(button => {
                button.addEventListener('click', () => updateQuantity(button.getAttribute('data-item-id'), 1));
            });
            document.querySelectorAll('.cart-remove').forEach(button => {
                button.addEventListener('click', () => removeFromCart(button.getAttribute('data-item-id')));
            });
        }

        // Update cart item count display
        document.querySelector('.cart-item-count').textContent = cartItemCount;
        document.querySelector('.cart-item-count').style.visibility = cartItemCount > 0 ? 'visible' : 'hidden';
    }


    // Update item quantity via CartServlet
    function updateQuantity(itemId, change) {
        fetch(`${window.contextPath}/CartServlet`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `action=update&itemId=${itemId}&change=${change}`
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    updateCartUI(data.cart, data.totalPrice);
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    // Remove item from cart via CartServlet
    function removeFromCart(itemId) {
        fetch(`${window.contextPath}/CartServlet`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `action=remove&itemId=${itemId}`
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    updateCartUI(data.cart, data.totalPrice);
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    // Load initial cart state from CartServlet
    fetch(`${window.contextPath}/CartServlet?action=getCart`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                updateCartUI(data.cart, data.totalPrice);
            } else {
                console.error('Failed to load initial cart:', data.message);
            }
        })
        .catch(error => console.error('Error loading cart:', error));
});