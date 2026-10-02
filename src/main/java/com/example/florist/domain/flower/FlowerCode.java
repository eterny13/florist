package com.example.florist.domain.flower;

public record FlowerCode(int value) implements Comparable<FlowerCode> {
    public FlowerCode {
        if (value <= 0) {
            throw new IllegalArgumentException("Flower code must be positive: " + value);
        }
    }

    @Override
    public int compareTo(FlowerCode other) {
        return Integer.compare(this.value, other.value);
    }
}
