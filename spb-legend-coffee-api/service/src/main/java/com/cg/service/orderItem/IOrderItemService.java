package com.cg.service.orderItem;

import com.cg.domain.dto.orderItem.OrderItemDTO;
import com.cg.domain.dto.orderItem.OrderItemKitchenDTO;
import com.cg.domain.dto.orderItem.OrderItemKitchenTableDTO;
import com.cg.domain.dto.orderItem.OrderItemResponseDTO;
import com.cg.domain.dto.report.ReportProductDTO;
import com.cg.domain.entity.OrderItem;
import com.cg.domain.enums.EnumOrderItemStatus;
import com.cg.service.IGeneralService;

import java.util.List;

public interface IOrderItemService extends IGeneralService<OrderItem> {
    List<OrderItemDTO> getOrderItemDTOByOrderIdAndStatus(Long orderId, EnumOrderItemStatus enumOrderItemStatus);

    List<OrderItemDTO> getOrderItemDTOByOrderId(Long orderId);

    List<OrderItemDTO> getOrderItemByProductAndSizeAndStatus(EnumOrderItemStatus orderItemStatus, Long productId, String size);

    List<OrderItemDTO> getOrderItemByProductAndSizeAndStatusAndTable(EnumOrderItemStatus orderItemStatus,
                                                                     Long productId,
                                                                     String size,
                                                                     Long tableId);

    List<OrderItemResponseDTO> getOrderItemResponseDTOByOrderIdAndOrderItemStatus(Long orderId, EnumOrderItemStatus enumOrderItemStatus);

    List<OrderItemResponseDTO> getOrderItemResponseDTOByOrderId(Long orderId);

    List<OrderItemKitchenDTO> getOrderItemByStatusGroupByProduct(EnumOrderItemStatus orderItemStatus);

    List<OrderItemKitchenDTO> getOrderItemByStatusWithTable(EnumOrderItemStatus orderItemStatus);

    List<OrderItemKitchenDTO> getOrderItemByStatusWithTable2(EnumOrderItemStatus orderItemStatus);

    List<OrderItemKitchenTableDTO> getOrderItemByStatusAndTable(EnumOrderItemStatus orderItemStatus, Long tableId);

    void changeStatusFromCookingToWaiterToProduct(Long productId, String size);


    void changeStatusFromCookingToWaiterToTable(OrderItem orderItem);

    void changeStatusFromWaiterToDeliveryToTable(OrderItem orderItem);

    void checkExitsOrderItemWithStatus(OrderItem itemNew, EnumOrderItemStatus orderItemStatus);

    void checkExitsOrderItemWithWaiter(OrderItem itemNew);

    void checkExitsOrderItemWithDelivery(OrderItem itemNew);

    void checkExitsOrderItemWithDone(OrderItem itemNew);

    void removeWithQuantity(OrderItem orderItem, int quantity);

    void softDelete(Long id);

    void updateStatus(EnumOrderItemStatus orderItemStatus, Long orderItemId);

    void updateQuantity(int quantity, Long orderItemId);

    List<ReportProductDTO> getTop5Product();
}
