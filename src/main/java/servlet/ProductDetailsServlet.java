package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.FileUtil;
import model.GroceryItem;

import java.io.IOException;
import java.util.ArrayList;

public class ProductDetailsServlet extends HttpServlet {
    // Path to items data file
    private static final String ITEMS_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/items.txt";

    // Handles GET requests to fetch item details
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get productId from request
        String productIdStr = request.getParameter("productId");
        // Validate productId state
        if (productIdStr == null || productIdStr.isEmpty()) {
            request.setAttribute("error", "Product ID is required.");
            request.getRequestDispatcher("/cartAndOrders/product-details.jsp").forward(request, response);
            return;
        }

        // Parse productId to integer
        int productId;
        try {
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Product ID.");
            request.getRequestDispatcher("/cartAndOrders/product-details.jsp").forward(request, response);
            return;
        }

        // Load items from items.txt
        ArrayList<GroceryItem> items = FileUtil.readItems(ITEMS_FILE);
        if (items == null || items.isEmpty()) {
            request.setAttribute("error", "No products available.");
            request.getRequestDispatcher("/cartAndOrders/product-details.jsp").forward(request, response);
            return;
        }

        // Find item by productId
        GroceryItem item = items.stream()
                .filter(i -> i.getProductID() == productId)
                .findFirst()
                .orElse(null);

        // Set item or error attribute
        if (item == null) {
            request.setAttribute("error", "Product not found.");
        } else {
            request.setAttribute("item", item);
            request.setAttribute("outOfStock", item.getQuantity() <= 0); // Optional, for consistency
        }

        // Forward to product-details.jsp for display
        request.getRequestDispatcher("/cartAndOrders/product-details.jsp").forward(request, response);
    }
}