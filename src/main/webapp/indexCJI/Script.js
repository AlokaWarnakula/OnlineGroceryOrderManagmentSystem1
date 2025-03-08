// Declare variables at the top to avoid scope issues
let searchForm = document.querySelector('.search-form');
let shoppingCart = document.querySelector('.shopping-cart');
let loginNow = document.querySelector('.login-form');
let navbar = document.querySelector('.navbar');

// Toggle search bar
document.querySelector('#search-btn').onclick = () => {
    searchForm.classList.toggle('active'); // Show/hide search form
    navbar.classList.remove('active'); // Close navbar if open
    loginNow.classList.remove('active'); // Close login form if open
    shoppingCart.classList.remove('active'); // Close shopping cart if open
}

// Toggle shopping cart
document.querySelector('#cart-btn').onclick = () => {
    shoppingCart.classList.toggle('active'); // Show/hide shopping cart
    navbar.classList.remove('active'); // Close navbar if open
    loginNow.classList.remove('active'); // Close login form if open
    searchForm.classList.remove('active'); // Close search form if open
}

// Toggle login form
document.querySelector('#login-btm').onclick = () => {
    loginNow.classList.toggle('active'); // Show/hide login form
    navbar.classList.remove('active'); // Close navbar if open
    shoppingCart.classList.remove('active'); // Close shopping cart if open
    searchForm.classList.remove('active'); // Close search form if open
}

// Toggle navigation bar (for mobile)
document.querySelector('#menu-btm').onclick = () => {
    navbar.classList.toggle('active'); // Show/hide navbar
    loginNow.classList.remove('active'); // Close login form if open
    shoppingCart.classList.remove('active'); // Close shopping cart if open
    searchForm.classList.remove('active'); // Close search form if open
}

// Close all active menus when scrolling
window.onscroll = () => {
    searchForm.classList.remove('active'); // Hide search form on scroll
    shoppingCart.classList.remove('active'); // Hide shopping cart on scroll
    loginNow.classList.remove('active'); // Hide login form on scroll
    navbar.classList.remove('active'); // Hide navbar on scroll
};

// Initialize Swiper for deals (product slider) - Works with class .product-slider in id="Deals"
var swiper = new Swiper(".product-slider", {
    loop: true, // Enable infinite loop
    spaceBetween: 20, // Space between slides (in pixels)
    autoplay: {
        delay: 3500, // Autoplay delay in milliseconds (7.5 seconds)
        disableOnInteraction: false, // Continue autoplay after user interaction
    },
    pagination: {
        el: ".swiper-pagination", // Pagination element
    },
    breakpoints: {
        0: {
            slidesPerView: 1, // 1 slide on mobile (0px and above)
        },
        768: {
            slidesPerView: 2, // 2 slides on tablets (768px and above)
        },
        1020: {
            slidesPerView: 3, // 3 slides on desktops (1020px and above)
        },
    }
});