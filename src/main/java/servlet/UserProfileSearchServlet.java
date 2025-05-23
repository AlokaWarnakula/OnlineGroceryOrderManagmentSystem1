package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.FileUtil;
import model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class OrderQueue {
    private ArrayList<FileUtil.Order> orders;
    private boolean isDeliveredQueue; // True for deliveredDate, false for confirmationDate
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public OrderQueue(int initialCapacity, boolean isDeliveredQueue) {
        this.orders = new ArrayList<>();
        this.isDeliveredQueue = isDeliveredQueue;
    }

    // Enqueue
    public void enqueue(FileUtil.Order order) {
        // Get date to compare
        String dateStr = isDeliveredQueue ? order.getDeliveredDate() : order.getConfirmationDate();
        // Skip invalid dates (added at end)
        if (dateStr == null || dateStr.isEmpty()) {
            orders.add(order);
            return;
        }

        // Find insertion position
        int insertPos = orders.size();
        try {
            LocalDateTime orderDate = LocalDateTime.parse(dateStr, DATE_TIME_FORMATTER);
            for (int i = 0; i < orders.size(); i++) {
                String existingDateStr = isDeliveredQueue ? orders.get(i).getDeliveredDate() : orders.get(i).getConfirmationDate();
                if (existingDateStr == null || existingDateStr.isEmpty()) {
                    continue;
                }
                LocalDateTime existingDate = LocalDateTime.parse(existingDateStr, DATE_TIME_FORMATTER);
                if (orderDate.compareTo(existingDate) < 0) {
                    insertPos = i;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing date for order " + order.getOrderNumber() + ": " + e.getMessage());
            insertPos = orders.size(); // Add at end if parsing fails
        }

        // Insert order
        orders.add(insertPos, order);
    }

    // Dequeue
    public FileUtil.Order dequeue() {
        if (orders.size() == 0) {
            return null;
        }
        return orders.remove(0);
    }

    // Convert to ArrayList (FIFO order for Active)
    public ArrayList<FileUtil.Order> toArrayList() {
        return new ArrayList<>(orders);
    }

    // Convert to reverse ArrayList (LIFO order for Delivered)
    public ArrayList<FileUtil.Order> toArrayListReverse() {
        ArrayList<FileUtil.Order> result = new ArrayList<>();
        for (int i = orders.size() - 1; i >= 0; i--) {
            result.add(orders.get(i));
        }
        return result;
    }

    public int size() {
        return orders.size();
    }
}

public class UserProfileSearchServlet extends HttpServlet {
    private static final String ORDERS_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/orders.txt";
    private static final String DELIVERED_ORDERS_FILE = "/Users/alokawarnakula/TestOOPProjectFolder/OnlineGroceryOrderSystem/src/main/webapp/data/deliveredOrders.txt";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/userLogin/login.jsp");
            return;
        }

        String userNumber = loggedInUser.getUserNumber();
        String tab = request.getParameter("tab") != null ? request.getParameter("tab") : "Active";
        String searchQuery = request.getParameter("searchQuery");

        // Read orders into ArrayList
        ArrayList<FileUtil.Order> orders;
        if ("Delivered".equalsIgnoreCase(tab)) {
            orders = new ArrayList<>(FileUtil.readAllDeliveredOrders(DELIVERED_ORDERS_FILE));
        } else {
            orders = new ArrayList<>(FileUtil.readAllOrders(ORDERS_FILE));
        }

        // Filter by userNumber
        ArrayList<FileUtil.Order> userOrders = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            FileUtil.Order order = orders.get(i);
            if (order != null && userNumber.equals(order.getUserNumber())) {
                userOrders.add(order);
            }
        }

        // Filter by searchQuery
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            String query = searchQuery.trim().toLowerCase();
            ArrayList<FileUtil.Order> tempOrders = new ArrayList<>();
            for (int i = 0; i < userOrders.size(); i++) {
                FileUtil.Order order = userOrders.get(i);
                if (order != null && order.getOrderNumber().toLowerCase().contains(query)) {
                    tempOrders.add(order);
                }
            }
            userOrders = tempOrders;
        }

        // Process orders based on tab
        ArrayList<FileUtil.Order> finalOrders;
        if ("Delivered".equalsIgnoreCase(tab)) {
            // Delivered tab: LIFO
            OrderQueue queue = new OrderQueue(userOrders.size(), true); // true for deliveredDate
            for (int i = 0; i < userOrders.size(); i++) {
                if (userOrders.get(i) != null) {
                    queue.enqueue(userOrders.get(i));
                }
            }
            finalOrders = queue.toArrayListReverse(); // Reverse for LIFO
        } else {
            // Active tab: FIFO
            OrderQueue queue = new OrderQueue(userOrders.size(), false); // false for confirmationDate
            for (int i = 0; i < userOrders.size(); i++) {
                if (userOrders.get(i) != null) {
                    queue.enqueue(userOrders.get(i));
                }
            }
            finalOrders = queue.toArrayList(); // FIFO order
        }

        // Set attributes and forward to JSP
        request.setAttribute("orders", finalOrders);
        request.getRequestDispatcher("/userLogin/userProfile.jsp").forward(request, response);
    }
}