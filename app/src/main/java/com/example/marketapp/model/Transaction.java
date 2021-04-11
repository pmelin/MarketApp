package com.example.marketapp.model;

import java.util.Date;
import java.util.Objects;

public class Transaction {

    private final String voucherValue;
    private final Long carID;
    private final double total;
    private final String date;

    public Transaction(String voucher, long carID, long total, String date) {
        this.voucherValue = voucher;
        this.date = date;
        this.carID = carID;
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public String getClientID() {
        return voucherValue;
    }

    public Long getCarID() {
        return carID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction transaction = (Transaction) o;
        return Objects.equals(voucherValue, transaction.voucherValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voucherValue);
    }

    public String toString()
    {
        return "You bought a total value of " + total + " on the date of " + date;
    }
}
