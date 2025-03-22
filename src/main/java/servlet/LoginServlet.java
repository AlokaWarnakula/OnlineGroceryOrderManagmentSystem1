package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.FileUtil;
import model.User;

import java.io.File;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private String LOGGED_IN_USER_FILE;
    private String USERS_FILE;

    @Override
    public void init() throws ServletException {
        String basePath = "/Users/jayashanguruge/Desktop/Project/OnlineGroceryOrderManagmentSystem-feature-logIn/src/main/webapp/data";
        LOGGED_IN_USER_FILE = basePath + "/loggedInUser.txt"; // Use forward slash for consistency
        USERS_FILE = basePath + "/users.txt";

        // Verify file accessibility during initialization
        File usersFile = new File(USERS_FILE);
        System.out.println("USERS_FILE path: " + USERS_FILE + " | Exists: " + usersFile.exists() + " | Readable: " + usersFile.canRead());
        System.out.println("LOGGED_IN_USER_FILE path: " + LOGGED_IN_USER_FILE);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Trim input to avoid whitespace issues
        String email = request.getParameter("email") != null ? request.getParameter("email").trim() : null;
        String password = request.getParameter("password") != null ? request.getParameter("password").trim() : null;

        // Log the input for debugging
        System.out.println("Login attempt - Email: '" + email + "', Password: '" + password + "'");

        // Check for null or empty input
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            System.out.println("Email or password missing or empty: email='" + email + "', password='" + password + "'");
            request.setAttribute("error", "Email and password are required.");
            request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
            return;
        }

        // Read users from file and attempt authentication
        boolean userFound = false;
        for (User user : FileUtil.readUsers(USERS_FILE)) {
            System.out.println("Checking user - Email: '" + user.getEmail() + "', Password: '" + user.getPassword() + "'");
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                userFound = true;
                System.out.println("User authenticated: " + user.toString());

                // Create session and store user
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                // Write to loggedInUser.txt
                try {
                    FileUtil.writeLoggedInUser(LOGGED_IN_USER_FILE, user);
                    System.out.println("Successfully wrote to loggedInUser.txt for user: " + user.getEmail());
                } catch (Exception e) {
                    System.err.println("Failed to write to loggedInUser.txt: " + e.getMessage());
                    e.printStackTrace();
                }

                // Redirect to Success.jsp
                response.sendRedirect(request.getContextPath() + "/userLogin/Success.jsp?type=login");
                return;
            }
        }

        // If no match is found
        if (!userFound) {
            System.out.println("Authentication failed for email: '" + email + "'");
            request.setAttribute("error", "Invalid email or password.");
            request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
        }
    }
}