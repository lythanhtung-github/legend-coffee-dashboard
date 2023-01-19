package com.cg.domain.entity;

import com.cg.domain.dto.orderItem.OrderItemDTO;
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
@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal price;

    private int quantity;

    private int quantityDelivery;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal amount;

    private String note;

    private Long tableId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_item_status", length = 50)
    private EnumOrderItemStatus orderItemStatus;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItemDTO toOrderItemDTO() {

        return new OrderItemDTO()
                .setId(id)
                .setSize(size)
                .setPrice(price)
                .setQuantity(quantity)
                .setQuantityDelivery(quantityDelivery)
                .setAmount(amount)
                .setNote(note)
                .setTableId(tableId)
                .setProduct(product.toProductDTO())
                .setOrder(order.toOrderDTO())
                .setOrderItemStatus(String.valueOf(orderItemStatus));
    }
}
