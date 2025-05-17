package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.FileUtil;
import model.GroceryItem;
import model.User;
import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CartServlet extends HttpServlet {
    // File paths for grocery items and cart data
    private static final String ITEMS_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/items.txt";
    private static final String CART_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/cart.txt";
    // MergeServlet instance for sorting items
    private MergeServlet mergeServlet;

    // Initializes the servlet
    @Override
    public void init() throws ServletException {
        // Ensure data directory exists and is writable
        File dataDir = new File("/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data");
        if (!dataDir.exists()) {
            if (!dataDir.mkdirs()) {
                throw new ServletException("Unable to create data directory");
            }
        } else if (!dataDir.canWrite()) {
            throw new ServletException("Data directory is not writable");
        }
        // Initialize MergeServlet instance for sorting
        mergeServlet = new MergeServlet();
    }

    // Handles GET requests to retrieve cart or display filtered items
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user session and check if user is logged in
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            // Redirect to login page if not authenticated
            response.sendRedirect(request.getContextPath() + "/userLogin/login.jsp?error=notLoggedIn");
            return;
        }

        // Load cart items from file, ensuring thread safety
        ArrayList<GroceryItem> cart;
        synchronized (this) {
            cart = FileUtil.readItems(CART_FILE);
            if (cart == null) {
                // Initialize empty cart if file read fails
                cart = new ArrayList<>();
            }
        }

        // Load all grocery items from file
        ArrayList<GroceryItem> items = FileUtil.readItems(ITEMS_FILE);
        if (items == null) {
            // Initialize empty list if file read fails
            items = new ArrayList<>();
        }
        // Calculate total price of cart items
        double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();

        // Check if request is for cart data (JSON response)
        String action = request.getParameter("action");
        if ("getCart".equals(action)) {
            // Send cart data as JSON
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            if (cart.isEmpty()) {
                out.println("{\"success\": true, \"message\": \"Cart is empty\", \"cart\": [], \"totalPrice\": 0.00}");
            } else {
                sendCartResponse(out, cart, totalPrice, items);
            }
            out.flush();
        } else {
            // Handle request to display cart page with filtered items
            String category = request.getParameter("category");
            String minPriceStr = request.getParameter("minPrice");
            String maxPriceStr = request.getParameter("maxPrice");
            String name = request.getParameter("name");
            String sortBy = request.getParameter("sortBy");

            // Show all items if no category or "All" is selected
            if (category == null || category.trim().isEmpty() || category.equalsIgnoreCase("All")) {
                category = null;
            }
            final String finalCategory = category;

            // Start with all items for filtering
            ArrayList<GroceryItem> filteredItems = new ArrayList<>(items);

            // Filter by category if specified
            if (finalCategory != null && !finalCategory.trim().isEmpty()) {
                filteredItems = filteredItems.stream()
                        .filter(item -> item.getProductCategory().equalsIgnoreCase(finalCategory))
                        .collect(Collectors.toCollection(ArrayList::new));
            }

            // Filter by item name if provided
            if (name != null && !name.trim().isEmpty()) {
                filteredItems = filteredItems.stream()
                        .filter(item -> item.getProductName().toLowerCase().contains(name.toLowerCase()))
                        .collect(Collectors.toCollection(ArrayList::new));
            }

            // Filter by minimum price if provided
            if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
                try {
                    double minPrice = Double.parseDouble(minPriceStr);
                    filteredItems = filteredItems.stream()
                            .filter(item -> item.getProductPrice() >= minPrice)
                            .collect(Collectors.toCollection(ArrayList::new));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid minPrice: " + minPriceStr);
                }
            }

            // Filter by maximum price if provided
            if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
                try {
                    double maxPrice = Double.parseDouble(maxPriceStr);
                    filteredItems = filteredItems.stream()
                            .filter(item -> item.getProductPrice() <= maxPrice)
                            .collect(Collectors.toCollection(ArrayList::new));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid maxPrice: " + maxPriceStr);
                }
            }

            // Sort filtered items if not empty
            if (!filteredItems.isEmpty()) {
                MergeServlet.SortCriterion sortCriterion = MergeServlet.SortCriterion.NAME;
                if (sortBy != null) {
                    switch (sortBy.toLowerCase()) {
                        case "name":
                            sortCriterion = MergeServlet.SortCriterion.NAME;
                            break;
                        case "price":
                            sortCriterion = MergeServlet.SortCriterion.PRICE;
                            break;
                    }
                }
                mergeServlet.sortItems(filteredItems, sortCriterion);
            }

            // Determine if this is a search result (name or price filters applied)
            boolean isSearchResult = (name != null && !name.trim().isEmpty()) ||
                    (minPriceStr != null && !minPriceStr.trim().isEmpty()) ||
                    (maxPriceStr != null && !maxPriceStr.trim().isEmpty());
            request.setAttribute("items", filteredItems);
            request.setAttribute("cart", cart);
            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("category", category);
            request.setAttribute("isSearchResult", isSearchResult);
            request.getRequestDispatcher("/cartAndOrders/cartIndex.jsp").forward(request, response);
        }
    }

    // Handles POST requests to add, update, or remove cart items
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verify user is logged in
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.println("{\"success\": false, \"message\": \"Please log in to access the cart.\"}");
            out.flush();
            return;
        }

        // Set response type to JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Load grocery items from file
        ArrayList<GroceryItem> cart;
        ArrayList<GroceryItem> items = FileUtil.readItems(ITEMS_FILE);
        if (items == null) {
            items = new ArrayList<>();
        }

        // Load and update cart with thread safety
        synchronized (this) {
            cart = FileUtil.readItems(CART_FILE);
            if (cart == null) {
                cart = new ArrayList<>();
            }

            try {
                // Add new item to cart
                if ("add".equals(request.getParameter("action"))) {
                    int itemId = Integer.parseInt(request.getParameter("itemId"));
                    GroceryItem itemToAdd = items.stream().filter(item -> item.getProductID() == itemId).findFirst().orElse(null);
                    if (itemToAdd == null) {
                        out.println("{\"success\": false, \"message\": \"Item not found\"}");
                    } else if (itemToAdd.getQuantity() <= 0) {
                        out.println("{\"success\": false, \"message\": \"Item is out of stock\"}");
                    } else {
                        GroceryItem existingItem = cart.stream().filter(item -> item.getProductID() == itemId).findFirst().orElse(null);
                        if (existingItem != null) {
                            out.println("{\"success\": false, \"message\": \"Item already in cart, use cart to increase quantity\"}");
                        } else {
                            // Create cart item with quantity 1
                            GroceryItem cartItem = new GroceryItem(itemToAdd.getProductID(), itemToAdd.getProductCategory(),
                                    itemToAdd.getProductName(), itemToAdd.getProductPrice(), itemToAdd.getProductImageLink(),
                                    1, itemToAdd.getDescription());
                            cart.add(cartItem);
                            // Update stock quantity
                            itemToAdd.setQuantity(itemToAdd.getQuantity() - 1);
                            FileUtil.writeItems(ITEMS_FILE, items);
                            FileUtil.writeItems(CART_FILE, cart);
                            double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();
                            sendCartResponse(out, cart, totalPrice, items);
                        }
                    }
                } else if ("update".equals(request.getParameter("action"))) {
                    // Update item quantity in cart
                    int itemId = Integer.parseInt(request.getParameter("itemId"));
                    int change = Integer.parseInt(request.getParameter("change"));
                    GroceryItem item = cart.stream().filter(i -> i.getProductID() == itemId).findFirst().orElse(null);
                    if (item == null) {
                        out.println("{\"success\": false, \"message\": \"Item not found in cart\"}");
                    } else {
                        // Check available stock
                        int stockQuantity = items.stream()
                                .filter(i -> i.getProductID() == itemId)
                                .findFirst()
                                .map(GroceryItem::getQuantity)
                                .orElse(0);
                        int currentCartQuantity = item.getQuantity();
                        int totalAvailableStock = stockQuantity + currentCartQuantity;
                        // Adjust quantity within valid range
                        int newQuantity = Math.max(0, Math.min(currentCartQuantity + change, totalAvailableStock));
                        int quantityChange = newQuantity - currentCartQuantity;
                        if (quantityChange != 0) {
                            GroceryItem stockItem = items.stream()
                                    .filter(i -> i.getProductID() == itemId)
                                    .findFirst()
                                    .orElse(null);
                            if (stockItem != null) {
                                stockItem.setQuantity(stockItem.getQuantity() - quantityChange);
                                FileUtil.writeItems(ITEMS_FILE, items);
                            }
                            item.setQuantity(newQuantity);
                            if (newQuantity == 0) {
                                cart.remove(item);
                            }
                            FileUtil.writeItems(CART_FILE, cart);
                        }
                        double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();
                        sendCartResponse(out, cart, totalPrice, items);
                    }
                } else if ("remove".equals(request.getParameter("action"))) {
                    // Remove item from cart
                    int itemId = Integer.parseInt(request.getParameter("itemId"));
                    GroceryItem itemToRemove = cart.stream().filter(item -> item.getProductID() == itemId).findFirst().orElse(null);
                    if (itemToRemove != null) {
                        int quantityRemoved = itemToRemove.getQuantity();
                        cart.remove(itemToRemove);
                        // Restore stock quantity
                        GroceryItem stockItem = items.stream()
                                .filter(item -> item.getProductID() == itemId)
                                .findFirst()
                                .orElse(null);
                        if (stockItem != null) {
                            stockItem.setQuantity(stockItem.getQuantity() + quantityRemoved);
                            FileUtil.writeItems(ITEMS_FILE, items);
                        }
                        FileUtil.writeItems(CART_FILE, cart);
                        double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();
                        sendCartResponse(out, cart, totalPrice, items);
                    } else {
                        out.println("{\"success\": false, \"message\": \"Item not found in cart\"}");
                    }
                } else {
                    // Handle invalid action
                    out.println("{\"success\": false, \"message\": \"Invalid action\"}");
                }
            } catch (NumberFormatException e) {
                // Handle invalid numeric input
                out.println("{\"success\": false, \"message\": \"Invalid item ID or change value\"}");
            } catch (IOException e) {
                // Handle file operation errors
                out.println("{\"success\": false, \"message\": \"Failed to update cart due to I/O error\"}");
            }
        }
        out.flush();
    }

    // Sends JSON response with cart details and stock information
    private void sendCartResponse(PrintWriter out, ArrayList<GroceryItem> cart, double totalPrice, ArrayList<GroceryItem> items) {
        out.println("{\"success\": true, \"message\": \"Cart retrieved successfully\", \"cart\": [");
        // Iterate through cart items
        for (int i = 0; i < cart.size(); i++) {
            GroceryItem cartItem = cart.get(i);
            // Get stock quantity for item
            int stockQuantity = items.stream()
                    .filter(item -> item.getProductID() == cartItem.getProductID())
                    .findFirst()
                    .map(GroceryItem::getQuantity)
                    .orElse(0);
            int totalAvailableStock = stockQuantity + cartItem.getQuantity();
            // Write item details as JSON
            out.println(String.format("{\"productID\": %d, \"productName\": \"%s\", \"productPrice\": %.2f, " +
                            "\"productImageLink\": \"%s\", \"quantity\": %d, \"description\": \"%s\", \"stockQuantity\": %d, \"totalAvailableStock\": %d}",
                    cartItem.getProductID(), escapeJson(cartItem.getProductName()), cartItem.getProductPrice(),
                    escapeJson(cartItem.getProductImageLink()), cartItem.getQuantity(),
                    escapeJson(cartItem.getDescription() != null ? cartItem.getDescription() : ""),
                    stockQuantity, totalAvailableStock));
            if (i < cart.size() - 1) out.println(",");
        }
        // Close JSON array and add total price
        out.println("], \"totalPrice\": " + String.format("%.2f", totalPrice) + "}");
    }

    // Escapes special characters for valid JSON output
    private String escapeJson(String str) {
        return str != null ? str.replace("\"", "\\\"").replace("\n", "\\n") : "";
    }
}