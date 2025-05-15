package model;

import java.io.*;
import java.util.*;

// FileUtil class handles file operations for login (users, admins, logged-in user) and cart index (grocery items) in the online grocery system
public class FileUtil {

    // Reads grocery items from a file (items.txt for products or cart.txt for cart), used by CartServlet to load available items or cart contents
    public static ArrayList<GroceryItem> readItems(String filePath) {
        // Create an empty list to store grocery items (like apples, milk, etc.)
        ArrayList<GroceryItem> items = new ArrayList<>();
        // Create a File object to point to the file path (e.g., items.txt or cart.txt)
        File file = new File(filePath);
        // If the file doesn't exist, create it to avoid errors
        if (!file.exists()) {
            try {
                // Make sure the folder exists (e.g., data folder)
                file.getParentFile().mkdirs();
                // Create the file if it’s not there
                if (!file.createNewFile()) {
                    throw new IOException("Failed to create new file: " + filePath);
                }
                // Print a message to confirm the file was created
                System.out.println("Created file: " + file.getAbsolutePath());
            } catch (IOException e) {
                // If something goes wrong (e.g., no permission), print error and return empty list
                System.err.println("Error creating file " + filePath + ": " + e.getMessage());
                return items;
            }
        }

        // Read the file line by line using a BufferedReader (efficient for text files)
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Keep reading until there are no more lines
            while ((line = reader.readLine()) != null) {
                // Remove extra spaces and skip empty lines
                line = line.trim();
                if (line.isEmpty()) continue;

                // Each line has 7 parts (ID, category, name, price, image, quantity, description) separated by commas
                String[] parts = line.split(",", 7);
                // Check if the line has exactly 7 parts; if not, it’s invalid
                if (parts.length != 7) {
                    System.err.println("Invalid line in file " + filePath + ": " + line);
                    continue;
                }
                try {
                    // Convert text parts to the right data types (e.g., ID to int, price to double)
                    int productID = Integer.parseInt(parts[0].trim());
                    String productCategory = parts[1].trim();
                    String productName = parts[2].trim();
                    double productPrice = Double.parseDouble(parts[3].trim());
                    String productImageLink = parts[4].trim();
                    int quantity = Integer.parseInt(parts[5].trim());
                    String description = parts[6].trim();

                    // Create a new GroceryItem object with the parsed data
                    GroceryItem item = new GroceryItem(productID, productCategory, productName, productPrice, productImageLink, quantity, description);
                    // Add the item to the list
                    items.add(item);
                } catch (NumberFormatException e) {
                    // If numbers (like ID or price) can’t be parsed, print error and skip the line
                    System.err.println("Invalid number format in line: " + line + " in file " + filePath + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            // If reading the file fails (e.g., file is locked), print error
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
        }
        // Print how many items were read (e.g., "Read 5 items from cart.txt")
        System.out.println("Read " + items.size() + " items from " + filePath);
        return items;
    }

    // Writes grocery items to a file (items.txt for stock or cart.txt for cart), used by CartServlet to save cart or update stock
    public static void writeItems(String filePath, ArrayList<GroceryItem> items) throws IOException {
        // Create a File object for the file path
        File file = new File(filePath);
        // If the file doesn’t exist, create it
        if (!file.exists()) {
            try {
                // Create the folder if it’s not there
                file.getParentFile().mkdirs();
                // Create the file
                if (!file.createNewFile()) {
                    throw new IOException("Failed to create new file: " + filePath);
                }
                // Print a message to confirm file creation
                System.out.println("Created file: " + file.getAbsolutePath());
            } catch (IOException e) {
                // If creation fails, print error and stop the operation
                System.err.println("Error creating file " + filePath + ": " + e.getMessage());
                throw e;
            }
        }

        // Check if we have permission to write to the file
        if (!file.canWrite()) {
            throw new IOException("No write permission for file: " + filePath);
        }

        // Write items to the file using a BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            // Loop through each item in the list
            for (GroceryItem item : items) {
                // Write item data as a comma-separated line (e.g., "1,Fruit,Apple,1.50,apple.jpg,10,Fresh")
                writer.write(String.format("%d,%s,%s,%.2f,%s,%d,%s\n",
                        item.getProductID(),
                        item.getProductCategory(),
                        item.getProductName(),
                        item.getProductPrice(),
                        item.getProductImageLink(),
                        item.getQuantity(),
                        item.getDescription() != null ? item.getDescription() : ""));
            }
            // Make sure all data is saved to the file
            writer.flush();
            // Print a message to confirm the write (shows how many items were saved)
            System.out.println("Successfully wrote " + items.size() + " items to " + filePath + " with content: " + items);
        } catch (IOException e) {
            // If writing fails, print error and stop the operation
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
            throw e;
        }
    }

    // Reads all users from users.txt, used by LoginServlet to check login credentials and RegisterServlet to check for existing emails
    public static List<User> readUsers(String filePath) {
        // Create an empty list to store user data (like email, password, etc.)
        List<User> users = new ArrayList<>();
        // Create a File object for users.txt
        File file = new File(filePath);
        // If the file doesn’t exist, create it
        if (!file.exists()) {
            try {
                // Create the folder if needed
                file.getParentFile().mkdirs();
                // Create the file
                file.createNewFile();
                // Print a message to confirm
                System.out.println("Created users file: " + file.getAbsolutePath());
            } catch (IOException e) {
                // If creation fails, print error and return empty list
                System.err.println("Error creating users file " + filePath + ": " + e.getMessage());
                return users;
            }
        }

        // Read the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            User user = null;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                line = line.trim();
                if (line.isEmpty()) continue;

                // When a new user record starts (marked by "--- User Start:")
                if (line.startsWith("--- User Start:")) {
                    // Create a new User object with empty fields
                    user = new User(null, null, null, null, null, null, null);
                } else if (line.startsWith("--- User End ---")) {
                    // When the user record ends, check if it’s valid
                    if (user != null && user.getUsername() != null && user.getPassword() != null) {
                        // Add valid user to the list
                        users.add(user);
                    } else {
                        // Print error if user data is incomplete
                        System.err.println("Incomplete user data in " + filePath + ": " + user);
                    }
                    user = null;
                } else if (user != null) {
                    // Split the line into key and value (e.g., "email=john@example.com")
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        // Set the right field based on the key
                        switch (key) {
                            case "username":
                                user.setUsername(value);
                                break;
                            case "password":
                                user.setPassword(value);
                                break;
                            case "userNumber":
                                user.setUserNumber(value);
                                break;
                            case "fullName":
                                user.setFullName(value);
                                break;
                            case "email":
                                user.setEmail(value);
                                break;
                            case "phoneNumber":
                                user.setPhoneNumber(value);
                                break;
                            case "address":
                                user.setAddress(value);
                                break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            // If reading fails, print error and return null
            System.err.println("Error reading users file " + filePath + ": " + e.getMessage());
            return null;
        }
        // Print how many users were read
        System.out.println("Read " + users.size() + " users from " + filePath);
        return users;
    }

    // Writes all users to users.txt, used by LoginServlet to update passwords and RegisterServlet to save new users
    public static void writeUsers(String filePath, List<User> users) throws IOException {
        // Create a File object for users.txt
        File file = new File(filePath);
        // If the file doesn’t exist, create it
        if (!file.exists()) {
            // Create the folder if needed
            file.getParentFile().mkdirs();
            // Create the file
            file.createNewFile();
            // Print a message to confirm
            System.out.println("Created users file: " + file.getAbsolutePath());
        }

        // Write user data to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            // Loop through each user
            for (User user : users) {
                // Write the start of a user record
                writer.write("--- User Start: " + user.getUserNumber() + " ---\n");
                // Write all user details (handle null values)
                writer.write("username=" + (user.getUsername() != null ? user.getUsername() : "") + "\n");
                writer.write("password=" + (user.getPassword() != null ? user.getPassword() : "") + "\n");
                writer.write("userNumber=" + (user.getUserNumber() != null ? user.getUserNumber() : "") + "\n");
                writer.write("fullName=" + (user.getFullName() != null ? user.getFullName() : "") + "\n");
                writer.write("email=" + (user.getEmail() != null ? user.getEmail() : "") + "\n");
                writer.write("phoneNumber=" + (user.getPhoneNumber() != null ? user.getPhoneNumber() : "") + "\n");
                writer.write("address=" + (user.getAddress() != null ? user.getAddress() : "") + "\n");
                // Write the end of the user record
                writer.write("--- User End ---\n");
                writer.write("\n");
            }
            // Save all data to the file
            writer.flush();
            // Print a message to confirm
            System.out.println("Successfully wrote " + users.size() + " users to " + filePath);
        } catch (IOException e) {
            // If writing fails, print error and stop
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
            throw e;
        }
    }

    // Checks if a user number is unique in users.txt, used by RegisterServlet to assign new user IDs
    public static boolean isUserNumberUnique(String userNumber, String filePath) {
        // Read all users from users.txt
        List<User> users = readUsers(filePath);
        // If reading fails, assume the number is unique (safe default)
        if (users == null) {
            return true;
        }
        // Check if any user has the same user number
        boolean isUnique = users.stream().noneMatch(user -> user.getUserNumber() != null && user.getUserNumber().equals(userNumber));
        // Print whether the number is unique
        System.out.println("User number " + userNumber + " is " + (isUnique ? "unique" : "not unique") + " in " + filePath);
        return isUnique;
    }

    // Reads all admins from admins.txt, used by LoginServlet to check admin login credentials
    public static List<Admin> readAdmins(String filePath) {
        // Create an empty list to store admin data
        List<Admin> admins = new ArrayList<>();
        // Create a File object for admins.txt
        File file = new File(filePath);
        // If the file doesn’t exist, create it
        if (!file.exists()) {
            try {
                // Create the folder if needed
                file.getParentFile().mkdirs();
                // Create the file
                file.createNewFile();
                // Print a message to confirm
                System.out.println("Created admins file: " + file.getAbsolutePath());
            } catch (IOException e) {
                // If creation fails, print error and return empty list
                System.err.println("Error creating admins file " + filePath + ": " + e.getMessage());
                return admins;
            }
        }

        // Read the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Admin admin = null;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                line = line.trim();
                if (line.isEmpty()) continue;

                // Start a new admin record
                if (line.startsWith("--- Admin Start:") || line.startsWith("--- User Start:")) {
                    admin = new Admin(null, null, null, null);
                } else if (line.startsWith("--- Admin End ---") || line.startsWith("--- User End ---")) {
                    // Check if admin data is valid
                    if (admin != null && admin.getEmail() != null && admin.getPassword() != null) {
                        // Add valid admin to the list
                        admins.add(admin);
                    } else {
                        // Print error if admin data is incomplete
                        System.err.println("Incomplete admin data in " + filePath + ": " + admin);
                    }
                    admin = null;
                } else if (admin != null) {
                    // Split the line into key and value
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        // Set the right field based on the key
                        switch (key) {
                            case "adminNumber":
                            case "userNumber": // Support old files that used userNumber
                                admin.setAdminNumber(value);
                                break;
                            case "email":
                            case "username": // Support old files that used username
                                admin.setEmail(value);
                                break;
                            case "password":
                                admin.setPassword(value);
                                break;
                            case "role":
                                admin.setRole(value);
                                break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            // If reading fails, print error and return null
            System.err.println("Error reading admins file " + filePath + ": " + e.getMessage());
            return null;
        }
        // Print how many admins were read
        System.out.println("Read " + admins.size() + " admins from " + filePath);
        return admins;
    }

    // Reads an admin by email from admins.txt, used by LoginServlet to verify admin credentials
    public static Admin readAdminByEmail(String email, String filePath) {
        // Read the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Admin admin = null;
            while ((line = reader.readLine()) != null) {
                // Start a new admin record
                if (line.startsWith("--- Admin Start:") || line.startsWith("--- User Start:")) {
                    String adminNumber = null, adminEmail = null, password = null, role = null;
                    // Read all lines until the record ends
                    while ((line = reader.readLine()) != null &&
                            !(line.startsWith("--- Admin End ---") || line.startsWith("--- User End ---"))) {
                        if (line.startsWith("adminNumber=") || line.startsWith("userNumber=")) {
                            adminNumber = line.split("=", 2)[1];
                        } else if (line.startsWith("email=") || line.startsWith("username=")) {
                            adminEmail = line.split("=", 2)[1];
                        } else if (line.startsWith("password=")) {
                            password = line.split("=", 2)[1];
                        } else if (line.startsWith("role=")) {
                            role = line.split("=", 2)[1];
                        }
                    }
                    // If the email matches, create an Admin object
                    if (adminEmail != null && adminEmail.equals(email)) {
                        admin = new Admin(adminNumber, adminEmail, password, role);
                        break;
                    }
                }
            }
            return admin;
        } catch (IOException e) {
            // If reading fails, print error and return null
            System.out.println("Error reading admin from file: " + e.getMessage());
            return null;
        }
    }

    // Writes the logged-in user to loggedInUser.txt, used by LoginServlet and RegisterServlet to track the current user
    public static synchronized void writeLoggedInUser(String filePath, User user) {
        // Create a File object for loggedInUser.txt
        File file = new File(filePath);
        // If the file doesn’t exist, create it
        if (!file.exists()) {
            try {
                // Create the folder if needed
                file.getParentFile().mkdirs();
                // Create the file
                file.createNewFile();
                // Print a message to confirm
                System.out.println("Created loggedInUser file: " + file.getAbsolutePath());
            } catch (IOException e) {
                // If creation fails, print error and stop
                System.err.println("Error creating loggedInUser file " + filePath + ": " + e.getMessage());
                return;
            }
        }

        // Write the user data to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            if (user != null) {
                // Write the start of the user record
                writer.write("--- User Start: " + user.getUserNumber() + " ---\n");
                // Write all user details
                writer.write("username=" + (user.getUsername() != null ? user.getUsername() : "") + "\n");
                writer.write("password=" + (user.getPassword() != null ? user.getPassword() : "") + "\n");
                writer.write("userNumber=" + (user.getUserNumber() != null ? user.getUserNumber() : "") + "\n");
                writer.write("fullName=" + (user.getFullName() != null ? user.getFullName() : "") + "\n");
                writer.write("email=" + (user.getEmail() != null ? user.getEmail() : "") + "\n");
                writer.write("phoneNumber=" + (user.getPhoneNumber() != null ? user.getPhoneNumber() : "") + "\n");
                writer.write("address=" + (user.getAddress() != null ? user.getAddress() : "") + "\n");
                // Write the end of the user record
                writer.write("--- User End ---\n");
                // Print a message to confirm
                System.out.println("Wrote logged-in user to " + filePath + ": " + user.getEmail());
            } else {
                // Print error if trying to write a null user
                System.err.println("Cannot write null user to " + filePath);
            }
        } catch (IOException e) {
            // If writing fails, print error
            System.err.println("Error writing to loggedInUser file " + filePath + ": " + e.getMessage());
        }
    }

    // Clears loggedInUser.txt, used by LogoutServlet to remove the logged-in user’s data
    public static synchronized void clearLoggedInUser(String filePath) {
        // Create a File object for loggedInUser.txt
        File file = new File(filePath);
        // Check if the file exists
        if (file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                // Clear the file by writing nothing
                writer.write("");
                // Print a message to confirm
                System.out.println("Cleared loggedInUser file: " + filePath);
            } catch (IOException e) {
                // If clearing fails, print error
                System.err.println("Error clearing loggedInUser file " + filePath + ": " + e.getMessage());
            }
        } else {
            // If the file doesn’t exist, print a message (no action needed)
            System.out.println("loggedInUser file does not exist at " + filePath + "; nothing to clear.");
        }
    }
}