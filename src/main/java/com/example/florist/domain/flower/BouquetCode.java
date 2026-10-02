package com.example.florist.domain.flower;

public record BouquetCode(int value) implements Comparable<BouquetCode> {
    public BouquetCode {
        if (value <= 0) {
            throw new IllegalArgumentException("Bouquet code must be positive: " + value);
        }
    }

    @Override
    public int compareTo(BouquetCode other) {
        return Integer.compare(this.value, other.value);
    }
}
