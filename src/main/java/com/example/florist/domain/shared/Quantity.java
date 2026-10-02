package com.example.florist.domain.shared;

public record Quantity(int value) implements Comparable<Quantity> {
    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        if (value < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative: " + value);
        }
    }

    public Quantity add(Quantity other) {
        return new Quantity(this.value + other.value);
    }

    public Quantity subtract(Quantity other) {
        if (this.value < other.value) {
            throw new IllegalArgumentException("Cannot subtract " + other.value + " from " + this.value);
        }
        return new Quantity(this.value - other.value);
    }

    public boolean isGreaterThanOrEqual(Quantity other) {
        return this.value >= other.value;
    }

    public boolean isPositive() {
        return this.value > 0;
    }

    @Override
    public int compareTo(Quantity other) {
        return Integer.compare(this.value, other.value);
    }
}
