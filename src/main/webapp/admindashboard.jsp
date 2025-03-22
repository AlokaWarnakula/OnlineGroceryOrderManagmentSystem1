<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>E-commerce Admin Dashboard</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
      font-family: Arial, sans-serif;
    }

    body {
      background-color: #f5f5f5;
    }

    .container {
      display: flex;
      flex-wrap: wrap;
    }

    .sidebar {
      width: 250px;
      background-color: #fff;
      position: fixed;
      padding: 20px;
      height: 100vh;
      border-right: 1px solid #e0e0e0;
    }

    .logo {
      font-size: 20px;
      font-weight: bold;
      color: #007bff;
      margin-bottom: 30px;
    }

    .nav-links {
      list-style: none;
    }

    .nav-links li {
      padding: 10px 0;
      font-size: 16px;
      color: #666;
      cursor: pointer;
      display: flex;
      align-items: center;
    }

    .nav-links li i {
      margin-right: 10px;
    }

    .nav-links li.active {
      color: #007bff;
      font-weight: bold;
    }

    .badge {
      background-color: #dc3545;
      color: #fff;
      border-radius: 50%;
      padding: 2px 6px;
      font-size: 12px;
      margin-left: 5px;
    }

    .main-content {
      flex: 1;
      padding: 20px;
      margin-left: 250px; /* Offset for fixed sidebar */
      width: calc(100% - 250px);
    }

    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background-color: #fff;
      padding: 10px 20px;
      border-radius: 8px;
      margin-bottom: 20px;
    }

    .search-bar {
      display: flex;
      align-items: center;
      background-color: #f5f5f5;
      padding: 5px 10px;
      border-radius: 5px;
    }

    .search-bar input {
      border: none;
      background: none;
      outline: none;
      margin-left: 10px;
    }

    .user-info {
      display: flex;
      align-items: center;
    }

    .user-info i {
      margin-right: 10px;
      color: #666;
    }

    .avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
    }

    h1 {
      font-size: 24px;
      margin-bottom: 20px;
    }

    .stats-cards {
      display: flex;
      gap: 20px;
      margin-bottom: 20px;
      flex-wrap: wrap;
    }

    .card {
      background-color: #fff;
      padding: 20px;
      border-radius: 8px;
      flex: 1;
      text-align: center;
      min-width: 200px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    }

    .card h3 {
      font-size: 16px;
      color: #666;
      margin-bottom: 10px;
    }

    .card .value {
      font-size: 24px;
      font-weight: bold;
    }

    .charts-section {
      display: flex;
      gap: 20px;
      margin-bottom: 20px;
      flex-wrap: wrap;
    }

    .sales-chart, .visitors-chart {
      background-color: #fff;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    }

    .sales-chart {
      flex: 2;
    }

    .visitors-chart {
      flex: 1;
    }

    .chart-placeholder, .pie-chart-placeholder {
      height: 200px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #f5f5f5;
      border-radius: 8px;
      margin-top: 10px;
    }

    .pie-chart-placeholder p {
      font-size: 24px;
      font-weight: bold;
    }

    .visitor-stats {
      list-style: none;
      margin-top: 20px;
    }

    .visitor-stats li {
      display: flex;
      align-items: center;
      margin-bottom: 10px;
    }

    .dot {
      width: 10px;
      height: 10px;
      border-radius: 50%;
      margin-right: 10px;
    }

    .dot.blue { background-color: #007bff; }
    .dot.green { background-color: #28a745; }
    .dot.purple { background-color: #6f42c1; }
    .dot.teal { background-color: #17a2b8; }

    .latest-orders {
      background-color: #fff;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
      overflow-x: auto;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      min-width: 600px;
    }

    th, td {
      padding: 10px;
      text-align: left;
      border-bottom: 1px solid #e0e0e0;
    }

    th {
      background-color: #f5f5f5;
      font-weight: bold;
    }

    .status {
      padding: 5px 10px;
      border-radius: 5px;
      font-size: 14px;
    }

    .status.delivered {
      background-color: #d4edda;
      color: #155724;
    }

    .status.cancelled {
      background-color: #f8d7da;
      color: #721c24;
    }

    .status.pending {
      background-color: #fff3cd;
      color: #856404;
    }
  </style>
</head>
<body>
<div class="container">
  <!-- Sidebar -->
  <div class="sidebar">
    <div class="logo">
      <i class="fas fa-shopping-cart"></i> Admin Ecommerce
    </div>
    <ul class="nav-links">
      <li class="active"><i class="fas fa-tachometer-alt"></i> Dashboard</li>
      <li><i class="fas fa-box"></i> Products</li>
      <li><i class="fas fa-shopping-bag"></i> Orders</li>
      <li><i class="fas fa-users"></i> Customers</li>
      <li><i class="fas fa-chart-bar"></i> Statistics</li>
      <li><i class="fas fa-star"></i> Reviews</li>
      <li><i class="fas fa-exchange-alt"></i> Transactions <span class="badge">6</span></li>
      <li><i class="fas fa-store"></i> Sellers</li>
      <li><i class="fas fa-fire"></i> Hot Offers</li>
      <li><i class="fas fa-paint-brush"></i> Appearance</li>
      <li><i class="fas fa-cog"></i> Settings</li>
    </ul>
  </div>

  <!-- Main Content -->
  <div class="main-content">
    <!-- Header -->
    <div class="header">
      <div class="search-bar">
        <i class="fas fa-search"></i>
        <input type="text" placeholder="Search">
      </div>
      <div class="user-info">
        <i class="fas fa-bell"></i>
        <img src="https://via.placeholder.com/40" alt="User Avatar" class="avatar">
      </div>
    </div>

    <!-- Dashboard Content -->
    <h1>Dashboard</h1>

    <!-- Stats Cards -->
    <div class="stats-cards">
      <div class="card">
        <h3>Total Sales</h3>
        <p class="value">$19,626,058.20</p>
      </div>
      <div class="card">
        <h3>Total Orders</h3>
        <p class="value">3290</p>
      </div>
      <div class="card">
        <h3>Total Products</h3>
        <p class="value">322</p>
      </div>
    </div>

    <!-- Charts Section -->
    <div class="charts-section">
      <div class="sales-chart">
        <h3>Sales Statistics</h3>
        <div class="chart-placeholder">
          <!-- Placeholder for Sales Chart -->
          <p>[Sales Chart Placeholder]</p>
        </div>
      </div>
      <div class="visitors-chart">
        <h3>Visitors</h3>
        <h4>Recent Month</h4>
        <div class="pie-chart-placeholder">
          <p>9714%</p>
        </div>
        <ul class="visitor-stats">
          <li><span class="dot blue"></span> Social Media: 40%</li>
          <li><span class="dot green"></span> Affiliate Visitors: 18%</li>
          <li><span class="dot purple"></span> Purchased Visitors: 30%</li>
          <li><span class="dot teal"></span> By Advertisement: 20%</li>
        </ul>
      </div>
    </div>

    <!-- Latest Orders Table -->
    <div class="latest-orders">
      <h3>Latest Orders</h3>
      <table>
        <thead>
        <tr>
          <th>#</th>
          <th>Name</th>
          <th>Email</th>
          <th>Price</th>
          <th>Status</th>
          <th>Date</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td>2233</td>
          <td>Devon Lane</td>
          <td>devon@example.com</td>
          <td>$778.35</td>
          <td><span class="status delivered">Delivered</span></td>
          <td>07.05.2020</td>
          <td><i class="fas fa-ellipsis-h"></i></td>
        </tr>
        <tr>
          <td>2458</td>
          <td>Darrell Steward</td>
          <td>darrell@example.com</td>
          <td>$219.78</td>
          <td><span class="status delivered">Delivered</span></td>
          <td>03.07.2020</td>
          <td><i class="fas fa-ellipsis-h"></i></td>
        </tr>
        <tr>
          <td>6289</td>
          <td>Darlene Robertson</td>
          <td>darlene@example.com</td>
          <td>$928.41</td>
          <td><span class="status cancelled">Cancelled</span></td>
          <td>23.03.2020</td>
          <td><i class="fas fa-ellipsis-h"></i></td>
        </tr>
        <tr>
          <td>3869</td>
          <td>Courtney Henry</td>
          <td>courtney@example.com</td>
          <td>$90.51</td>
          <td><span class="status pending">Pending</span></td>
          <td>04.07.2020</td>
          <td><i class="fas fa-ellipsis-h"></i></td>
        </tr>
        <tr>
          <td>1247</td>
          <td>Eleanor Pena</td>
          <td>eleanor@example.com</td>
          <td>$275.43</td>
          <td><span class="status delivered">Delivered</span></td>
          <td>10.03.2020</td>
          <td><i class="fas fa-ellipsis-h"></i></td>
        </tr>
        <tr>
          <td>3961</td>
          <td>Ralph Edwards</td>
          <td>ralph@example.com</td>
          <td>$630.44</td>
          <td><span class="status delivered">Delivered</span></td>
          <td>23.03.2020</td>
          <td><i class="fas fa-ellipsis-h"></i></td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>
</body>
</html>