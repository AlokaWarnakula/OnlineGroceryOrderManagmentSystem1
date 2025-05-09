package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.FileUtil;

import java.io.IOException;
import java.io.PrintWriter;

public class LogoutServlet extends HttpServlet {
    private String LOGGED_IN_USER_FILE;

    @Override
    public void init() throws ServletException {
        String basePath = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/";
        LOGGED_IN_USER_FILE = basePath + "loggedInUser.txt";
        System.out.println("LogoutServlet initialized with LOGGED_IN_USER_FILE: " + LOGGED_IN_USER_FILE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
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

            // Ensure no new session is created after invalidation
            request.getSession(false); // Do not create a new session

            // Get success message from query parameter
            String successMessage = request.getParameter("success");
            if (successMessage == null || successMessage.trim().isEmpty()) {
                successMessage = "Logged out successfully";
            }

            // Redirect to index.jsp with success message
            String redirectUrl = request.getContextPath() + "/index.jsp?success=" + java.net.URLEncoder.encode(successMessage, "UTF-8");
            System.out.println("Redirecting to: " + redirectUrl);
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            // Fallback in case redirect fails
            System.err.println("Error during logout redirect: " + e.getMessage());
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h2>Error During Logout</h2>");
            out.println("<p>An error occurred while redirecting. Please go to the <a href=\"" + request.getContextPath() + "/index.jsp\">home page</a>.</p>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}