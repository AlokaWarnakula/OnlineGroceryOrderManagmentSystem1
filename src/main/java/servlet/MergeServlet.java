package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import model.GroceryItem;
import java.util.ArrayList;

// MergeServlet implements merge sort to sort GroceryItem lists for the cart page
public class MergeServlet extends HttpServlet {

    // Enum for sorting criteria
    public enum SortCriterion {
        NAME,  // Sort name alphabetically
        PRICE  // Sort price in ascending order
    }

    // Sorts a list of GroceryItems using merge sort
    public void sortItems(ArrayList<GroceryItem> items, SortCriterion criterion) {
        // Skip if list is null or empty
        if (items == null || items.isEmpty()) {
            System.out.println("MergeServlet: Items list is null or empty, skipping sort");
            return;
        }
        // Start merge sort on the entire list
        System.out.println("MergeServlet: Applying Merge Sort with criterion=" + criterion);
        mergeSort(items, 0, items.size() - 1, criterion);
        System.out.println("MergeServlet: Items after Merge Sort: " + items);
    }

    // Default method to sort by name
    public void sortItems(ArrayList<GroceryItem> items) {
        // sortItems with NAME criterion
        sortItems(items, SortCriterion.NAME);
    }

    // Recursively sorts the list using merge sort
    private void mergeSort(ArrayList<GroceryItem> items, int left, int right, SortCriterion criterion) {
        // Divide array if multiple elements exist
        if (left < right) {
            // Calculate midpoint
            int mid = left + (right - left) / 2;
            System.out.println("MergeServlet: Splitting at mid=" + mid);
            // Sort left and right sub-arrays
            mergeSort(items, left, mid, criterion);
            mergeSort(items, mid + 1, right, criterion);
            // Merge sorted sub-arrays
            merge(items, left, mid, right, criterion);
        }
        System.out.println("MergeServlet: Finished mergeSort for range left=" + left + ", right=" + right);
    }

    // Merges two sorted sub-arrays into one
    private void merge(ArrayList<GroceryItem> items, int left, int mid, int right, SortCriterion criterion) {
        System.out.println("MergeServlet: Merging from left=" + left + ", mid=" + mid + ", right=" + right);
        // Calculate subarray sizes
        int n1 = mid - left + 1;
        int n2 = right - mid;
        // Create temporary arrays
        ArrayList<GroceryItem> leftArray = new ArrayList<>(n1);
        ArrayList<GroceryItem> rightArray = new ArrayList<>(n2);

        // Copy data to temporary arrays
        for (int i = 0; i < n1; i++) {
            leftArray.add(items.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            rightArray.add(items.get(mid + 1 + j));
        }

        // Merge arrays back into original list
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            GroceryItem leftItem = leftArray.get(i);
            GroceryItem rightItem = rightArray.get(j);
            int comparison = compareItems(leftItem, rightItem, criterion);
            System.out.println("MergeServlet: Comparison (" + criterion + ") - " + getItemKey(leftItem, criterion) + " vs " + getItemKey(rightItem, criterion) + " -> " + comparison);
            // Compare and place smaller item
            if (comparison <= 0) {
                items.set(k++, leftArray.get(i++));
            } else {
                items.set(k++, rightArray.get(j++));
            }
        }

        // Copy remaining items from left array
        while (i < n1) {
            System.out.println("MergeServlet: Copying remaining left item at index " + i);
            items.set(k++, leftArray.get(i++));
        }
        // Copy remaining items from right array
        while (j < n2) {
            System.out.println("MergeServlet: Copying remaining right item at index " + j);
            items.set(k++, rightArray.get(j++));
        }
        System.out.println("MergeServlet: Finished merging, items in range [" + left + "," + right + "]: " + items.subList(left, right + 1));
    }

    // Compares two items based on the criterion
    private int compareItems(GroceryItem item1, GroceryItem item2, SortCriterion criterion) {
        switch (criterion) {
            case NAME:
                // Compare names case-insensitively
                String name1 = item1.getProductName() != null ? item1.getProductName() : "";
                String name2 = item2.getProductName() != null ? item2.getProductName() : "";
                return name1.compareToIgnoreCase(name2);
            case PRICE:
                // Compare prices
                return Double.compare(item1.getProductPrice(), item2.getProductPrice());
            default:
                return 0;
        }
    }

    // Gets item name or price(Key) for logging
    private String getItemKey(GroceryItem item, SortCriterion criterion) {
        switch (criterion) {
            case NAME:
                return item.getProductName() != null ? item.getProductName() : "null";
            case PRICE:
                return String.valueOf(item.getProductPrice());
            default:
                return "";
        }
    }
}