package com.example.marketapp.model;

import java.util.Objects;

public class Voucher {
    private Long id;
    private String name;
    private int discount;

    public Voucher(Long id, String name, int discount) {
        this.id = id;
        this.name = name;
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voucher voucher = (Voucher) o;
        return Objects.equals(id, voucher.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String toString()
    {
        return "Voucher id:" + id + ", discount given:" + discount;
    }

}
