package com.example.marketapp.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class that holds the products added to the shopping cart.
 */
public class ShoppingCart {
    private final static Set<CartProduct> products = new HashSet<>();
    private final static Set<Voucher> vouchers = new HashSet<>();

    /**
     * Adds / replaces a product on the shopping cart.
     */
    public static void addProduct(CartProduct product) {
        // makes sure the new value replaces the old one
        removeProduct(product);
        products.add(product);
    }

    /**
     * Adds / replaces a voucher on the shopping cart.
     */
    public static void addVoucher(Voucher voucher) {
        // makes sure the new value replaces the old one
        removeVoucher(voucher);
        vouchers.add(voucher);
    }

    /**
     * Removes a product from the shopping cart.
     */
    public static void removeProduct(CartProduct product) {
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
    public static Set<CartProduct> getProducts() {

        Product product1 = new Product((long) 1, "Maça", 3.5);
        Product product2 = new Product((long) 2, "Limão", 4.5);
        Product product3 = new Product((long) 3, "Morango", 6.5);

        CartProduct cartProduct1 = new CartProduct((product1));
        CartProduct cartProduct2 = new CartProduct((product2));
        CartProduct cartProduct3 = new CartProduct((product3));

        addProduct(cartProduct1);
        addProduct(cartProduct2);
        addProduct(cartProduct3);

        return Collections.unmodifiableSet(products);
    }

    /**
     * Retrieves the list of products on the shopping cart.
     */
    public static Set<Voucher> getVouchers() {
        Voucher voucher1 = new Voucher((long) 1, "Voucher 1");
        addVoucher(voucher1);

        Voucher voucher2 = new Voucher((long) 2, "Voucher 2");
        addVoucher(voucher2);

        return Collections.unmodifiableSet(vouchers);
    }
}
