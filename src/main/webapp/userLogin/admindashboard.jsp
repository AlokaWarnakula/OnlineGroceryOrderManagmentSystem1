<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin Dashboard</title>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap">
  <style>
    <%-- CSS from previous response --%>
    :root {
      --green: green;
      --red: #e74c3c;
      --black: #130f40;
      --light-color: #666;
      --box-shadow: 0 .1rem 1.5rem rgba(0,0,0,0.1);
      --border: .2rem solid rgba(0,0,0,0.1);
      --outline: .1rem solid rgba(0,0,0,0.1);
    }

    * {
      font-family: 'Poppins', sans-serif;
      margin: 0;
      padding: 0;
      box-sizing: border-box;
      outline: none;
      border: none;
      text-decoration: none;
      text-transform: capitalize;
      transition: all .2s linear;
    }

    html {
      font-size: 62.5%;
      overflow-x: hidden;
      scroll-behavior: smooth;
    }

    body {
      background: #eee;
    }

    .btn {
      border: 2px solid var(--black);
      margin-top: 1rem;
      display: inline-block;
      padding: .8rem 1rem;
      font-size: 1.7rem;
      border-radius: .5rem;
      color: var(--black);
      cursor: pointer;
      background: none;
    }

    .btn:hover {
      background: var(--green);
      color: white;
    }

    .Header {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 2rem 9%;
      background: #fff;
      box-shadow: var(--box-shadow);
      z-index: 1000;
    }

    .Header .logo {
      font-size: 2.5rem;
      font-weight: bolder;
      color: var(--black);
    }

    .Header .logo i {
      color: var(--green);
      font-style: italic;
    }

    .Header .navbar a {
      font-size: 1.7rem;
      margin: 0 1rem;
      color: var(--black);
    }

    .Header .navbar a:hover {
      color: #27ae60;
    }

    .Header .icons .icon-link {
      height: 4.5rem;
      width: 4.5rem;
      line-height: 4.5rem;
      border-radius: 50%;
      color: var(--black);
      font-size: 2rem;
      text-align: center;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .Header .icons .profile-icon {
      background-color: var(--green);
      color: white;
    }

    .Header .icons .logout-icon {
      background-color: var(--red);
      color: white;
    }

    .Header .icons .icon-link:hover .profile-icon {
      background-color: #2ecc71;
    }

    .Header .icons .icon-link:hover .logout-icon {
      background-color: #ff7061;
    }

    #login-page {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }

    .login-form {
      background: #fff;
      padding: 3rem;
      border-radius: 1rem;
      box-shadow: var(--box-shadow);
      text-align: center;
    }

    .login-form h2 {
      font-size: 3rem;
      color: var(--black);
      margin-bottom: 2rem;
    }

    .login-form input {
      width: 100%;
      padding: 1rem;
      margin: 1rem 0;
      border: var(--border);
      border-radius: .5rem;
      font-size: 1.6rem;
    }

    main {
      margin-top: 8rem;
      padding: 2rem 9%;
    }

    .heading {
      text-align: center;
      padding: 2rem 0;
      font-size: 3.5rem;
      color: var(--black);
    }

    .heading span {
      background: var(--green);
      color: #fff;
      padding: .5rem 3rem;
      clip-path: polygon(100% 0, 93% 50%, 100% 99%, 0% 100%, 7% 50%, 0% 0);
    }

    .stats {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(20rem, 1fr));
      gap: 1.5rem;
    }

    .card {
      background: #fff;
      padding: 2rem;
      border-radius: 1rem;
      box-shadow: var(--box-shadow);
      font-size: 1.8rem;
      color: var(--black);
      text-align: center;
    }

    .data-table {
      width: 100%;
      background: #fff;
      border-radius: 1rem;
      box-shadow: var(--box-shadow);
      overflow: hidden;
    }

    .data-table th, .data-table td {
      padding: 1.5rem;
      font-size: 1.6rem;
      border-bottom: var(--border);
    }

    .data-table th {
      background: var(--green);
      color: #fff;
    }

    .data-table td {
      color: var(--light-color);
    }

    .chart-placeholder {
      background: #fff;
      padding: 5rem;
      text-align: center;
      border-radius: 1rem;
      box-shadow: var(--box-shadow);
      font-size: 2rem;
      color: var(--light-color);
    }

    #settings form {
      background: #fff;
      padding: 2rem;
      border-radius: 1rem;
      box-shadow: var(--box-shadow);
    }

    #settings label {
      font-size: 1.8rem;
      color: var(--black);
      display: block;
      margin-bottom: 1rem;
    }

    #settings input {
      width: 100%;
      padding: 1rem;
      border: var(--border);
      border-radius: .5rem;
      font-size: 1.6rem;
    }

    body.dark-mode {
      background: var(--black);
      color: var(--light-color);
    }

    body.dark-mode .Header {
      background: #222;
      color: #fff;
    }

    body.dark-mode .card,
    body.dark-mode .data-table,
    body.dark-mode .chart-placeholder,
    body.dark-mode #settings form {
      background: #333;
      color: #fff;
    }

    body.dark-mode .login-form {
      background: #333;
    }

    .hidden {
      display: none;
    }

    @media (max-width: 991px) {
      html { font-size: 55%; }
      .Header { padding: 2rem; }
      main { padding: 2rem; }
    }

    @media (max-width: 768px) {
      .Header .navbar { display: none; } /* Add hamburger menu logic if needed */
    }
  </style>
