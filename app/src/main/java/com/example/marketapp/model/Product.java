package com.example.marketapp.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * class that represents a product.
 */
public class Product implements Serializable {
    private final Long id;
    private final String name;
    private final Double price;


    public Product(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /**
     * Builds a product instance from a QR code value.
     */
    public static Product fromQrCode(String value) {
        System.out.println("QR code value: " + value);
        if (value == null || value.trim().length() == 0) {
            throw new RuntimeException("Invalid QR Code value.");
        }

        String[] parts = value.trim().split(";");

        if (parts.length != 4) {
            throw new RuntimeException("Invalid QR Code value.");
        }

        return new Product(
                Long.parseLong(parts[0]),
                parts[1].trim(),
                Double.parseDouble(parts[2])
                );
    }

    public Long getId() {
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
