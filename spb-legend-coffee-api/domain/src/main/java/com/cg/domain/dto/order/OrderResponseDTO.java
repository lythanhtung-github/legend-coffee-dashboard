package com.cg.domain.dto.order;

import com.cg.domain.dto.orderItem.OrderItemResponseDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {

    private Long orderId;

    private Long staffId;

    private Long tableId;

    private BigDecimal totalAmount;


    @Valid
    private List<OrderItemResponseDTO> orderItems;

}
