package com.cg.domain.dto.order;

import com.cg.domain.dto.orderItem.OrderItemKitchenDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class OrderKitChenResponseDTO {
    private List<Long> orderIdChangeList;
    private List<OrderItemKitchenDTO> orderItemResponseList;
}
