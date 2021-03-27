package com.example.marketapp.model;

import java.util.Objects;

/**
 * Class that represents a product placed on a shopping cart.
 */
public class CartProduct {
    private final Product product;

    public CartProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    // adds hashcode here to make sure we only have one entry per product on the shopping cart set

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartProduct that = (CartProduct) o;
        return product.equals(that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }
}
