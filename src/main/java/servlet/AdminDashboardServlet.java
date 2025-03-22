package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.FileUtil;

import java.io.IOException;
import java.util.List;

public class AdminDashboardServlet extends HttpServlet {
    private String ORDERS_DIR;

    @Override
    public void init() throws ServletException {
        String basePath = "/Users/jayashanguruge/Desktop/Project/OnlineGroceryOrderManagmentSystem-feature-logIn/src/main/webapp/data";
        ORDERS_DIR = basePath + "/orders";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch all orders
        List<FileUtil.Order> orders = FileUtil.readAllOrders(ORDERS_DIR);

        // Set orders as a request attribute
        request.setAttribute("orders", orders);

        // Forward to admindashboard.jsp
        request.getRequestDispatcher("/admindashboard.jsp").forward(request, response);
    }
}