<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ page import="model.FileUtil" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>User Profile</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f4f4;
      margin: 0;
      padding: 20px;
    }
    .profile-container {
      max-width: 600px;
      margin: 0 auto;
      background: #fff;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    }
    .profile-container h2 {
      text-align: center;
      color: #333;
    }
    .profile-container p {
      font-size: 16px;
      color: #555;
      margin: 10px 0;
    }
    .profile-container .edit-btn {
      display: inline-block;
      margin-top: 10px;
      text-decoration: none;
      color: #fff;
      background-color: #8bc34a;
      padding: 8px 15px;
      border-radius: 5px;
    }
    .profile-container .edit-btn:hover {
      background-color: #689f38;
    }
    .back-button {
      display: block;
      text-align: center;
      margin-top: 20px;
    }
    .back-button a {
      text-decoration: none;
      color: #fff;
      background-color: #8bc34a;
      padding: 10px 20px;
      border-radius: 5px;
    }
    .back-button a:hover {
      background-color: #689f38;
    }
  </style>
</head>
<body>
<div class="profile-container">
  <h2>User Profile</h2>
  <%
    User loggedInUser = (User) session.getAttribute("user");
    String loggedInUserFile = application.getRealPath("/data/loggedInUser.txt");
    User fileUser = FileUtil.readLoggedInUser(loggedInUserFile);
    if (loggedInUser == null && fileUser == null) {
  %>
  <p>You must be logged in to view this page.</p>
  <p><a href="${pageContext.request.contextPath}/userLogin/login.jsp">Log In</a></p>
  <% } else {
    User userToDisplay = (loggedInUser != null) ? loggedInUser : fileUser;
    if (userToDisplay != null) {
  %>
  <p><strong>User Number:</strong> <%= userToDisplay.getUserNumber() != null ? userToDisplay.getUserNumber() : "Not available" %></p>
  <p><strong>Full Name:</strong> <%= userToDisplay.getFullName() %></p>
  <p><strong>Email:</strong> <%= userToDisplay.getEmail() %></p>
  <p><strong>Phone Number:</strong> <%= userToDisplay.getPhoneNumber() %></p>
  <p><strong>Address:</strong> <%= userToDisplay.getAddress() != null ? userToDisplay.getAddress() : "Not provided" %></p>
  <a href="${pageContext.request.contextPath}/userLogin/editProfile.jsp" class="edit-btn">Edit Profile</a>
  <% } else { %>
  <p>No user data available. Please log in again.</p>
  <p><a href="${pageContext.request.contextPath}/userLogin/login.jsp">Log In</a></p>
  <% }
  } %>
  <div class="back-button">
    <a href="${pageContext.request.contextPath}/index.jsp">Back to Home</a>
  </div>
</div>
</body>
</html>