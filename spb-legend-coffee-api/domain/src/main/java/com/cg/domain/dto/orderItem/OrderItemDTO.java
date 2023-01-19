package com.cg.domain.dto.orderItem;

import com.cg.domain.dto.order.OrderDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.OrderItem;
import com.cg.domain.entity.Product;
import com.cg.domain.enums.EnumOrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemDTO {

    private Long id;

    private String size;

    private BigDecimal price;

    private int quantity;

    private int quantityDelivery;

    private BigDecimal amount;

    private String note;

    private Long tableId;

    private ProductDTO product;

    private OrderDTO order;


    private String orderItemStatus;

    private Date createdAt;

    public OrderItemDTO(Long id, String size ,BigDecimal price, int quantity, int quantityDelivery ,BigDecimal amount, String note, Long tableId ,Product product, Order order, EnumOrderItemStatus orderItemStatus) {
        this.id = id;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.quantityDelivery = quantityDelivery;
        this.amount = amount;
        this.note = note;
        this.tableId = tableId;
        this.product = product.toProductDTO();
        this.order = order.toOrderDTO();
        this.orderItemStatus = String.valueOf(orderItemStatus);
    }



    public OrderItemDTO(Long id, String size ,BigDecimal price, int quantity, int quantityDelivery ,BigDecimal amount, String note, Long tableId ,Product product, Order order, EnumOrderItemStatus orderItemStatus, Date createdAt) {
        this.id = id;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.quantityDelivery = quantityDelivery;
        this.amount = amount;
        this.note = note;
        this.tableId = tableId;
        this.product = product.toProductDTO();
        this.order = order.toOrderDTO();
        this.orderItemStatus = String.valueOf(orderItemStatus);
        this.createdAt = createdAt;
    }

    public OrderItem toOrderItem(){

        return (OrderItem) new OrderItem()
                .setId(id)
                .setSize(size)
                .setPrice(price)
                .setQuantity(quantity)
                .setQuantityDelivery(quantityDelivery)
                .setAmount(amount)
                .setNote(note)
                .setTableId(tableId)
                .setProduct(product.toProduct())
                .setOrder(order.toOrder())
                .setOrderItemStatus(EnumOrderItemStatus.valueOf(orderItemStatus))
                .setCreatedAt(createdAt);
    }
}
