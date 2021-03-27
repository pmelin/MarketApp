package com.example.marketapp.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class that holds the products added to the shopping cart.
 */
public class ShoppingCart {
    private final static Set<CartProduct> products = new HashSet<>();

    /**
     * Adds / replaces a product on the shopping cart.
     */
    public static void addProduct(CartProduct product) {
        // makes sure the new value replaces the old one
        removeProduct(product);
        products.add(product);
    }

    /**
     * Removes a product from the shopping cart.
     */
    public static void removeProduct(CartProduct product) {
        products.remove(product);
    }

    /**
     * Retrieves the list of products on the shopping cart.
     */
    public static Set<CartProduct> getProducts() {
        return Collections.unmodifiableSet(products);
    }
}
