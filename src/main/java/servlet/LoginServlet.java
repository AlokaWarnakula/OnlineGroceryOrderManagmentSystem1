// Package for servlet-related classes handling HTTP requests
package servlet;

// Import Jakarta Servlet APIs for HTTP request handling and session management
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
// Import model classes for file operations and user data
import model.FileUtil;
import model.User;
// Import Java I/O and utility classes for file operations and list processing
import java.io.IOException;
import java.util.List;

// LoginServlet handles user authentication and password reset for an online grocery system
public class LoginServlet extends HttpServlet {
    // File path for storing the currently logged-in user
    private String LOGGED_IN_USER_FILE;
    // File path for storing user data
    private String USERS_FILE;

    // Initializes the servlet, setting file paths for user data
    @Override
    public void init() throws ServletException {
        // Define base path for data files
        String basePath = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/";
        LOGGED_IN_USER_FILE = basePath + "loggedInUser.txt";
        USERS_FILE = basePath + "users.txt";
        // Log file paths for debugging
        System.out.println("LOGGED_IN_USER_FILE path: " + LOGGED_IN_USER_FILE);
        System.out.println("USERS_FILE path: " + USERS_FILE);
    }

    // Handles HTTP POST requests for user login and password reset
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String servletPath = request.getServletPath();

        // Handle forgot password form submission
        if ("/forgotPassword".equals(servletPath)) {
            // Validate required fields
            if (email == null || password == null || confirmPassword == null) {
                // Log missing fields for debugging
                System.out.println("Email, password, or confirm password missing: email=" + email);
                request.setAttribute("error", "All fields are required.");
                request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
                return;
            }

            // Verify password match
            if (!password.equals(confirmPassword)) {
                // Log password mismatch for debugging
                System.out.println("Passwords do not match for email: " + email);
                request.setAttribute("error", "Passwords do not match.");
                request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
                return;
            }

            // Load users from file
            List<User> users = FileUtil.readUsers(USERS_FILE);
            if (users == null) {
                // Log file read failure
                System.err.println("Failed to read users file: " + USERS_FILE);
                request.setAttribute("error", "Server error. Please try again later.");
                request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
                return;
            }

            // Find user by email
            User targetUser = null;
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    targetUser = user;
                    break;
                }
            }

            // Handle user not found
            if (targetUser == null) {
                // Log missing user for debugging
                System.out.println("No user found with email: " + email);
                request.setAttribute("error", "No account found with this email.");
                request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
                return;
            }

            // Update user password
            targetUser.setPassword(password);
            try {
                FileUtil.writeUsers(USERS_FILE, users);
                // Log successful password update
                System.out.println("Password updated for user: " + email);
                request.setAttribute("success", "Password reset successfully. Please login with your new password.");
                request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
            } catch (IOException e) {
                // Log file write error
                System.err.println("Error writing updated users to file: " + e.getMessage());
                request.setAttribute("error", "Error updating password. Please try again later.");
                request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
            }
            return;
        }

        // Handle user login
        // Validate required fields
        if (email == null || password == null) {
            // Log missing fields for debugging
            System.out.println("Email or password missing: email=" + email + ", password=" + password);
            request.setAttribute("error", "Email and password are required.");
            request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
            return;
        }

        // Log login attempt
        System.out.println("Attempting login for email: " + email);
        // Initialize session
        HttpSession session = request.getSession();

        // Load users from file
        List<User> users = FileUtil.readUsers(USERS_FILE);
        if (users == null) {
            // Log file read failure
            System.err.println("Failed to read users file: " + USERS_FILE);
            request.setAttribute("error", "Server error. Please try again later.");
            request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
            return;
        }

        // Authenticate user
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                // Log successful authentication
                System.out.println("User authenticated: " + user.toString());
                // Store user in session
                session.setAttribute("user", user);
                try {
                    // Save logged-in user to file
                    FileUtil.writeLoggedInUser(LOGGED_IN_USER_FILE, user);
                    // Log successful file write
                    System.out.println("Successfully wrote to loggedInUser.txt for user: " + user.getEmail());
                } catch (Exception e) {
                    // Log file write error
                    System.err.println("Failed to write to loggedInUser.txt: " + e.getMessage());
                    e.printStackTrace();
                }
                // Redirect to success page
                response.sendRedirect(request.getContextPath() + "/userLogin/Success.jsp?type=login");
                return;
            }
        }

        // Handle authentication failure
        // Log failed authentication
        System.out.println("User authentication failed for email: " + email);
        request.setAttribute("error", "Invalid email or password.");
        request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
    }
}