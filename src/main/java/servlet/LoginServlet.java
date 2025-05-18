package servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Admin;
import model.FileUtil;
import model.User;
import java.util.List;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private String LOGGED_IN_USER_FILE;
    private String USERS_FILE;
    private String ADMINS_FILE;

    @Override
    public void init() throws ServletException {
        String basePath = "/Users/jayashanguruge/Desktop/OnlineGroceryOrderSystem/src/main/webapp/data";
        LOGGED_IN_USER_FILE = basePath + "/loggedInUser.txt";
        USERS_FILE = basePath + "/users.txt";
        ADMINS_FILE = basePath + "/admins.txt";
        System.out.println("LOGGED_IN_USER_FILE path: " + this.LOGGED_IN_USER_FILE);
        System.out.println("USERS_FILE path: " + this.USERS_FILE);
        System.out.println("ADMINS_FILE path: " + this.ADMINS_FILE);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String servletPath = request.getServletPath();

        // Handle forgot password form submission
        if ("/forgotPassword".equals(servletPath)) {
            if (email == null || password == null || confirmPassword == null) {
                System.out.println("Email, password, or confirm password missing: email=" + email);
                request.setAttribute("error", "All fields are required.");
                request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
                return;
            }

            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match for email: " + email);
                request.setAttribute("error", "Passwords do not match.");
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

            // Find user by email
            User targetUser = null;
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    targetUser = user;
                    break;
                }
            }

            if (targetUser == null) {
                System.out.println("No user found with email: " + email);
                request.setAttribute("error", "No account found with this email.");
                request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
                return;
            }

            // Update user's password
            targetUser.setPassword(password);
            try {
                FileUtil.writeUsers(USERS_FILE, users);
                System.out.println("Password updated for user: " + email);
                request.setAttribute("success", "Password reset successfully. Please login with your new password.");
                request.getRequestDispatcher("/userLogin/Loading.jsp").forward(request, response);//Loading.jsp
            } catch (IOException e) {
                System.err.println("Error writing updated users to file: " + e.getMessage());
                request.setAttribute("error", "Error updating password. Please try again later.");
                request.getRequestDispatcher("/userLogin/forgotPassword.jsp").forward(request, response);
            }
            return;
        }

        // Existing login logic
        if (email == null || password == null) {
            System.out.println("Email or password missing: email=" + email + ", password=" + password);
            request.setAttribute("error", "Email and password are required.");
            request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
            return;
        }

        System.out.println("Attempting login for email: " + email);
        HttpSession session = request.getSession();

        // Check if the email is for an admin (ends with .admin)
        if (email.toLowerCase().endsWith(".admin")) {
            // Admin login flow
            List<Admin> admins = FileUtil.readAdmins(ADMINS_FILE);
            if (admins == null) {
                System.err.println("Failed to read admins file: " + ADMINS_FILE);
                request.setAttribute("error", "Server error. Please try again later.");
                request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
                return;
            }

             for (Admin admin : admins) {
                if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
                    System.out.println("Admin authenticated: " + admin.toString());
                    System.out.println("setting adminNumber in session: " + admin.getAdminNumber());
                    System.out.println("setting adminEmail in session: " + admin.getEmail());
                    System.out.println("setting adminRole in session: " + admin.getRole());
                    session.setAttribute("adminEmail", admin.getEmail());
                    session.setAttribute("adminNumber", admin.getAdminNumber());
                    session.setAttribute("adminRole", admin.getRole());
                    response.sendRedirect(request.getContextPath() + "/adminPages/adminSuccessful.jsp?type=login");
                    return;
                }
            }

            // Admin authentication failed
            System.out.println("Admin authentication failed for email: " + email);
            request.setAttribute("error", "Invalid email or password.");
            request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
        } else {
            // Regular user login flow
            List<User> users = FileUtil.readUsers(USERS_FILE);
            if (users == null) {
                System.err.println("Failed to read users file: " + USERS_FILE);
                request.setAttribute("error", "Server error. Please try again later.");
                request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
                return;
            }

            for (User user : users) {
                if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                    System.out.println("User authenticated: " + user.toString());
                    session.setAttribute("user", user);
                    try {
                        FileUtil.writeLoggedInUser(LOGGED_IN_USER_FILE, user);
                        System.out.println("Successfully wrote to loggedInUser.txt for user: " + user.getEmail());
                    } catch (Exception e) {
                        System.err.println("Failed to write to loggedInUser.txt: " + e.getMessage());
                    }
                   finally {
                        response.sendRedirect(request.getContextPath() + "/userLogin/Success.jsp?type=login");
                        return;
                    }
                }
            }

            // User authentication failed
            System.out.println("User authentication failed for email: " + email);
            request.setAttribute("error", "Invalid email or password.");
            request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
        }
    }
}