</head>
<body>
<!-- Login Page (Feature 7) -->
<section id="login-page">
  <form class="login-form" method="post" action="loginServlet">
    <h2>Admin Login</h2>
    <input type="text" name="username" placeholder="Username" required>
    <input type="password" name="password" placeholder="Password" required>
    <button type="submit" class="btn">Login</button>
  </form>
</section>

<!-- Main Dashboard (Hidden until logged in) -->
<div id="dashboard" class="hidden">
  <header class="Header">
    <div class="logo">Admin <i>Panel</i></div>
    <nav class="navbar">
      <a href="#overview">Dashboard</a>
      <a href="#users">Users</a>
      <a href="#orders">Orders</a>
      <a href="#products">Products</a>
      <a href="#analytics">Analytics</a>
      <a href="#settings">Settings</a>
    </nav>
    <div class="icons">
      <div id="dark-mode-toggle" class="icon-link"><i class="profile-icon">🌙</i></div>
      <a href="logoutServlet" id="logout-btn" class="icon-link"><i class="logout-icon">🚪</i></a>
    </div>
  </header>

  <main>
    <!-- Dashboard Overview (Feature 1) -->
    <section id="overview">
      <h2 class="heading">Dashboard <span>Overview</span></h2>
      <div class="stats">
        <div class="card">Users: <%= request.getAttribute("userCount") != null ? request.getAttribute("userCount") : "1,234" %></div>
        <div class="card">Orders: <%= request.getAttribute("orderCount") != null ? request.getAttribute("orderCount") : "567" %></div>
        <div class="card">Revenue: <%= request.getAttribute("revenue") != null ? request.getAttribute("revenue") : "$12,345" %></div>
      </div>
    </section>

    <!-- User Management (Feature 2) -->
    <section id="users" class="hidden">
      <h2 class="heading">User <span>Management</span></h2>
      <table class="data-table">
        <thead><tr><th>ID</th><th>Name</th><th>Role</th><th>Actions</th></tr></thead>
        <tbody>
        <tr><td>1</td><td>John</td><td>Admin</td><td><button class="btn">Edit</button></td></tr>
        <%-- Add dynamic rows here with JSP --%>
        </tbody>
      </table>
    </section>

    <!-- Order Management (Feature 3) -->
    <section id="orders" class="hidden">
      <h2 class="heading">Order <span>Management</span></h2>
      <table class="data-table">
        <thead><tr><th>ID</th><th>Customer</th><th>Status</th><th>Total</th></tr></thead>
        <tbody>
        <tr><td>101</td><td>Jane</td><td>Shipped</td><td>$50</td></tr>
        <%-- Add dynamic rows here with JSP --%>
        </tbody>
      </table>
    </section>

    <!-- Product Management (Feature 4) -->
    <section id="products" class="hidden">
      <h2 class="heading">Product <span>Management</span></h2>
      <button class="btn">Add Product</button>
      <table class="data-table">
        <thead><tr><th>ID</th><th>Name</th><th>Price</th><th>Stock</th></tr></thead>
        <tbody>
        <tr><td>P1</td><td>Shirt</td><td>$20</td><td>100</td></tr>
        <%-- Add dynamic rows here with JSP --%>
        </tbody>
      </table>
    </section>

    <!-- Analytics (Feature 5) -->
    <section id="analytics" class="hidden">
      <h2 class="heading">Analytics <span>& Reports</span></h2>
      <div class="chart-placeholder">Chart Placeholder</div>
    </section>

    <!-- Settings (Feature 8) -->
    <section id="settings" class="hidden">
      <h2 class="heading">Settings</h2>
      <form method="post" action="settingsServlet">
        <label>Site Name: <input type="text" name="siteName" placeholder="My Site"></label>
        <button type="submit" class="btn">Save</button>
      </form>
    </section>
  </main>
</div>

<script>
  // Toggle dark mode (Feature 13)
  document.querySelector('#dark-mode-toggle').addEventListener('click', () => {
    document.body.classList.toggle('dark-mode');
  });

  // Basic login toggle (for demo, replace with servlet logic)
  document.querySelector('.login-form').addEventListener('submit', (e) => {
    // Let the form submit to servlet; hide login and show dashboard on success
  });

  <%-- JSP logic to show dashboard if logged in --%>
  <% if (session.getAttribute("loggedIn") != null && (boolean) session.getAttribute("loggedIn")) { %>
  document.getElementById('login-page').classList.add('hidden');
  document.getElementById('dashboard').classList.remove('hidden');
  <% } %>
</script>
</body>
</html>