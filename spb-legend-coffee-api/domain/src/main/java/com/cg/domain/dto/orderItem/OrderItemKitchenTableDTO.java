package com.cg.domain.dto.orderItem;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderItemKitchenTableDTO {

    private Long orderItemId;
    private String tableName;
    private Long productId;

    private String productName;

    private String size;

    private int quantity;

    private int quantityDelivery;


    private Date createdAt;

    public OrderItemKitchenTableDTO( Long orderItemId,String tableName, Long productId, String title, String size, int quantity, int quantityDelivery, Date createdAt) {
        this.orderItemId = orderItemId;
        this.tableName = tableName;
        this.productId = productId;
        this.productName = title;
        this.size = size;
        this.quantity = quantity;
        this.quantityDelivery = quantityDelivery;
        this.createdAt = createdAt;

    }
}
