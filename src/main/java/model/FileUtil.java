package model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileUtil {
    // Read GroceryItem list from a file
    public static ArrayList<GroceryItem> readItems(String filePath) {
        ArrayList<GroceryItem> items = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                if (!file.createNewFile()) {
                    throw new IOException("Failed to create new file: " + filePath);
                }
                System.out.println("Created file: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error creating file " + filePath + ": " + e.getMessage());
                return items;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // Skip empty lines

                String[] parts = line.split(",", 7); // Split into 7 parts max
                if (parts.length != 7) {
                    System.err.println("Invalid line in file " + filePath + ": " + line);
                    continue;
                }
                try {
                    int productID = Integer.parseInt(parts[0].trim());
                    String productCategory = parts[1].trim();
                    String productName = parts[2].trim();
                    double productPrice = Double.parseDouble(parts[3].trim());
                    String productImageLink = parts[4].trim();
                    int quantity = Integer.parseInt(parts[5].trim());
                    String description = parts[6].trim();

                    GroceryItem item = new GroceryItem(productID, productCategory, productName, productPrice, productImageLink, quantity, description);
                    items.add(item);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format in line: " + line + " in file " + filePath + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
        }
        System.out.println("Read " + items.size() + " items from " + filePath);
        return items;
    }

    // Write GroceryItem list to a file
    public static void writeItems(String filePath, ArrayList<GroceryItem> items) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                if (!file.createNewFile()) {
                    throw new IOException("Failed to create new file: " + filePath);
                }
                System.out.println("Created file: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error creating file " + filePath + ": " + e.getMessage());
                throw e;
            }
        }

        // Check write permissions
        if (!file.canWrite()) {
            throw new IOException("No write permission for file: " + filePath);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (GroceryItem item : items) {
                writer.write(String.format("%d,%s,%s,%.2f,%s,%d,%s\n",
                        item.getProductID(),
                        item.getProductCategory(),
                        item.getProductName(),
                        item.getProductPrice(),
                        item.getProductImageLink(),
                        item.getQuantity(),
                        item.getDescription() != null ? item.getDescription() : ""));
            }
            writer.flush(); // Ensure data is written to the file
            System.out.println("Successfully wrote " + items.size() + " items to " + filePath + " with content: " + items);
        } catch (IOException e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
            throw e; // Propagate the exception to halt execution
        }
    }

    // Check if an order number is unique in the orders file
    public static boolean isOrderNumberUnique(String orderNumber, String filePath) {
        Set<String> orderNumbers = new HashSet<>();
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("No orders file found at " + filePath + "; order number " + orderNumber + " is unique.");
            return true;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("orderNumber=")) {
                    orderNumbers.add(line.split("=", 2)[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading order numbers from " + filePath + ": " + e.getMessage());
            return false;
        }
        boolean isUnique = !orderNumbers.contains(orderNumber);
        System.out.println("Order number " + orderNumber + " is " + (isUnique ? "unique" : "not unique") + " in " + filePath);
        return isUnique;
    }

    // Write an order to a file
    public static void writeOrder(String filePath, String orderNumber, String userNumber, String fullName,
                                  String phoneNumber, String address, String deliveryMethod,
                                  String paymentMethod, String deliveryDate, ArrayList<GroceryItem> cartItems,
                                  double totalPrice) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            System.out.println("Created new order file at: " + file.getAbsolutePath());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write("orderNumber=" + orderNumber + "\n");
            writer.write("userNumber=" + userNumber + "\n");
            writer.write("name=" + (fullName != null ? fullName : "") + "\n");
            writer.write("phoneNum=" + (phoneNumber != null ? phoneNumber : "") + "\n");
            writer.write("address=" + (address != null ? address : "") + "\n");
            writer.write("deliveryMethod=" + (deliveryMethod != null ? deliveryMethod : "") + "\n");
            writer.write("paymentMethod=" + (paymentMethod != null ? paymentMethod : "") + "\n");
            writer.write("deliveryDate=" + (deliveryDate != null ? deliveryDate : "") + "\n");
            writer.write("[products]\n");
            for (GroceryItem item : cartItems) {
                writer.write("productID=" + item.getProductID() + ", quantity=" + item.getQuantity() + "\n");
            }
            writer.write("[total]\n");
            writer.write("totalPrice=" + String.format("%.2f", totalPrice) + "\n");
            System.out.println("Wrote order " + orderNumber + " to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing order to file " + filePath + ": " + e.getMessage());
            throw e;
        }
    }

    // Read all orders from a directory of order files
    public static List<Order> readAllOrders(String ordersDir) {
        List<Order> orders = new ArrayList<>();
        File dir = new File(ordersDir);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Orders directory not found: " + ordersDir);
            return orders;
        }

        File[] orderFiles = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (orderFiles == null) {
            System.out.println("No order files found in: " + ordersDir);
            return orders;
        }

        for (File file : orderFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Order order = new Order();
                List<String[]> products = new ArrayList<>();
                String line;
                String currentSection = null;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    if (line.startsWith("[") && line.endsWith("]")) {
                        currentSection = line.substring(1, line.length() - 1);
                        continue;
                    }

                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();

                        switch (key) {
                            case "orderNumber":
                                order.setOrderNumber(value);
                                break;
                            case "userNumber":
                                order.setUserNumber(value);
                                break;
                            case "name":
                                order.setFullName(value);
                                break;
                            case "phoneNum":
                                order.setPhoneNum(value);
                                break;
                            case "address":
                                order.setAddress(value);
                                break;
                            case "deliveryMethod":
                                order.setDeliveryMethod(value);
                                break;
                            case "paymentMethod":
                                order.setPaymentMethod(value);
                                break;
                            case "deliveryDate":
                                order.setDeliveryDate(value.isEmpty() ? null : value);
                                break;
                            case "totalPrice":
                                if ("total".equals(currentSection)) {
                                    order.setTotalPrice(Double.parseDouble(value));
                                }
                                break;
                            default:
                                if ("products".equals(currentSection) && key.startsWith("productID")) {
                                    String[] productParts = line.split(", quantity=");
                                    if (productParts.length == 2) {
                                        products.add(new String[]{productParts[0].split("=")[1].trim(), productParts[1].trim()});
                                    }
                                }
                                break;
                        }
                    }
                }
                order.setProducts(products);
                orders.add(order);
            } catch (IOException e) {
                System.err.println("Error reading order file " + file.getName() + ": " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format in file " + file.getName() + ": " + e.getMessage());
            }
        }
        System.out.println("Read " + orders.size() + " orders from " + ordersDir);
        return orders;
    }

    // Read all users from users.txt
    public static List<User> readUsers(String filePath) {
        List<User> users = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                System.out.println("Created users file: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error creating users file " + filePath + ": " + e.getMessage());
                return users;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String userNumber = null, fullName = null, email = null, phoneNumber = null, address = null, password = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("--- User Start:")) {
                    try {
                        userNumber = line.split(":")[1].trim().split(" ---")[0];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.err.println("Invalid User Start line in " + filePath + ": " + line);
                        continue;
                    }
                } else if (line.startsWith("userNumber=")) {
                    userNumber = line.split("=", 2)[1].trim();
                } else if (line.startsWith("fullName=")) {
                    fullName = line.split("=", 2)[1].trim();
                } else if (line.startsWith("email=")) {
                    email = line.split("=", 2)[1].trim();
                } else if (line.startsWith("phoneNumber=")) {
                    phoneNumber = line.split("=", 2)[1].trim();
                } else if (line.startsWith("address=")) {
                    address = line.split("=", 2)[1].trim();
                } else if (line.startsWith("password=")) {
                    password = line.split("=", 2)[1].trim();
                } else if (line.startsWith("--- User End ---")) {
                    if (userNumber != null && fullName != null && email != null && phoneNumber != null && address != null && password != null) {
                        users.add(new User(userNumber, fullName, email, phoneNumber, address, password));
                    } else {
                        System.err.println("Incomplete user data in " + filePath + ": userNumber=" + userNumber + ", email=" + email);
                    }
                    userNumber = fullName = email = phoneNumber = address = password = null; // Reset for next user
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users file " + filePath + ": " + e.getMessage());
        }
        System.out.println("Read " + users.size() + " users from " + filePath);
        return users;
    }

    // Check if a user number is unique in users.txt
    public static boolean isUserNumberUnique(String userNumber, String filePath) {
        List<User> users = readUsers(filePath);
        boolean isUnique = users.stream().noneMatch(user -> user.getUserNumber().equals(userNumber));
        System.out.println("User number " + userNumber + " is " + (isUnique ? "unique" : "not unique") + " in " + filePath);
        return isUnique;
    }

    // Write logged-in user to loggedInUser.txt
    public static void writeLoggedInUser(String filePath, User user) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                System.out.println("Created loggedInUser file: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error creating loggedInUser file " + filePath + ": " + e.getMessage());
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            if (user != null) {
                writer.write("userNumber=" + (user.getUserNumber() != null ? user.getUserNumber() : "") + "\n");
                writer.write("fullName=" + (user.getFullName() != null ? user.getFullName() : "") + "\n");
                writer.write("email=" + (user.getEmail() != null ? user.getEmail() : "") + "\n");
                writer.write("phoneNumber=" + (user.getPhoneNumber() != null ? user.getPhoneNumber() : "") + "\n");
                writer.write("address=" + (user.getAddress() != null ? user.getAddress() : "") + "\n");
                System.out.println("Wrote logged-in user to " + filePath + ": " + user.getEmail());
            } else {
                System.err.println("Cannot write null user to " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error writing to loggedInUser file " + filePath + ": " + e.getMessage());
        }
    }

    // Read logged-in user from loggedInUser.txt
    public static User readLoggedInUser(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("No loggedInUser file found at " + filePath);
            return null;
        }

        String userNumber = null, fullName = null, email = null, phoneNumber = null, address = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "userNumber":
                            userNumber = value.isEmpty() ? null : value;
                            break;
                        case "fullName":
                            fullName = value.isEmpty() ? null : value;
                            break;
                        case "email":
                            email = value.isEmpty() ? null : value;
                            break;
                        case "phoneNumber":
                            phoneNumber = value.isEmpty() ? null : value;
                            break;
                        case "address":
                            address = value.isEmpty() ? null : value;
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading loggedInUser file " + filePath + ": " + e.getMessage());
            return null;
        }

        if (userNumber != null && fullName != null && email != null && phoneNumber != null && address != null) {
            User user = new User(userNumber, fullName, email, phoneNumber, address, "");
            System.out.println("Read logged-in user from " + filePath + ": " + user.getEmail());
            return user;
        }
        System.err.println("Incomplete logged-in user data in " + filePath);
        return null;
    }

    // Clear loggedInUser.txt on logout
    public static void clearLoggedInUser(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write("");
                System.out.println("Cleared loggedInUser file: " + filePath);
            } catch (IOException e) {
                System.err.println("Error clearing loggedInUser file " + filePath + ": " + e.getMessage());
            }
        } else {
            System.out.println("loggedInUser file does not exist at " + filePath + "; nothing to clear.");
        }
    }

    // Order class for reading orders
    public static class Order {
        private String orderNumber;
        private String userNumber;
        private String fullName;
        private String phoneNum;
        private String address;
        private String deliveryMethod;
        private String paymentMethod;
        private String deliveryDate;
        private List<String[]> products; // [productID, quantity]
        private double totalPrice;

        public String getOrderNumber() { return orderNumber; }
        public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
        public String getUserNumber() { return userNumber; }
        public void setUserNumber(String userNumber) { this.userNumber = userNumber; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getPhoneNum() { return phoneNum; }
        public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getDeliveryMethod() { return deliveryMethod; }
        public void setDeliveryMethod(String deliveryMethod) { this.deliveryMethod = deliveryMethod; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public String getDeliveryDate() { return deliveryDate; }
        public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }
        public List<String[]> getProducts() { return products; }
        public void setProducts(List<String[]> products) { this.products = products; }
        public double getTotalPrice() { return totalPrice; }
        public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    }
}