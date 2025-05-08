package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.FileUtil;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class RegisterServlet extends HttpServlet {
    private static final String USERS_FILE = "/Users/jayashanguruge/Desktop/Project/OnlineGroceryOrderManagmentSystem-feature-logIn/src/main/webapp/data/users.txt";
    private static final String LOGGED_IN_USER_FILE = "/Users/jayashanguruge/Desktop/Project/OnlineGroceryOrderManagmentSystem-feature-logIn/src/main/webapp/data/loggedInUser.txt";
    // Password strength pattern: at least 8 chars, 1 upper, 1 lower, 1 digit, 1 special char
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String password = request.getParameter("password");

        // Trim inputs to avoid whitespace issues
        if (fullName != null) fullName = fullName.trim();
        if (email != null) email = email.trim();
        if (phoneNumber != null) phoneNumber = phoneNumber.trim();
        if (address != null) address = address.trim();
        if (password != null) password = password.trim();

        // Basic validation
        if (fullName == null || email == null || phoneNumber == null || address == null || password == null ||
                fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
            return;
        }

        // Password strength validation
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            request.setAttribute("error", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character (@$!%*?&).");
            System.out.println("Validation failed: Password does not meet strength requirements");
            request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
            return;
        }

        // Read existing users
        List<User> users = FileUtil.readUsers(USERS_FILE);
        if (users == null) {
            request.setAttribute("error", "Error reading user data. Please try again later.");
            request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
            return;
        }

        System.out.println("Checking email: " + email + ", existing users: " + users);
        String finalEmail = email;
        if (users.stream().anyMatch(u -> u.getEmail().equals(finalEmail))) {
            System.out.println("Email already registered: " + email);
            request.setAttribute("error", "Email already registered.");
            request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
            return;
        }

        // Generate unique user number
        String userNumber;
        do {
            userNumber = "US" + String.format("%012d", Math.abs(new Random().nextLong() % 1000000000000L));
        } while (!FileUtil.isUserNumberUnique(userNumber, USERS_FILE));

        // Use email as username
        String username = email;
        User user = new User(username, password, userNumber, fullName, email, phoneNumber, address);

        // Add the new user to the list and write to users.txt
        synchronized (this) {
            users.add(user);
            try {
                FileUtil.writeUsers(USERS_FILE, users);
                System.out.println("Added new user to users.txt: " + user.toString());
            } catch (IOException e) {
                System.err.println("Error writing user to file: " + e.getMessage());
                request.setAttribute("error", "Error saving user data. Please try again later.");
                request.getRequestDispatcher("/userLogin/login.jsp").forward(request, response);
                return;
            }
        }

        // Set user in session
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        System.out.println("Set user in session: " + user.toString());

        // Write to loggedInUser.txt
        FileUtil.writeLoggedInUser(LOGGED_IN_USER_FILE, user);
        System.out.println("Wrote new user to loggedInUser.txt: " + user.toString());

        // Redirect to success page
        response.sendRedirect(request.getContextPath() + "/userLogin/Success.jsp?type=signup");
    }
}