package com.cg.domain.dto.order;

import com.cg.domain.dto.orderItem.OrderItemKitchenTableDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderKitchenDTO {

    private Long tableId;

    private String tableName;

    private Date timeWait;

    private int countProduct;

    private Long orderId;

    private List<OrderItemKitchenTableDTO> orderItems;

}
