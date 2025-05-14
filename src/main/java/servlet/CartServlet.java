// Package for servlet-related classes handling HTTP requests
package servlet;

// Import Jakarta Servlet APIs for HTTP request handling and session management
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
// Import model classes for file operations, grocery items, and user data
import model.FileUtil;
import model.GroceryItem;
import model.User;
// Import Java I/O and utility classes for file operations and stream processing
import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

// CartServlet handles HTTP requests for cart operations in an online grocery system
public class CartServlet extends HttpServlet {
    // File path for storing available grocery items
    private static final String ITEMS_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/items.txt";
    // File path for storing cart items used in order creation
    private static final String CART_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/cart.txt";
    // Instance of MergeServlet for sorting grocery items
    private MergeServlet mergeServlet;

    // Initializes the servlet, ensuring the data directory exists and is writable
    @Override
    public void init() throws ServletException {
        // Create data directory if it does not exist
        File dataDir = new File("/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data");
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                // Log successful creation of data directory
                System.out.println("Created data directory: " + dataDir.getAbsolutePath());
            } else {
                // Log and throw exception if directory creation fails
                System.err.println("Failed to create data directory: " + dataDir.getAbsolutePath());
                throw new ServletException("Unable to create data directory for file operations");
            }
        } else if (!dataDir.canWrite()) {
            // Log and throw exception if directory is not writable
            System.err.println("Data directory is not writable: " + dataDir.getAbsolutePath());
            throw new ServletException("Data directory is not writable for file operations");
        }
        // Log file paths for debugging
        System.out.println("ITEMS_FILE path: " + ITEMS_FILE);
        System.out.println("CART_FILE path: " + CART_FILE);
        // Initialize MergeServlet for sorting operations
        mergeServlet = new MergeServlet();
    }

    // Handles HTTP GET requests for cart retrieval or item filtering
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the current HTTP session
        HttpSession session = request.getSession();
        // Get the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("user");
        // Redirect to login page if user is not authenticated
        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/userLogin/login.jsp?error=notLoggedIn");
            return;
        }

        // Declare list to store cart items
        ArrayList<GroceryItem> cart;
        // Synchronize to ensure thread-safe file operations
        synchronized (this) {
            // Load cart items from file
            cart = FileUtil.readItems(CART_FILE);
            if (cart == null) {
                // Initialize empty cart if file read fails
                cart = new ArrayList<>();
                // Log initialization for debugging
                System.out.println("Initialized empty cart for " + CART_FILE + " (file not found or invalid)");
            } else {
                // Log loaded cart for debugging
                System.out.println("Loaded cart from " + CART_FILE + ": " + cart);
            }
        }

        // Load available grocery items from file
        ArrayList<GroceryItem> items = FileUtil.readItems(ITEMS_FILE);
        if (items == null) {
            // Initialize empty items list if file read fails
            items = new ArrayList<>();
            // Log initialization for debugging
            System.out.println("Initialized empty items list for " + ITEMS_FILE + " (file not found or invalid)");
        }
        // Log loaded items for debugging
        System.out.println("All loaded items: " + items);
        // Calculate total cart price by summing item prices
        double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();

        // Retrieve action parameter from request
        String action = request.getParameter("action");
        // Log action for debugging
        System.out.println("doGet action: " + action);
        if ("getCart".equals(action)) {
            // Set response content type to JSON
            response.setContentType("application/json");
            // Initialize response writer
            PrintWriter out = response.getWriter();
            if (cart.isEmpty()) {
                // Send JSON response for empty cart
                out.println("{\"success\": true, \"message\": \"Cart is empty\", \"cart\": [], \"totalPrice\": 0.00}");
            } else {
                // Send JSON response with cart details
                sendCartResponse(out, cart, totalPrice, items);
            }
            // Flush output to ensure delivery
            out.flush();
        } else {
            // Retrieve filter parameters from request
            String category = request.getParameter("category");
            String minPriceStr = request.getParameter("minPrice");
            String maxPriceStr = request.getParameter("maxPrice");
            String name = request.getParameter("name");
            String sortBy = request.getParameter("sortBy");
            // Log filter parameters for debugging
            System.out.println("Parameters - category: " + category + ", minPrice: " + minPriceStr + ", maxPrice: " + maxPriceStr + ", name: " + name + ", sortBy: " + sortBy);

            // Set category to null to show all items if unspecified or "All"
            if (category == null || category.trim().isEmpty() || category.equalsIgnoreCase("All")) {
                category = null;
                // Log that all categories are displayed
                System.out.println("No specific category selected, showing all products");
            }
            // Create final category variable for lambda expressions
            final String finalCategory = category;

            // Initialize filtered items list with all items
            ArrayList<GroceryItem> filteredItems = new ArrayList<>(items);
            // Log initial item count for debugging
            System.out.println("Initial items count: " + filteredItems.size());

            // Filter items by category if specified
            if (finalCategory != null && !finalCategory.trim().isEmpty()) {
                filteredItems = filteredItems.stream()
                        // Keep items matching the specified category (case-insensitive)
                        .filter(item -> {
                            boolean matches = item.getProductCategory().equalsIgnoreCase(finalCategory);
                            // Log category check for debugging
                            System.out.println("Checking category for item " + item.getProductName() + ": " + item.getProductCategory() + " == " + finalCategory + " -> " + matches);
                            return matches;
                        })
                        // Collect filtered items into a new ArrayList
                        .collect(Collectors.toCollection(ArrayList::new));
                // Log filtered item count and list for debugging
                System.out.println("After category filter, result size: " + filteredItems.size());
                System.out.println("Items after category filter: " + filteredItems);
            }

            // Filter items by name if specified
            if (name != null && !name.trim().isEmpty()) {
                filteredItems = filteredItems.stream()
                        // Keep items with names containing the search term (case-insensitive)
                        .filter(item -> {
                            boolean matches = item.getProductName().toLowerCase().contains(name.toLowerCase());
                            // Log name check for debugging
                            System.out.println("Checking name for item " + item.getProductName() + ": contains " + name + " -> " + matches);
                            return matches;
                        })
                        // Collect filtered items into a new ArrayList
                        .collect(Collectors.toCollection(ArrayList::new));
                // Log filtered item count and list for debugging
                System.out.println("After name filter, result size: " + filteredItems.size());
                System.out.println("Items after name filter: " + filteredItems);
            }

            // Filter items by minimum price if specified
            if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
                try {
                    // Parse minimum price from string
                    double minPrice = Double.parseDouble(minPriceStr);
                    filteredItems = filteredItems.stream()
                            // Keep items with price at least the minimum
                            .filter(item -> {
                                boolean matches = item.getProductPrice() >= minPrice;
                                // Log price check for debugging
                                System.out.println("Checking minPrice for item " + item.getProductName() + ": " + item.getProductPrice() + " >= " + minPrice + " -> " + matches);
                                return matches;
                            })
                            // Collect filtered items into a new ArrayList
                            .collect(Collectors.toCollection(ArrayList::new));
                    // Log filtered item count and list for debugging
                    System.out.println("After minPrice filter (" + minPrice + "), result size: " + filteredItems.size());
                    System.out.println("Items after minPrice filter: " + filteredItems);
                } catch (NumberFormatException e) {
                    // Log invalid minimum price for debugging
                    System.out.println("Invalid minPrice: " + minPriceStr);
                }
            }

            // Filter items by maximum price if specified
            if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
                try {
                    // Parse maximum price from string
                    double maxPrice = Double.parseDouble(maxPriceStr);
                    filteredItems = filteredItems.stream()
                            // Keep items with price at most the maximum
                            .filter(item -> {
                                boolean matches = item.getProductPrice() <= maxPrice;
                                // Log price check for debugging
                                System.out.println("Checking maxPrice for item " + item.getProductName() + ": " + item.getProductPrice() + " <= " + maxPrice + " -> " + matches);
                                return matches;
                            })
                            // Collect filtered items into a new ArrayList
                            .collect(Collectors.toCollection(ArrayList::new));
                    // Log filtered item count and list for debugging
                    System.out.println("After maxPrice filter (" + maxPrice + "), result size: " + filteredItems.size());
                    System.out.println("Items after maxPrice filter: " + filteredItems);
                } catch (NumberFormatException e) {
                    // Log invalid maximum price for debugging
                    System.out.println("Invalid maxPrice: " + maxPriceStr);
                }
            }

            // Sort filtered items if the list is not empty
            if (!filteredItems.isEmpty()) {
                // Default to sorting by name
                MergeServlet.SortCriterion sortCriterion = MergeServlet.SortCriterion.NAME;
                // Map sortBy parameter to sorting criterion
                if (sortBy != null) {
                    switch (sortBy.toLowerCase()) {
                        case "name":
                            sortCriterion = MergeServlet.SortCriterion.NAME;
                            break;
                        case "price":
                            sortCriterion = MergeServlet.SortCriterion.PRICE;
                            break;
                        default:
                            // Log invalid sort parameter and default to name
                            System.out.println("Invalid sortBy parameter: " + sortBy + ", defaulting to NAME");
                    }
                }
                // Log sorting criterion for debugging
                System.out.println("Sorting items with sortCriterion=" + sortCriterion);
                // Sort items using MergeServlet
                mergeServlet.sortItems(filteredItems, sortCriterion);
            }

            // Determine if the response is a search result (name or price filters applied)
            boolean isSearchResult = (name != null && !name.trim().isEmpty()) ||
                    (minPriceStr != null && !minPriceStr.trim().isEmpty()) ||
                    (maxPriceStr != null && !maxPriceStr.trim().isEmpty());
            // Set attributes for the JSP page
            request.setAttribute("items", filteredItems);
            request.setAttribute("cart", cart);
            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("category", category);
            request.setAttribute("isSearchResult", isSearchResult);
            // Log forwarding details for debugging
            System.out.println("Forwarding to cartIndex.jsp with items size: " + filteredItems.size() + ", isSearchResult: " + isSearchResult);
            // Forward request to cartIndex.jsp for rendering
            request.getRequestDispatcher("/cartAndOrders/cartIndex.jsp").forward(request, response);
        }
    }

    // Handles HTTP POST requests for adding, updating, or removing cart items
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the current HTTP session
        HttpSession session = request.getSession();
        // Get the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("user");
        // Require authentication for cart operations
        if (loggedInUser == null) {
            // Set response content type to JSON
            response.setContentType("application/json");
            // Initialize response writer
            PrintWriter out = response.getWriter();
            // Send JSON error response for unauthenticated access
            out.println("{\"success\": false, \"message\": \"Please log in to access the cart.\"}");
            // Flush output to ensure delivery
            out.flush();
            return;
        }

        // Log POST request action for debugging
        System.out.println("Received POST request to CartServlet with action: " + request.getParameter("action"));
        // Set response content type to JSON
        response.setContentType("application/json");
        // Initialize response writer
        PrintWriter out = response.getWriter();

        // Declare list to store cart items
        ArrayList<GroceryItem> cart;
        // Load available grocery items from file
        ArrayList<GroceryItem> items = FileUtil.readItems(ITEMS_FILE);
        if (items == null) {
            // Initialize empty items list if file read fails
            items = new ArrayList<>();
            // Log initialization for debugging
            System.out.println("Initialized empty items list for " + ITEMS_FILE + " (file not found or invalid)");
        }
        // Log loaded items for debugging
        System.out.println("Loaded items from " + ITEMS_FILE + ": " + items);

        // Synchronize to ensure thread-safe cart operations
        synchronized (this) {
            // Load cart items from file
            cart = FileUtil.readItems(CART_FILE);
            if (cart == null) {
                // Initialize empty cart if file read fails
                cart = new ArrayList<>();
                // Log initialization for debugging
                System.out.println("Initialized empty cart for " + CART_FILE);
            }
            // Log loaded cart for debugging
            System.out.println("Loaded cart before modification: " + cart);

            try {
                // Handle add action to include a new item in the cart
                if ("add".equals(request.getParameter("action"))) {
                    // Log item ID for debugging
                    System.out.println("Processing 'add' action with itemId: " + request.getParameter("itemId"));
                    // Parse item ID from request
                    int itemId = Integer.parseInt(request.getParameter("itemId"));
                    // Find item by ID in the items list
                    GroceryItem itemToAdd = items.stream().filter(item -> item.getProductID() == itemId).findFirst().orElse(null);
                    if (itemToAdd == null) {
                        // Log and send error if item is not found
                        System.out.println("Item not found for itemId: " + itemId);
                        out.println("{\"success\": false, \"message\": \"Item not found\"}");
                    } else if (itemToAdd.getQuantity() <= 0) {
                        // Log and send error if item is out of stock
                        System.out.println("Item out of stock: " + itemToAdd);
                        out.println("{\"success\": false, \"message\": \"Item is out of stock\"}");
                    } else {
                        // Check if item is already in the cart
                        GroceryItem existingItem = cart.stream().filter(item -> item.getProductID() == itemId).findFirst().orElse(null);
                        if (existingItem != null) {
                            // Send error if item is already in cart
                            out.println("{\"success\": false, \"message\": \"Item already in cart, use cart to increase quantity\"}");
                        } else {
                            // Create new cart item with quantity 1
                            GroceryItem cartItem = new GroceryItem(itemToAdd.getProductID(), itemToAdd.getProductCategory(),
                                    itemToAdd.getProductName(), itemToAdd.getProductPrice(), itemToAdd.getProductImageLink(),
                                    1, itemToAdd.getDescription());
                            // Add item to cart
                            cart.add(cartItem);
                            // Decrease stock quantity
                            itemToAdd.setQuantity(itemToAdd.getQuantity() - 1);
                            // Update items file
                            FileUtil.writeItems(ITEMS_FILE, items);
                            // Update cart file
                            FileUtil.writeItems(CART_FILE, cart);
                            // Log addition for debugging
                            System.out.println("Added new item to cart and updated stock: " + cartItem);
                            // Calculate total cart price
                            double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();
                            // Send JSON response with updated cart
                            sendCartResponse(out, cart, totalPrice, items);
                        }
                    }
                } else if ("update".equals(request.getParameter("action"))) {
                    // Log item ID for debugging
                    System.out.println("Processing 'update' action with itemId: " + request.getParameter("itemId"));
                    // Parse item ID and quantity change
                    int itemId = Integer.parseInt(request.getParameter("itemId"));
                    int change = Integer.parseInt(request.getParameter("change"));
                    // Find item in cart by ID
                    GroceryItem item = cart.stream().filter(i -> i.getProductID() == itemId).findFirst().orElse(null);
                    if (item == null) {
                        // Log and send error if item is not in cart
                        System.out.println("Item not found in cart for itemId: " + itemId);
                        out.println("{\"success\": false, \"message\": \"Item not found in cart\"}");
                    } else {
                        // Get stock quantity for the item
                        int stockQuantity = items.stream()
                                .filter(i -> i.getProductID() == itemId)
                                .findFirst()
                                .map(GroceryItem::getQuantity)
                                .orElse(0);
                        // Calculate available stock
                        int currentCartQuantity = item.getQuantity();
                        int totalAvailableStock = stockQuantity + currentCartQuantity;
                        // Adjust quantity within valid range
                        int newQuantity = Math.max(0, Math.min(currentCartQuantity + change, totalAvailableStock));
                        int quantityChange = newQuantity - currentCartQuantity;
                        if (quantityChange != 0) {
                            // Update stock quantity
                            GroceryItem stockItem = items.stream()
                                    .filter(i -> i.getProductID() == itemId)
                                    .findFirst()
                                    .orElse(null);
                            if (stockItem != null) {
                                stockItem.setQuantity(stockItem.getQuantity() - quantityChange);
                                // Update items file
                                FileUtil.writeItems(ITEMS_FILE, items);
                                // Log stock update
                                System.out.println("Updated stock in " + ITEMS_FILE + ": " + items);
                            }
                            // Update cart quantity
                            item.setQuantity(newQuantity);
                            if (newQuantity == 0) {
                                // Remove item if quantity is zero
                                cart.remove(item);
                                // Log removal
                                System.out.println("Removed item from cart: " + item);
                            } else {
                                // Log quantity update
                                System.out.println("Updated item quantity in cart: " + item);
                            }
                            // Update cart file
                            FileUtil.writeItems(CART_FILE, cart);
                            // Log cart update
                            System.out.println("Wrote cart to " + CART_FILE + ": " + cart);
                        }
                        // Calculate total cart price
                        double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();
                        // Send JSON response with updated cart
                        sendCartResponse(out, cart, totalPrice, items);
                    }
                } else if ("remove".equals(request.getParameter("action"))) {
                    // Log item ID for debugging
                    System.out.println("Processing 'remove' action with itemId: " + request.getParameter("itemId"));
                    // Parse item ID
                    int itemId = Integer.parseInt(request.getParameter("itemId"));
                    // Find item in cart by ID
                    GroceryItem itemToRemove = cart.stream().filter(item -> item.getProductID() == itemId).findFirst().orElse(null);
                    if (itemToRemove != null) {
                        // Restore stock quantity
                        int quantityRemoved = itemToRemove.getQuantity();
                        cart.remove(itemToRemove);
                        GroceryItem stockItem = items.stream()
                                .filter(item -> item.getProductID() == itemId)
                                .findFirst()
                                .orElse(null);
                        if (stockItem != null) {
                            stockItem.setQuantity(stockItem.getQuantity() + quantityRemoved);
                            // Update items file
                            FileUtil.writeItems(ITEMS_FILE, items);
                            // Log stock update
                            System.out.println("Updated stock in " + ITEMS_FILE + ": " + items);
                        }
                        // Update cart file
                        FileUtil.writeItems(CART_FILE, cart);
                        // Log cart update
                        System.out.println("Wrote cart to " + CART_FILE + ": " + cart);
                        // Calculate total cart price
                        double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();
                        // Send JSON response with updated cart
                        sendCartResponse(out, cart, totalPrice, items);
                    } else {
                        // Log and send error if item is not in cart
                        System.out.println("Item not found in cart for itemId: " + itemId);
                        out.println("{\"success\": false, \"message\": \"Item not found in cart\"}");
                    }
                } else {
                    // Log and send error for invalid action
                    System.out.println("Invalid action received: " + request.getParameter("action"));
                    out.println("{\"success\": false, \"message\": \"Invalid action\"}");
                }
            } catch (NumberFormatException e) {
                // Log and send error for invalid numeric input
                System.out.println("Invalid item ID or change value: " + e.getMessage());
                out.println("{\"success\": false, \"message\": \"Invalid item ID or change value\"}");
            } catch (IOException e) {
                // Log and send error for file operation failures
                System.err.println("I/O error during cart operation: " + e.getMessage());
                out.println("{\"success\": false, \"message\": \"Failed to update cart due to I/O error\"}");
            }
        }
        // Flush output to ensure delivery
        out.flush();
    }

    // Generates a JSON response with cart details and stock information
    private void sendCartResponse(PrintWriter out, ArrayList<GroceryItem> cart, double totalPrice, ArrayList<GroceryItem> items) {
        // Start JSON response with success status and cart array
        out.println("{\"success\": true, \"message\": \"Cart retrieved successfully\", \"cart\": [");
        // Iterate through cart items
        for (int i = 0; i < cart.size(); i++) {
            // Get current cart item
            GroceryItem cartItem = cart.get(i);
            // Retrieve stock quantity for the item
            int stockQuantity = items.stream()
                    .filter(item -> item.getProductID() == cartItem.getProductID())
                    .findFirst()
                    .map(GroceryItem::getQuantity)
                    .orElse(0);
            // Calculate total available stock
            int totalAvailableStock = stockQuantity + cartItem.getQuantity();
            // Write JSON object for the cart item
            out.println(String.format("{\"productID\": %d, \"productName\": \"%s\", \"productPrice\": %.2f, " +
                            "\"productImageLink\": \"%s\", \"quantity\": %d, \"description\": \"%s\", \"stockQuantity\": %d, \"totalAvailableStock\": %d}",
                    cartItem.getProductID(), escapeJson(cartItem.getProductName()), cartItem.getProductPrice(),
                    escapeJson(cartItem.getProductImageLink()), cartItem.getQuantity(),
                    escapeJson(cartItem.getDescription() != null ? cartItem.getDescription() : ""),
                    stockQuantity, totalAvailableStock));
            // Add comma between items except for the last
            if (i < cart.size() - 1) out.println(",");
        }
        // Close cart array and add total price
        out.println("], \"totalPrice\": " + String.format("%.2f", totalPrice) + "}");
    }

    // Escapes special characters in strings for valid JSON output
    private String escapeJson(String str) {
        // Return empty string if null, otherwise escape quotes and newlines
        return str != null ? str.replace("\"", "\\\"").replace("\n", "\\n") : "";
    }
}