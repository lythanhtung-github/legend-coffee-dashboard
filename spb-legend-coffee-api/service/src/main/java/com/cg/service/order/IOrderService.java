package com.cg.service.order;

import com.cg.domain.dto.order.*;
import com.cg.domain.dto.orderItem.OrderItemKitchenTableDTO;
import com.cg.domain.dto.report.ReportDayToDayDTO;
import com.cg.domain.dto.report.ReportDTO;
import com.cg.domain.dto.report.ReportYearDTO;
import com.cg.domain.entity.Order;
import com.cg.domain.enums.EnumOrderItemStatus;
import com.cg.domain.enums.EnumOrderStatus;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderService extends IGeneralService<Order> {
    List<OrderDTO> getAllOrderDTOWhereDeletedIsFalse();

    List<OrderDTO> getAllOrderDTOByDayToDay(String startDay, String endDay);

    List<OrderDTO> getOrderDTOByStatus(EnumOrderStatus orderStatus);


    List<OrderKitchenDTO> getAllOrderKitchenByTable(EnumOrderItemStatus orderItemStatus);

    int countProductInOrder(List<OrderItemKitchenTableDTO> orderItemList);

    List<OrderDTO> getOrderDTOByTableIdAndOrderStatus(Long tableId, EnumOrderStatus orderStatus);


    OrderResponseDTO createWithOrderItems(OrderCreateWithOrderItemDTO orderCreateWithOrderItemDTO);

    OrderResponseDTO updateWithOrderItems(OrderCreateWithOrderItemDTO orderCreateWithOrderItemDTO);

    BigDecimal calculateTotalAmount(Long orderId);

    List<OrderCountCurrentMonthDTO> countOrderOfCurrentDay();

    void softDelete(Long id);

    List<ReportYearDTO> getReportByYear(int year);

    List<ReportYearDTO> getReportByMonth(int month, int year);

    List<ReportDTO> getReportOfCurrentMonth();

    List<ReportDTO> getReportOfDay(String day);

    List<ReportDayToDayDTO> getReportFromDayToDay(String startDay, String endDay);

    void deleteOrderById(Long orderId);

    OrderPayDTO pay(Order order);
}
