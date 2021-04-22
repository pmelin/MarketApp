package com.example.marketapp.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * class that represents a product.
 */
public class Product implements Serializable {
    private final UUID id;
    private final String name;
    private final Double price;


    public Product(UUID id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    // equals and hashcode matches the "id" field
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String toString()
    {
        return "Bought " + name + " that costed " + price;
    }
}
