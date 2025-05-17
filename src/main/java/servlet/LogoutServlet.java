package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.FileUtil;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private String LOGGED_IN_USER_FILE;

    @Override
    public void init() throws ServletException {
        String basePath = "C:\\Users\\Nimesh\\IdeaProjects\\OnlineGroceryOrderManagmentSystem\\OnlineGroceryOrderManagmentSystem\\src\\main\\webapp\\data\\admins.txt";
        LOGGED_IN_USER_FILE = basePath + "loggedInUser.txt";
        System.out.println("LogoutServlet initialized with LOGGED_IN_USER_FILE: " + LOGGED_IN_USER_FILE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Clear admin-specific session attributes
            session.removeAttribute("adminEmail");
            session.removeAttribute("adminRole");
            // Clear user-specific session attributes
            session.removeAttribute("user");
            session.invalidate();
            System.out.println("Session invalidated during logout.");
        }

        // Clear loggedInUser.txt (only used for regular users)
        try {
            FileUtil.clearLoggedInUser(LOGGED_IN_USER_FILE);
            System.out.println("Cleared loggedInUser.txt during logout.");
        } catch (Exception e) {
            System.err.println("Error clearing loggedInUser.txt during logout: " + e.getMessage());
        }

        // Get success message from query parameter
        String successMessage = request.getParameter("success");
        if (successMessage == null || successMessage.trim().isEmpty()) {
            successMessage = "Logged out successfully";
        }

        // Redirect to index.jsp with success message
        response.sendRedirect(request.getContextPath() + "/index.jsp?success=" + java.net.URLEncoder.encode(successMessage, "UTF-8"));
    }
}