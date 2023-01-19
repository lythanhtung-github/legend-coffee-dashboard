package com.cg.domain.enums;

public enum EnumFileType {
    IMAGE("image"),
    VIDEO("video");

    private final String value;

    EnumFileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
