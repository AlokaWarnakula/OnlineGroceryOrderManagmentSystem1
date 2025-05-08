package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import model.FileUtil;

import java.io.IOException;
import java.util.List;
import java.io.File;

public class ForgotPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String USERS_FILE = "/Users/jayashanguruge/Desktop/Project/OnlineGroceryOrderManagmentSystem-feature-logIn/src/main/webapp/data/users.txt";

    @Override
    public void init() throws ServletException {
        File file = new File(USERS_FILE);
        System.out.println("USERS_FILE path: " + USERS_FILE + ", exists: " + file.exists() + ", writable: " + file.canWrite());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Forwarding to /userLogin/forgotPassword.jsp for GET request");
        request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Trim inputs to avoid whitespace issues
        if (email != null) {
            email = email.trim();
        }
        if (password != null) {
            password = password.trim();
        }
        if (confirmPassword != null) {
            confirmPassword = confirmPassword.trim();
        }

        System.out.println("Processing password reset for email: " + email);

        // Validation
        if (email == null || email.isEmpty()) {
            request.setAttribute("error", "Email is required.");
            System.out.println("Validation failed: Email is required");
            request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
            return;
        }

        if (password == null || password.isEmpty()) {
            request.setAttribute("error", "Password is required.");
            System.out.println("Validation failed: Password is required");
            request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
            return;
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            request.setAttribute("error", "Confirm password is required.");
            System.out.println("Validation failed: Confirm password is required");
            request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords don't match.");
            System.out.println("Validation failed: Passwords don't match");
            request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
            return;
        }

        // Read users from users.txt
        List<User> users = FileUtil.readUsers(USERS_FILE);
        if (users == null) {
            System.err.println("Failed to read users file: " + USERS_FILE);
            request.setAttribute("error", "Server error. Please try again later.");
            request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
            return;
        }

        // Log parsed users for debugging
        System.out.println("Found " + users.size() + " users in users.txt");
        for (User user : users) {
            System.out.println("User: email=" + user.getEmail() + ", username=" + user.getUsername());
        }

        // Check if the email exists and update the password
        boolean emailFound = false;
        for (User user : users) {
            if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(email)) {
                emailFound = true;
                user.setPassword(password);
                System.out.println("Updated password for user: " + email);
                break;
            }
        }

        if (!emailFound) {
            request.setAttribute("error", "Email not found.");
            System.out.println("Validation failed: Email not found: " + email);
            request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
            return;
        }

        // Write the updated users back to users.txt
        try {
            FileUtil.writeUsers(USERS_FILE, users);
            System.out.println("Successfully wrote updated users to: " + USERS_FILE);
            request.setAttribute("success", "Password reset successful. You can now log in.");
        } catch (IOException e) {
            System.err.println("Failed to write to users.txt: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Failed to reset password. Please try again.");
            request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
            return;
        }

        System.out.println("Forwarding to /userLogin/forgotPassword.jsp for POST response");
        request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
    }
}