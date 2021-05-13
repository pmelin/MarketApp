package com.example.marketapp.model;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * Class that holds the products added to the shopping cart.
 */
public class ShoppingCart {
    private final static Set<Product> products = new HashSet<>();

    /*private final static Set<Voucher> vouchers = new HashSet<>(
            Arrays.asList(
                    new Voucher((long) 1, "Voucher 1", 10),
                    new Voucher((long) 2, "Voucher 2",10)));*/

    /**
     * Adds / replaces a product on the shopping cart.
     */
    public static void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Removes a product from the shopping cart.
     */
    public static void removeProduct(Product product) {
        products.remove(product);
    }

    public static void removeAllProduct() { products.clear();
    }

    /**
     * Retrieves the list of products on the shopping cart.
     */
    public static Set<Product> getProducts() {
        return Collections.unmodifiableSet(products);
    }

    /**
     * Returns the total amount of the shopping cart.
     */
    public static Double getTotal(int voucherDiscount) {
        // sums the price of all products
        double sum = products.stream().mapToDouble(Product::getPrice).reduce(0, Double::sum);
        // rounds the sum to 2 decimals
        if (voucherDiscount != 0)
        {
            float discount = (100-voucherDiscount)/100f;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            return Double.valueOf(decimalFormat.format((Math.round(sum * 100.0) / 100.0 * discount)));
        } else {
            return (Math.round(sum * 100.0) / 100.0);
        }

    }

    /**
     * Returns the indication if the shopping cart is empty.
     */
    public static boolean isEmpty() {
        return products.isEmpty();
    }
}
