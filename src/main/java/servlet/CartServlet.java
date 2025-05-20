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
    private static final String ITEMS_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/items.txt";
    private static final String CART_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/cart.txt";

    @Override
    public void init() throws ServletException {
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

        System.out.println("ITEMS_FILE path: " + ITEMS_FILE);
        System.out.println("CART_FILE path: " + CART_FILE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/userLogin/login.jsp?error=notLoggedIn");
            return;
        }

        ArrayList<GroceryItem> cart;
        synchronized (this) {
            cart = FileUtil.readItems(CART_FILE);
            if (cart == null) {
                cart = new ArrayList<>();
                System.out.println("Initialized empty cart for " + CART_FILE + " (file not found or invalid)");
            } else {
                System.out.println("Loaded cart from " + CART_FILE + ": " + cart);
            }
        }

        ArrayList<GroceryItem> items = FileUtil.readItems(ITEMS_FILE);
        double totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();

        String action = request.getParameter("action");
        if ("getCart".equals(action)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            if (cart.isEmpty()) {
                out.write("{\"success\": true, \"message\": \"Cart is empty\", \"cart\": [], \"totalPrice\": 0.00}");
            } else {
                sendCartResponse(out, cart, totalPrice, items);
            }
            out.flush();
        } else {
            String category = request.getParameter("category");
            if (category != null && !category.isEmpty()) {
                items = items.stream()
                        .filter(item -> item.getProductCategory().equalsIgnoreCase(category))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            request.setAttribute("items", items);
            request.setAttribute("cart", cart);
            request.setAttribute("totalPrice", totalPrice);
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
        return str.replace("\"", "\\\"").replace("\n", "\\n");
    }
}