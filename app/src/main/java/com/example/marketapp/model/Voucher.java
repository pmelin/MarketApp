package com.example.marketapp.model;

import java.util.Objects;

public class Voucher {
    private String code;
    private int discount;

    public Voucher(String code, int discount) {
        this.code = code;
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voucher voucher = (Voucher) o;
        return Objects.equals(code, voucher.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public String toString()
    {
        return "Voucher code:" + code + ", discount given:" + discount;
    }

}
