package com.example.florist.domain.shared;

public record Days(int value) implements Comparable<Days> {
    public static final Days ZERO = new Days(0);

    public Days {
        if (value < 0) {
            throw new IllegalArgumentException("Days cannot be negative: " + value);
        }
    }

    @Override
    public int compareTo(Days other) {
        return Integer.compare(this.value, other.value);
    }
}
