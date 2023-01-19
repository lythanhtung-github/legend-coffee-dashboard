package com.cg.domain.enums;

public enum EnumTableStatus {
    EMPTY("Trống"),
    OPEN("Mở"),
    HANDLING("Nhập bếp"),
    SERVICE("Đã phục vụ");

    private final String value;

    EnumTableStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
