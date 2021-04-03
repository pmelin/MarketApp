package com.example.marketapp.model;

import java.util.Date;
import java.util.Objects;

public class Transaction {

    private final Long id;
    private final double total;
    private final Date date;

    public Transaction(Long id, double total, Date date) {
        this.id = id;
        this.total = total;
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public Date getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction transaction = (Transaction) o;
        return Objects.equals(id, transaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
