package com.example.marketapp.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Class that holds the products added to the shopping cart.
 */
public class ShoppingCart {
    private final static Set<Product> products = new HashSet<>(
            Arrays.asList(
                    new Product((long) 1, "Maça", 3.5),
                    new Product((long) 2, "Limão", 4.5),
                    new Product((long) 3, "Morango", 6.5)));

    private final static Set<Voucher> vouchers = new HashSet<>(
            Arrays.asList(
                    new Voucher((long) 1, "Voucher 1"),
                    new Voucher((long) 2, "Voucher 2")));

    /**
     * Adds / replaces a product on the shopping cart.
     */
    public static void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Adds / replaces a voucher on the shopping cart.
     */
    public static void addVoucher(Voucher voucher) {
        vouchers.add(voucher);
    }

    /**
     * Removes a product from the shopping cart.
     */
    public static void removeProduct(Product product) {
        products.remove(product);
    }

    /**
     * Removes a product from the shopping cart.
     */
    public static void removeVoucher(Voucher voucher) {
        vouchers.remove(voucher);
    }


    /**
     * Retrieves the list of products on the shopping cart.
     */
    public static Set<Product> getProducts() {
        return Collections.unmodifiableSet(products);
    }

    /**
     * Retrieves the list of products on the shopping cart.
     */
    public static Set<Voucher> getVouchers() {
        return Collections.unmodifiableSet(vouchers);
    }

    /**
     * Returns the total amount of the shopping cart.
     */
    public static Double getTotal() {
        // sums the price of all products
        double sum = products.stream().mapToDouble(Product::getPrice).reduce(0, Double::sum);
        // rounds the sum to 2 decimals
        return Math.round(sum * 100.0) / 100.0;
    }

    /**
     * Returns the indication if the shopping cart is empty.
     */
    public static boolean isEmpty() {
        return products.isEmpty();
    }
}
