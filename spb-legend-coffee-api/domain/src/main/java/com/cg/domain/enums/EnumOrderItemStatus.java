package com.cg.domain.enums;

public enum EnumOrderItemStatus {

    NEW("Mới"),
    COOKING("Đang làm"),
    WAITER("Chờ cung ứng"),

    DELIVERY("Đã giao"),

    DONE("Hoàn tất");

    private final String value;

    EnumOrderItemStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
