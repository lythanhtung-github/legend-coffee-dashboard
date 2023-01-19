package com.cg.domain.dto.orderItem;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class OrderItemKitchenDTO {

    private Long orderItemId;

    private Long productId;

    private String productName;


    private String tableName;

    private String size;

    private int quantity;

    private int quantityDelivery;

    private Date createdAt;

    private Date updatedAt;

    private Long orderId;

    private Long tableId;


    public OrderItemKitchenDTO(Long productId, String title, String size, Long quantity, Long quantityDelivery) {
        this.productId = productId;
        this.productName = title;
        this.size = size;
        this.quantity = Integer.parseInt(String.valueOf(quantity));
        this.quantityDelivery = Integer.parseInt(String.valueOf(quantityDelivery));

    }

    public OrderItemKitchenDTO(Long orderItemId, Long productId ,String title, String tableName, String size, int quantity, int quantityDelivery, Date createdAt, Date updatedAt) {
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.productName = title;
        this.tableName = tableName;
        this.size = size;
        this.quantity = quantity;
        this.quantityDelivery = quantityDelivery;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }


    public OrderItemKitchenDTO(Long orderItemId, Long productId ,String title, String tableName, String size, int quantity, int quantityDelivery, Date createdAt, Date updatedAt, Long orderId, Long tableId) {
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.productName = title;
        this.tableName = tableName;
        this.size = size;
        this.quantity = quantity;
        this.quantityDelivery = quantityDelivery;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderId = orderId;
        this.tableId = tableId;

    }
}
