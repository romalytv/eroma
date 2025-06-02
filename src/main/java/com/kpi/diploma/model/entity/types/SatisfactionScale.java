package com.kpi.diploma.model.entity.types;

import lombok.Getter;

@Getter
public enum SatisfactionScale {
    VERY_HAPPY("Дуже задоволений", "text-green-soft fw-bold"),
    HAPPY("Задоволений", "text-success fw-bold"),
    NEUTRAL("Нейтральний", "text-secondary"),
    UNHAPPY("Незадоволений", "text-warning fw-bold"),
    VERY_UNHAPPY("Дуже незадоволений", "text-danger fw-bold");

    private final String displayName;
    private final String cssClass;

    SatisfactionScale(String displayName, String cssClass) {
        this.displayName = displayName;
        this.cssClass = cssClass;
    }

}