package com.cg.domain.dto.orderItem;


import com.cg.domain.entity.Order;
import com.cg.domain.entity.OrderItem;
import com.cg.domain.entity.Product;
import com.cg.domain.enums.EnumOrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemResponseDTO {

    private Long id;

    private String size;

    private String productName;

    private String productPhoto;

    private BigDecimal price;

    private int quantity;

    private int quantityDelivery;

    private BigDecimal amount;

    private String note;

    private Long tableId;

    private Long productId;

    private Long orderId;

    private String orderItemStatus;


    public OrderItemResponseDTO(Long id, String size , BigDecimal price, int quantity, int quantityDelivery , BigDecimal amount, String note, Long tableId , Product product, Order order, EnumOrderItemStatus orderItemStatus) {
        this.id = id;
        this.productName = product.getTitle();
        this.productPhoto = product.getProductImage().getFileUrl();
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.quantityDelivery = quantityDelivery;
        this.amount = amount;
        this.note = note;
        this.tableId = tableId;
        this.productId = product.getId();
        this.orderId = order.getId();
        this.orderItemStatus = String.valueOf(orderItemStatus);
    }

    public OrderItem toOrderItem(Product product, Order order){
        return new OrderItem()
                .setId(id)
                .setSize(size)
                .setPrice(price)
                .setQuantity(quantity)
                .setQuantityDelivery(quantityDelivery)
                .setAmount(amount)
                .setNote(note)
                .setTableId(tableId)
                .setOrderItemStatus(EnumOrderItemStatus.valueOf(orderItemStatus))
                .setProduct(product)
                .setOrder(order);
    }
}
