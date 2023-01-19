package com.cg.domain.dto.order;

import com.cg.domain.dto.orderItem.OrderItemResponseDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderPayDTO {

    private Long orderId;

    private String staffName;

    private String tableName;

    private BigDecimal totalAmount;

   private Date creatAt;


    @Valid
    private List<OrderItemResponseDTO> orderItems;

}
