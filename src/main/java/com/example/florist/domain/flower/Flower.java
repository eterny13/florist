package com.example.florist.domain.flower;

import lombok.Value;

@Value
public class Flower {
    int code;
    String name;
    int minUnitQuantity;
    int orderLeadTime;
    int daysOfBestQuality;
}
