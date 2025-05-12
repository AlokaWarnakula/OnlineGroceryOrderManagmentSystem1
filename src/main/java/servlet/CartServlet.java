package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
// import model classes
import model.FileUtil;
import model.GroceryItem;
import model.User;
//import I/O and utility classes
import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CartServlet extends HttpServlet {
    private static final String ITEMS_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/items.txt"; // File location for storing info related to items in shop
    private static final String CART_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/cart.txt"; // Use for cart -> order creating
    private MergeServlet mergeServlet; // Instance of MergeServlet for sorting items

    @Override
    public void init() throws ServletException { // check data directory exist if not create one
        File dataDir = new File("/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data");
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                System.out.println("Created data directory: " + dataDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create data directory: " + dataDir.getAbsolutePath());
                throw new ServletException("Unable to create data directory for file operations");
            }
        } else if (!dataDir.canWrite()) {
            System.err.println("Data directory is not writable: " + dataDir.getAbsolutePath());
            throw new ServletException("Data directory is not writable for file operations");
        }
        //print log file path for easy debugging
        System.out.println("ITEMS_FILE path: " + ITEMS_FILE);
        System.out.println("CART_FILE path: " + CART_FILE);
        mergeServlet = new MergeServlet(); // Instance of MergeServlet for sorting items
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/userLogin/login.jsp?error=notLoggedIn");
            return;
        } //get user from session if the use not redirect to log in

        ArrayList<GroceryItem> cart; //store GroceryItem objects for the cart
        synchronized (this) { // thread-safe access to file operations
            cart = FileUtil.readItems(CART_FILE);
            if (cart == null) {// if cart empty make new one and log
                cart = new ArrayList<>();
                System.out.println("Initialized empty cart for " + CART_FILE + " (file not found or invalid)");
            } else { // tell cart loaded with content
                System.out.println("Loaded cart from " + CART_FILE + ": " + cart);
            }
        }

        ArrayList<GroceryItem> items = FileUtil.readItems(ITEMS_FILE); // items file read
        if (items == null) { // if items null init new array
            items = new ArrayList<>();
            System.out.println("Initialized empty items list for " + ITEMS_FILE + " (file not found or invalid)");
        }
        System.out.println("All loaded items: " + items); // for debug
        double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum(); // calculate total price of items in cart by summing individual prices

        String action = request.getParameter("action"); // get action from http request
        System.out.println("doGet action: " + action);
        if ("getCart".equals(action)) { // return cart details as JSON
            response.setContentType("application/json"); //content type to JSON
            PrintWriter out = response.getWriter();
            if (cart.isEmpty()) { // cart is empty, send an empty cart JSON response
                out.write("{\"success\": true, \"message\": \"Cart is empty\", \"cart\": [], \"totalPrice\": 0.00}");
            } else {
                sendCartResponse(out, cart, totalPrice, items); // Send cart details if cart is not empty
            }
            out.flush(); // ensure it is sent to the client
        } else {
            //get filter parameters from the request
            String category = request.getParameter("category");
            String minPriceStr = request.getParameter("minPrice");
            String maxPriceStr = request.getParameter("maxPrice");
            String name = request.getParameter("name");
            String sortBy = request.getParameter("sortBy"); // Primary sorting criterion
            //log printer para for debug
            System.out.println("Parameters - category: " + category + ", minPrice: " + minPriceStr + ", maxPrice: " + maxPriceStr + ", name: " + name + ", sortBy: " + sortBy);

            // default category to null -> show all if not specified or "All"
            if (category == null || category.trim().isEmpty() || category.equalsIgnoreCase("All")) {
                category = null; // show all categories
                System.out.println("No specific category selected, showing all products");
            }
            final String finalCategory = category; // Create a final copy for lambda expression

            ArrayList<GroceryItem> filteredItems = new ArrayList<>(items); // new ArrayList with all items for filtering
            System.out.println("Initial items count: " + filteredItems.size());

            // filter by category if specified
            if (finalCategory != null && !finalCategory.trim().isEmpty()) {
                filteredItems = filteredItems.stream()
                        // filter items where category matches the specified category (case-insensitive)
                        .filter(item -> { // lambda expression
                            boolean matches = item.getProductCategory().equalsIgnoreCase(finalCategory);
                            System.out.println("Checking category for item " + item.getProductName() + ": " + item.getProductCategory() + " == " + finalCategory + " -> " + matches);
                            return matches;
                        })
                        // add filtered items into a new ArrayList
                        .collect(Collectors.toCollection(ArrayList::new));
                System.out.println("After category filter, result size: " + filteredItems.size());
                System.out.println("Items after category filter: " + filteredItems);
            }

            // filter by name if specified
            if (name != null && !name.trim().isEmpty()) {
                filteredItems = filteredItems.stream()
                        // filter items where product name contains the specified name (case-insensitive)
                        .filter(item -> {
                            boolean matches = item.getProductName().toLowerCase().contains(name.toLowerCase());
                            System.out.println("Checking name for item " + item.getProductName() + ": contains " + name + " -> " + matches);
                            return matches;
                        })
                        // add filtered items into a new ArrayList
                        .collect(Collectors.toCollection(ArrayList::new));
                // print number of items and filtered list after name filter
                System.out.println("After name filter, result size: " + filteredItems.size());
                System.out.println("Items after name filter: " + filteredItems);
            }

            // filter by price range apply minPrice filter
            if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
                try {
                    // parse the minimum price as a double
                    double minPrice = Double.parseDouble(minPriceStr);
                    filteredItems = filteredItems.stream()
                            // filter items where price is greater than or equal to minPrice
                            .filter(item -> {
                                boolean matches = item.getProductPrice() >= minPrice;
                                System.out.println("Checking minPrice for item " + item.getProductName() + ": " + item.getProductPrice() + " >= " + minPrice + " -> " + matches);
                                return matches;
                            })
                            // add filtered items into a new ArrayList
                            .collect(Collectors.toCollection(ArrayList::new));
                    // print number of items and filtered list after minPrice filter
                    System.out.println("After minPrice filter (" + minPrice + "), result size: " + filteredItems.size());
                    System.out.println("Items after minPrice filter: " + filteredItems);
                } catch (NumberFormatException e) {
                    // print error if minPrice is invalid
                    System.out.println("Invalid minPrice: " + minPriceStr);
                }
            }

            // maxPrice filter
            if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
                try {
                    // parse the maximum price as a double
                    double maxPrice = Double.parseDouble(maxPriceStr);
                    filteredItems = filteredItems.stream()
                            // filter items where price is less than or equal to maxPrice
                            .filter(item -> {
                                boolean matches = item.getProductPrice() <= maxPrice;
                                System.out.println("Checking maxPrice for item " + item.getProductName() + ": " + item.getProductPrice() + " <= " + maxPrice + " -> " + matches);
                                return matches;
                            })
                            // add filtered items into a new ArrayList
                            .collect(Collectors.toCollection(ArrayList::new));
                    // print number of items and filtered list after maxPrice filter
                    System.out.println("After maxPrice filter (" + maxPrice + "), result size: " + filteredItems.size());
                    System.out.println("Items after maxPrice filter: " + filteredItems);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid maxPrice: " + maxPriceStr);
                }
            }

            // Sort the filtered items using MergeServlet
            if (!filteredItems.isEmpty()) {
                MergeServlet.SortCriterion sortCriterion = MergeServlet.SortCriterion.NAME; // default to name

                // Map sortBy parameter to SortCriterion
                if (sortBy != null) {
                    //default sorting to name
                    switch (sortBy.toLowerCase()) {
                        case "name":
                            sortCriterion = MergeServlet.SortCriterion.NAME;
                            break;
                        case "price":
                            sortCriterion = MergeServlet.SortCriterion.PRICE;
                            break;
                        default:
                            // print if sortBy parameter is invalid and default to name
                            System.out.println("Invalid sortBy parameter: " + sortBy + ", defaulting to NAME");
                    }
                }
                // print the sorting criterion
                System.out.println("Sorting items with sortCriterion=" + sortCriterion);
                // sort the filtered items using MergeServlet
                mergeServlet.sortItems(filteredItems, sortCriterion);
            }

            // determine if we're showing search results or a specific category
            boolean isSearchResult = (name != null && !name.trim().isEmpty()) ||
                    (minPriceStr != null && !minPriceStr.trim().isEmpty()) ||
                    (maxPriceStr != null && !maxPriceStr.trim().isEmpty());
            // set request attributes for the cartIndex.jsp page
            request.setAttribute("items", filteredItems);
            request.setAttribute("cart", cart);
            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("category", category);
            request.setAttribute("isSearchResult", isSearchResult);
            //print forwarding details
            System.out.println("Forwarding to cartIndex.jsp with items size: " + filteredItems.size() + ", isSearchResult: " + isSearchResult);
            // forward the request to the cartIndex.jsp page
            request.getRequestDispatcher("/cartAndOrders/cartIndex.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write("{\"success\": false, \"message\": \"Please log in to access the cart.\"}");
            out.flush();
            return;
        }

        System.out.println("Received POST request to CartServlet with action: " + request.getParameter("action"));
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        ArrayList<GroceryItem> cart;
        ArrayList<GroceryItem> items = FileUtil.readItems(ITEMS_FILE);
        if (items == null) {
            items = new ArrayList<>();
            System.out.println("Initialized empty items list for " + ITEMS_FILE + " (file not found or invalid)");
        }
        System.out.println("Loaded items from " + ITEMS_FILE + ": " + items);

        synchronized (this) {
            cart = FileUtil.readItems(CART_FILE);
            if (cart == null) {
                cart = new ArrayList<>();
                System.out.println("Initialized empty cart for " + CART_FILE);
            }
            System.out.println("Loaded cart before modification: " + cart);

            try {
                if ("add".equals(request.getParameter("action"))) {
                    System.out.println("Processing 'add' action with itemId: " + request.getParameter("itemId"));
                    int itemId = Integer.parseInt(request.getParameter("itemId"));
                    GroceryItem itemToAdd = items.stream().filter(item -> item.getProductID() == itemId).findFirst().orElse(null);
                    if (itemToAdd == null) {
                        System.out.println("Item not found for itemId: " + itemId);
                        out.write("{\"success\": false, \"message\": \"Item not found\"}");
                    } else if (itemToAdd.getQuantity() <= 0) {
                        System.out.println("Item out of stock: " + itemToAdd);
                        out.write("{\"success\": false, \"message\": \"Item is out of stock\"}");
                    } else {
                        GroceryItem existingItem = cart.stream().filter(item -> item.getProductID() == itemId).findFirst().orElse(null);
                        if (existingItem != null) {
                            out.write("{\"success\": false, \"message\": \"Item already in cart, use cart to increase quantity\"}");
                        } else {
                            GroceryItem cartItem = new GroceryItem(itemToAdd.getProductID(), itemToAdd.getProductCategory(),
                                    itemToAdd.getProductName(), itemToAdd.getProductPrice(), itemToAdd.getProductImageLink(),
                                    1, itemToAdd.getDescription());
                            cart.add(cartItem);
                            itemToAdd.setQuantity(itemToAdd.getQuantity() - 1);
                            FileUtil.writeItems(ITEMS_FILE, items);
                            FileUtil.writeItems(CART_FILE, cart);
                            System.out.println("Added new item to cart and updated stock: " + cartItem);
                            double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();
                            sendCartResponse(out, cart, totalPrice, items);
                        }
                    }
                } else if ("update".equals(request.getParameter("action"))) {
                    System.out.println("Processing 'update' action with itemId: " + request.getParameter("itemId"));
                    int itemId = Integer.parseInt(request.getParameter("itemId"));
                    int change = Integer.parseInt(request.getParameter("change"));
                    GroceryItem item = cart.stream().filter(i -> i.getProductID() == itemId).findFirst().orElse(null);
                    if (item == null) {
                        System.out.println("Item not found in cart for itemId: " + itemId);
                        out.write("{\"success\": false, \"message\": \"Item not found in cart\"}");
                    } else {
                        int stockQuantity = items.stream()
                                .filter(i -> i.getProductID() == itemId)
                                .findFirst()
                                .map(GroceryItem::getQuantity)
                                .orElse(0);
                        int currentCartQuantity = item.getQuantity();
                        int totalAvailableStock = stockQuantity + currentCartQuantity;
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
                                System.out.println("Updated stock in " + ITEMS_FILE + ": " + items);
                            }
                            item.setQuantity(newQuantity);
                            if (newQuantity == 0) {
                                cart.remove(item);
                                System.out.println("Removed item from cart: " + item);
                            } else {
                                System.out.println("Updated item quantity in cart: " + item);
                            }
                            FileUtil.writeItems(CART_FILE, cart);
                            System.out.println("Wrote cart to " + CART_FILE + ": " + cart);
                        }
                        double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();
                        sendCartResponse(out, cart, totalPrice, items);
                    }
                } else if ("remove".equals(request.getParameter("action"))) {
                    System.out.println("Processing 'remove' action with itemId: " + request.getParameter("itemId"));
                    int itemId = Integer.parseInt(request.getParameter("itemId"));
                    GroceryItem itemToRemove = cart.stream().filter(item -> item.getProductID() == itemId).findFirst().orElse(null);
                    if (itemToRemove != null) {
                        int quantityRemoved = itemToRemove.getQuantity();
                        cart.remove(itemToRemove);
                        GroceryItem stockItem = items.stream()
                                .filter(item -> item.getProductID() == itemId)
                                .findFirst()
                                .orElse(null);
                        if (stockItem != null) {
                            stockItem.setQuantity(stockItem.getQuantity() + quantityRemoved);
                            FileUtil.writeItems(ITEMS_FILE, items);
                            System.out.println("Updated stock in " + ITEMS_FILE + ": " + items);
                        }
                        FileUtil.writeItems(CART_FILE, cart);
                        System.out.println("Wrote cart to " + CART_FILE + ": " + cart);
                        double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();
                        sendCartResponse(out, cart, totalPrice, items);
                    } else {
                        System.out.println("Item not found in cart for itemId: " + itemId);
                        out.write("{\"success\": false, \"message\": \"Item not found in cart\"}");
                    }
                } else {
                    System.out.println("Invalid action received: " + request.getParameter("action"));
                    out.write("{\"success\": false, \"message\": \"Invalid action\"}");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid item ID or change value: " + e.getMessage());
                out.write("{\"success\": false, \"message\": \"Invalid item ID or change value\"}");
            } catch (IOException e) {
                System.err.println("I/O error during cart operation: " + e.getMessage());
                out.write("{\"success\": false, \"message\": \"Failed to update cart due to I/O error\"}");
            }
        }
        out.flush();
    }

    private void sendCartResponse(PrintWriter out, ArrayList<GroceryItem> cart, double totalPrice, ArrayList<GroceryItem> items) {
        out.write("{\"success\": true, \"message\": \"Cart retrieved successfully\", \"cart\": [");
        for (int i = 0; i < cart.size(); i++) {
            GroceryItem cartItem = cart.get(i);
            int stockQuantity = items.stream()
                    .filter(item -> item.getProductID() == cartItem.getProductID())
                    .findFirst()
                    .map(GroceryItem::getQuantity)
                    .orElse(0);
            int totalAvailableStock = stockQuantity + cartItem.getQuantity();
            out.write(String.format("{\"productID\": %d, \"productName\": \"%s\", \"productPrice\": %.2f, " +
                            "\"productImageLink\": \"%s\", \"quantity\": %d, \"description\": \"%s\", \"stockQuantity\": %d, \"totalAvailableStock\": %d}",
                    cartItem.getProductID(), escapeJson(cartItem.getProductName()), cartItem.getProductPrice(),
                    escapeJson(cartItem.getProductImageLink()), cartItem.getQuantity(),
                    escapeJson(cartItem.getDescription() != null ? cartItem.getDescription() : ""),
                    stockQuantity, totalAvailableStock));
            if (i < cart.size() - 1) out.write(",");
        }
        out.write("], \"totalPrice\": " + String.format("%.2f", totalPrice) + "}");
    }

    private String escapeJson(String str) {
        return str != null ? str.replace("\"", "\\\"").replace("\n", "\\n") : "";
    }
}