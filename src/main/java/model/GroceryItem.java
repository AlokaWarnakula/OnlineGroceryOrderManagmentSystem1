package model;

public class GroceryItem {
    private int productID;
    private String productCategory;
    private String productName;
    private double productPrice;
    private String productImageLink;
    private int quantity; // Quantity in cart
    private String description;

    public GroceryItem(int productID, String productCategory, String productName, double productPrice, String productImageLink, int quantity, String description) {
        this.productID = productID;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageLink = productImageLink;
        this.quantity = quantity;
        this.description = description;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImageLink() {
        return productImageLink;
    }

    public void setProductImageLink(String productImageLink) {
        this.productImageLink = productImageLink;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotalPrice() {
        return productPrice * quantity;
    }
}