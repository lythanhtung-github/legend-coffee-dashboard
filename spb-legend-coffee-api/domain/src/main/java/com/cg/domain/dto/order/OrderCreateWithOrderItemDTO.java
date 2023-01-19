package com.cg.domain.dto.order;

import com.cg.domain.dto.orderItem.OrderItemCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderCreateWithOrderItemDTO {

    private Long orderId;

    private Long staffId;

    private Long tableId;


    @Valid
    private List<OrderItemCreateDTO> orderItems;
}
