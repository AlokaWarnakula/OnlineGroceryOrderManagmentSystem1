package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.FileUtil;
import model.GroceryItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class OrderServlet extends HttpServlet {
    private static final String ITEMS_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/items.txt";
    private static final String CART_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/cart.txt";
    private static final String ORDERS_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/orders.txt";

    @Override
    public void init() throws ServletException {
        File ordersDir = new File("/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data");
        if (!ordersDir.exists()) {
            ordersDir.mkdirs();
            System.out.println("Created directory: " + ordersDir.getAbsolutePath());
        }
        System.out.println("ITEMS_FILE path: " + ITEMS_FILE);
        System.out.println("CART_FILE path: " + CART_FILE);
        System.out.println("ORDERS_FILE path: " + ORDERS_FILE);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<GroceryItem> cart;
        synchronized (this) {
            cart = FileUtil.readItems(CART_FILE);
            if (cart == null || cart.isEmpty()) {
                System.out.println("Cart is empty or null in " + CART_FILE);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cart is empty");
                return;
            }
            System.out.println("Loaded cart from " + CART_FILE + ": " + cart);
        }

        // Declare variables at method scope
        double totalPrice;
        String orderNumber;
        String deliveryDate;

        ArrayList<GroceryItem> items = FileUtil.readItems(ITEMS_FILE);

        synchronized (this) {
            // Only check if items exist, no stock validation since CartServlet reserved them
            for (GroceryItem cartItem : cart) {
                GroceryItem stockItem = items.stream()
                        .filter(item -> item.getProductID() == cartItem.getProductID())
                        .findFirst()
                        .orElse(null);
                if (stockItem == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item ID " + cartItem.getProductID() + " not found in inventory");
                    return;
                }
                // No stock check here; stock was already deducted by CartServlet
            }

            totalPrice = cart.stream().mapToDouble(GroceryItem::getTotalPrice).sum();

            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            String deliveryMethod = request.getParameter("deliveryMethod");
            String paymentMethod = request.getParameter("paymentMethod");

            // Set deliveryDate based on deliveryMethod
            if ("same-day".equals(deliveryMethod)) {
                deliveryDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE); // e.g., "2025-03-15"
            } else {
                deliveryDate = request.getParameter("deliveryDate"); // For scheduled or store pickup
            }

            if (fullName == null || phoneNumber == null || address == null ||
                    deliveryMethod == null || paymentMethod == null ||
                    (("scheduled".equals(deliveryMethod) || "store pickup".equals(deliveryMethod)) && (deliveryDate == null || deliveryDate.isEmpty()))) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields");
                return;
            }

            do {
                orderNumber = generateOrderNumber();
            } while (!FileUtil.isOrderNumberUnique(orderNumber, ORDERS_FILE));

            String userNumber = "US111111111111";
            String confirmationDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String paymentStatus = "online card".equals(paymentMethod) ? "Paid" : "Pending";
            String deliveryStatus = "Pending";
            String orderStatus = "active"; // New field for order status

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE, true))) {
                writer.write("--- Order Start: " + orderNumber + " ---\n");
                writer.write("orderNumber=" + orderNumber + "\n");
                writer.write("userNumber=" + userNumber + "\n");
                writer.write("name=" + (fullName != null ? fullName : "") + "\n");
                writer.write("phoneNum=" + (phoneNumber != null ? phoneNumber : "") + "\n");
                writer.write("address=" + (address != null ? address : "") + "\n");
                writer.write("deliveryMethod=" + (deliveryMethod != null ? deliveryMethod : "") + "\n");
                writer.write("paymentMethod=" + (paymentMethod != null ? paymentMethod : "") + "\n");
                writer.write("deliveryDate=" + (deliveryDate != null ? deliveryDate : "") + "\n");
                writer.write("confirmationDate=" + confirmationDate + "\n");
                writer.write("paymentStatus=" + paymentStatus + "\n");
                writer.write("deliveryStatus=" + deliveryStatus + "\n");
                writer.write("orderStatus=" + orderStatus + "\n"); // Add orderStatus
                writer.write("[products]\n");
                for (GroceryItem item : cart) {
                    writer.write("productID=" + item.getProductID() + ", quantity=" + item.getQuantity() + "\n");
                }
                writer.write("[total]\n");
                writer.write("totalPrice=" + String.format("%.2f", totalPrice) + "\n");
                writer.write("--- Order End ---\n");
                writer.write("\n");
                System.out.println("Appended order to " + ORDERS_FILE);
            } catch (IOException e) {
                System.err.println("Failed to append order to " + ORDERS_FILE + ": " + e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save order: " + e.getMessage());
                return;
            }

            // No stock deduction here; CartServlet already handled it
            cart.clear();
            FileUtil.writeItems(CART_FILE, cart);
            System.out.println("Cleared cart in " + CART_FILE);
        }

        request.setAttribute("orderNumber", orderNumber);
        request.setAttribute("fullName", request.getParameter("fullName"));
        request.setAttribute("phoneNumber", request.getParameter("phoneNumber"));
        request.setAttribute("address", request.getParameter("address"));
        request.setAttribute("deliveryMethod", request.getParameter("deliveryMethod"));
        request.setAttribute("deliveryDate", deliveryDate);
        request.setAttribute("paymentMethod", request.getParameter("paymentMethod"));
        request.setAttribute("totalPrice", String.format("%.2f", totalPrice));
        request.setAttribute("confirmationDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        request.setAttribute("paymentStatus", "online card".equals(request.getParameter("paymentMethod")) ? "Paid" : "Pending");
        request.setAttribute("deliveryStatus", "Pending");
        request.setAttribute("orderStatus", "active"); // Pass to JSP

        request.getRequestDispatcher("/cartAndOrders/processCheckOut.jsp").forward(request, response);
    }

    private String generateOrderNumber() {
        Random random = new Random();
        long number = Math.abs(random.nextLong() % 1000000000000L);
        return "OD" + String.format("%012d", number);
    }
}