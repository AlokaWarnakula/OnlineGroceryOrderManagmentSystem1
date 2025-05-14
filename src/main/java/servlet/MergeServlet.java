// Package for servlet-related classes handling HTTP requests
package servlet;

// Import Jakarta Servlet API for servlet functionality
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
// Import model class for grocery items
import model.GroceryItem;
// Import utility for dynamic array operations
import java.util.ArrayList;

// MergeServlet implements merge sort for sorting GroceryItem lists by name or price
public class MergeServlet extends HttpServlet {

    // Enum defining criteria for sorting GroceryItem objects
    public enum SortCriterion {
        NAME,  // Sorts items alphabetically by product name
        PRICE  // Sorts items by price in ascending order
    }

    // Sorts a list of GroceryItems using merge sort with the specified criterion
    public void mergeSort(ArrayList<GroceryItem> items, int left, int right, SortCriterion criterion) {
        // Log sorting range and criterion for debugging
        System.out.println("MergeServlet: Starting mergeSort with left=" + left + ", right=" + right + ", items size=" + items.size() + ", criterion=" + criterion);
        // Proceed only if the subarray has multiple elements
        if (left < right) {
            // Calculate midpoint to divide the array, avoiding integer overflow
            int mid = left + (right - left) / 2;
            // Log the split point for debugging
            System.out.println("MergeServlet: Splitting at mid=" + mid);
            // Recursively sort the left subarray
            mergeSort(items, left, mid, criterion);
            // Recursively sort the right subarray
            mergeSort(items, mid + 1, right, criterion);
            // Merge the sorted subarrays
            merge(items, left, mid, right, criterion);
        }
        // Log completion of sorting for this range
        System.out.println("MergeServlet: Finished mergeSort for range left=" + left + ", right=" + right);
    }

    // Merges two sorted subarrays into a single sorted array based on the criterion
    private void merge(ArrayList<GroceryItem> items, int left, int mid, int right, SortCriterion criterion) {
        // Log merging range for debugging
        System.out.println("MergeServlet: Merging from left=" + left + ", mid=" + mid + ", right=" + right);
        // Calculate sizes of left and right subarrays
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Initialize temporary arrays for left and right subarrays
        ArrayList<GroceryItem> leftArray = new ArrayList<>(n1);
        ArrayList<GroceryItem> rightArray = new ArrayList<>(n2);

        // Copy items to temporary left array
        for (int i = 0; i < n1; i++) {
            leftArray.add(items.get(left + i));
        }
        // Copy items to temporary right array
        for (int j = 0; j < n2; j++) {
            rightArray.add(items.get(mid + 1 + j));
        }

        // Merge items back into the original array
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            // Get items from left and right subarrays
            GroceryItem leftItem = leftArray.get(i);
            GroceryItem rightItem = rightArray.get(j);

            // Compare items using the specified criterion
            int comparison = compareItems(leftItem, rightItem, criterion);
            // Log comparison details for debugging
            System.out.println("MergeServlet: Comparison (" + criterion + ") - " + getItemKey(leftItem, criterion) + " vs " + getItemKey(rightItem, criterion) + " -> " + comparison);

            // Place the smaller (or equal) item in the result array
            if (comparison <= 0) {
                items.set(k++, leftArray.get(i++));
            } else {
                items.set(k++, rightArray.get(j++));
            }
        }

        // Copy any remaining items from left subarray
        while (i < n1) {
            // Log copying of remaining left item for debugging
            System.out.println("MergeServlet: Copying remaining left item at index " + i);
            items.set(k++, leftArray.get(i++));
        }

        // Copy any remaining items from right subarray
        while (j < n2) {
            // Log copying of remaining right item for debugging
            System.out.println("MergeServlet: Copying remaining right item at index " + j);
            items.set(k++, rightArray.get(j++));
        }

        // Log the merged subarray for debugging
        System.out.println("MergeServlet: Finished merging, items in range [" + left + "," + right + "]: " + items.subList(left, right + 1));
    }

    // Compares two GroceryItems based on the specified criterion
    private int compareItems(GroceryItem item1, GroceryItem item2, SortCriterion criterion) {
        switch (criterion) {
            case NAME:
                // Compare names case-insensitively, handling nulls as empty strings
                String name1 = item1.getProductName() != null ? item1.getProductName() : "";
                String name2 = item2.getProductName() != null ? item2.getProductName() : "";
                return name1.compareToIgnoreCase(name2);
            case PRICE:
                // Compare prices using Double.compare for precision
                return Double.compare(item1.getProductPrice(), item2.getProductPrice());
            default:
                // Return 0 for invalid criteria to ensure safe handling
                return 0;
        }
    }

    // Retrieves the key (name or price) for logging comparison details
    private String getItemKey(GroceryItem item, SortCriterion criterion) {
        switch (criterion) {
            case NAME:
                // Return product name or "null" if name is absent
                return item.getProductName() != null ? item.getProductName() : "null";
            case PRICE:
                // Convert price to string for logging
                return String.valueOf(item.getProductPrice());
            default:
                // Return empty string for invalid criteria
                return "";
        }
    }

    // Sorts the items list using merge sort with the specified criterion
    public void sortItems(ArrayList<GroceryItem> items, SortCriterion criterion) {
        // Skip sorting if the list is null or empty
        if (items == null || items.isEmpty()) {
            // Log that sorting is skipped for debugging
            System.out.println("MergeServlet: Items list is null or empty, skipping sort");
            return;
        }
        // Log the start of sorting with criterion for debugging
        System.out.println("MergeServlet: Applying Merge Sort with criterion=" + criterion);
        // Perform merge sort on the entire list
        mergeSort(items, 0, items.size() - 1, criterion);
        // Log the sorted list for debugging
        System.out.println("MergeServlet: Items after Merge Sort: " + items);
    }

    // Sorts items by name for backward compatibility
    public void sortItems(ArrayList<GroceryItem> items) {
        // Delegate to sortItems with NAME criterion
        sortItems(items, SortCriterion.NAME);
    }
}