var swiper = new Swiper(".product-slider", {
    loop: true,
    spaceBetween: 20,
    autoplay: {
        delay: 3500,
        disableOnInteraction: false,
    },
    pagination: {
        el: ".swiper-pagination",
    },
    breakpoints: {
        0: {
            slidesPerView: 1,
        },
        768: {
            slidesPerView: 2,
        },
        1020: {
            slidesPerView: 3,
        },
    },
});
// Initialize Swiper for the home section
const homeSwiper = new Swiper('.home-slider', {
    loop: true, // Enable continuous looping
    autoplay: {
        delay: 5000, // Slide changes every 5 seconds
        disableOnInteraction: false, // Autoplay continues after user interaction
    },
    pagination: {
        el: '.swiper-pagination', // Enable pagination dots
        clickable: true,
    },
    navigation: {
        nextEl: '.swiper-button-next', // Enable next button
        prevEl: '.swiper-button-prev', // Enable previous button
    },
});