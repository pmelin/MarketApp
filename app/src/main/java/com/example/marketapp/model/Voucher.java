package com.example.marketapp.model;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class Voucher {
    private final Long id;
    private final String name;

    public Voucher(Long id, String name) {
        this.id = id;
        this.name = name;
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

}
