package model;

import java.io.*;
import java.util.*;

public class FileUtil {

    // Reads grocery items from items.txt or cart.txt
    public static ArrayList<GroceryItem> readItems(String filePath) {
        // Initialize empty list for grocery items
        ArrayList<GroceryItem> items = new ArrayList<>();
        // Create File object for the specified path
        File file = new File(filePath);
        // Create file and directories if they don't exist
        if (!file.exists()) {
            try {
                // Ensure parent directories exist
                file.getParentFile().mkdirs();
                // Create the file
                if (!file.createNewFile()) {
                    throw new IOException("Failed to create file: " + filePath);
                }
                System.out.println("Created file: " + file.getAbsolutePath());
            } catch (IOException e) {
                // Log error and return empty list if creation fails
                System.out.println("Error creating file: " + e.getMessage());
                return items;
            }
        }

        // Read file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Process each line until end of file
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                line = line.trim();
                if (line.isEmpty()) continue;
                // Split line into 7 fields (ID, category, name, price, image, quantity, description)
                String[] parts = line.split(",", 7);
                // Validate line has exactly 7 fields
                if (parts.length != 7) {
                    System.out.println("Invalid line: " + line);
                    continue;
                }
                try {
                    // Parse fields into appropriate types
                    int productID = Integer.parseInt(parts[0].trim());
                    String productCategory = parts[1].trim();
                    String productName = parts[2].trim();
                    double productPrice = Double.parseDouble(parts[3].trim());
                    String productImageLink = parts[4].trim();
                    int quantity = Integer.parseInt(parts[5].trim());
                    String description = parts[6].trim();
                    // Create and add GroceryItem to list
                    items.add(new GroceryItem(productID, productCategory, productName, productPrice, productImageLink, quantity, description));
                } catch (NumberFormatException e) {
                    // Log error for invalid numbers and skip line
                    System.out.println("Invalid number in line: " + line);
                }
            }
        } catch (IOException e) {
            // Log error if file reading fails
            System.out.println("Error reading file: " + e.getMessage());
        }
        // Log number of items read
        System.out.println("Read " + items.size() + " items from " + filePath);
        return items;
    }

    // Write grocery items from items.txt or cart.txt
    public static void writeItems(String filePath, ArrayList<GroceryItem> items) throws IOException {
        // Create File object for the specified path
        File file = new File(filePath);
        // Create file and directories if they don't exist
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            if (!file.createNewFile()) {
                throw new IOException("Failed to create file: " + filePath);
            }
            System.out.println("Created file: " + file.getAbsolutePath());
        }
        // Check write permissions
        if (!file.canWrite()) {
            throw new IOException("No write permission: " + filePath);
        }

        // Write items to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) { // Rewrite not append
            // Iterate through each item
            for (GroceryItem item : items) {
                // Write item as comma-separated line with 7 fields
                writer.write(String.format("%d,%s,%s,%.2f,%s,%d,%s\n",
                        item.getProductID(), item.getProductCategory(), item.getProductName(),
                        item.getProductPrice(), item.getProductImageLink(), item.getQuantity(),
                        item.getDescription() != null ? item.getDescription() : ""));
            }
            // Ensure data is saved immediately
            writer.flush();
            System.out.println("Wrote " + items.size() + " items to " + filePath);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
            throw e;
        }
    }

    // Logic section
    public static List<User> readUsers(String filePath) {
        List<User> users = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                System.out.println("Created users file: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error creating users file: " + e.getMessage());
                return users;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            User user = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("--- User Start:")) {
                    user = new User(null, null, null, null, null, null, null);
                } else if (line.startsWith("--- User End ---")) {
                    if (user != null && user.getUsername() != null && user.getPassword() != null) {
                        users.add(user);
                    } else {
                        System.err.println("Incomplete user data: " + user);
                    }
                    user = null;
                } else if (user != null) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        switch (key) {
                            case "username": user.setUsername(value); break;
                            case "password": user.setPassword(value); break;
                            case "userNumber": user.setUserNumber(value); break;
                            case "fullName": user.setFullName(value); break;
                            case "email": user.setEmail(value); break;
                            case "phoneNumber": user.setPhoneNumber(value); break;
                            case "address": user.setAddress(value); break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
            return null;
        }
        System.out.println("Read " + users.size() + " users from " + filePath);
        return users;
    }

    public static void writeUsers(String filePath, List<User> users) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            System.out.println("Created users file: " + file.getAbsolutePath());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) { // Rewrite not append
            for (User user : users) {
                writer.write("--- User Start: " + user.getUserNumber() + " ---\n");
                writer.write("username=" + (user.getUsername() != null ? user.getUsername() : "") + "\n");
                writer.write("password=" + (user.getPassword() != null ? user.getPassword() : "") + "\n");
                writer.write("userNumber=" + (user.getUserNumber() != null ? user.getUserNumber() : "") + "\n");
                writer.write("fullName=" + (user.getFullName() != null ? user.getFullName() : "") + "\n");
                writer.write("email=" + (user.getEmail() != null ? user.getEmail() : "") + "\n");
                writer.write("phoneNumber=" + (user.getPhoneNumber() != null ? user.getPhoneNumber() : "") + "\n");
                writer.write("address=" + (user.getAddress() != null ? user.getAddress() : "") + "\n");
                writer.write("--- User End ---\n\n");
            }
            writer.flush();
            System.out.println("Wrote " + users.size() + " users to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing users file: " + e.getMessage());
            throw e;
        }
    }

    public static boolean isUserNumberUnique(String userNumber, String filePath) {
        List<User> users = readUsers(filePath);
        if (users == null) {
            return true;
        }
        boolean isUnique = users.stream().noneMatch(user -> user.getUserNumber() != null && user.getUserNumber().equals(userNumber));
        System.out.println("User number " + userNumber + " is " + (isUnique ? "unique" : "not unique"));
        return isUnique;
    }

    public static synchronized void writeLoggedInUser(String filePath, User user) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                System.out.println("Created loggedInUser file: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error creating loggedInUser file: " + e.getMessage());
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            if (user != null) {
                writer.write("--- User Start: " + user.getUserNumber() + " ---\n");
                writer.write("username=" + (user.getUsername() != null ? user.getUsername() : "") + "\n");
                writer.write("password=" + (user.getPassword() != null ? user.getPassword() : "") + "\n");
                writer.write("userNumber=" + (user.getUserNumber() != null ? user.getUserNumber() : "") + "\n");
                writer.write("fullName=" + (user.getFullName() != null ? user.getFullName() : "") + "\n");
                writer.write("email=" + (user.getEmail() != null ? user.getEmail() : "") + "\n");
                writer.write("phoneNumber=" + (user.getPhoneNumber() != null ? user.getPhoneNumber() : "") + "\n");
                writer.write("address=" + (user.getAddress() != null ? user.getAddress() : "") + "\n");
                writer.write("--- User End ---\n");
                System.out.println("Wrote logged-in user: " + user.getEmail());
            } else {
                System.err.println("Cannot write null user");
            }
        } catch (IOException e) {
            System.err.println("Error writing loggedInUser file: " + e.getMessage());
        }
    }

    public static synchronized void clearLoggedInUser(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write("");
                System.out.println("Cleared loggedInUser file");
            } catch (IOException e) {
                System.err.println("Error clearing loggedInUser file: " + e.getMessage());
            }
        } else {
            System.out.println("loggedInUser file does not exist; nothing to clear");
        }
    }
}