package com.kpi.diploma.model.entity.types;

import lombok.Getter;

@Getter
public enum DeliveryType {
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    REJECTED("Rejected");

    private final String displayName;

    DeliveryType(String displayName) {
        this.displayName = displayName;
    }

}

