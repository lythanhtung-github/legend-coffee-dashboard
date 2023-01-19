package com.cg.domain.enums;

public enum EnumGender {
    MALE("Nam"),
    FEMALE("Nữ");

    private final String value;

    EnumGender(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
