package com.cg.domain.enums;

public enum EnumOrderStatus {

    PAID("Thanh toán"),
    UNPAID("Chưa thanh toán");

    private final String value;

    EnumOrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
