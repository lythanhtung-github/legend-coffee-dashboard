package com.cg.domain.dto.orderItem;

import com.cg.domain.entity.Order;
import com.cg.domain.entity.Product;
import com.cg.domain.enums.EnumOrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemCreateDTO {
    private Long id;

    private String size;

    private BigDecimal price;

    private int quantity;

    private int quantityDelivery;

    private BigDecimal amount;

    private String note;

    private Long tableId;

    private Long productId;

    private Long orderId;

    private String orderItemStatus;


    public OrderItemCreateDTO(Long id, String size , BigDecimal price, int quantity, int quantityDelivery , BigDecimal amount, String note, Long tableId , Product product, Order order, EnumOrderItemStatus orderItemStatus) {
        this.id = id;
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

    public OrderItemResponseDTO toOrderItemResponseDTO(Product product){
        return new OrderItemResponseDTO()
                .setId(id)
                .setProductName(product.getTitle())
                .setProductPhoto(product.getProductImage().getFileUrl())
                .setSize(size)
                .setPrice(price)
                .setQuantity(quantity)
                .setQuantityDelivery(quantityDelivery)
                .setAmount(amount)
                .setNote(note)
                .setTableId(tableId)
                .setProductId(productId)
                .setOrderId(orderId)
                .setOrderItemStatus(orderItemStatus);
    }
}
