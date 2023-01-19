package com.cg.service.order;

import com.cg.domain.dto.order.*;
import com.cg.domain.dto.orderItem.OrderItemCreateDTO;
import com.cg.domain.dto.orderItem.OrderItemDTO;
import com.cg.domain.dto.orderItem.OrderItemKitchenTableDTO;
import com.cg.domain.dto.orderItem.OrderItemResponseDTO;
import com.cg.domain.dto.report.ReportDayToDayDTO;
import com.cg.domain.dto.report.ReportDTO;
import com.cg.domain.dto.report.ReportYearDTO;
import com.cg.domain.entity.*;
import com.cg.domain.enums.EnumOrderItemStatus;
import com.cg.domain.enums.EnumOrderStatus;
import com.cg.domain.enums.EnumTableStatus;
import com.cg.exception.DataInputException;
import com.cg.repository.OrderRepository;
import com.cg.service.orderItem.IOrderItemService;
import com.cg.service.product.IProductService;
import com.cg.service.staff.IStaffService;
import com.cg.service.table.ITableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ITableService tableService;

    @Autowired
    private IStaffService staffService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IOrderItemService orderItemService;

    @Autowired
    private IOrderService orderService;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }


    @Override
    public List<OrderDTO> getAllOrderDTOWhereDeletedIsFalse() {
        return orderRepository.getAllOrderDTOWhereDeletedIsFalse();
    }

    @Override
    public List<OrderDTO> getAllOrderDTOByDayToDay(String startDay, String endDay) {
        return orderRepository.getAllOrderDTOByDayToDay(startDay, endDay);
    }

    @Override
    public List<OrderDTO> getOrderDTOByStatus(EnumOrderStatus orderStatus) {
        return orderRepository.getOrderDTOByStatus(orderStatus);
    }

    @Override
    public List<OrderKitchenDTO> getAllOrderKitchenByTable(EnumOrderItemStatus orderItemStatus) {

        List<OrderKitchenDTO> orderList = new ArrayList<>();

        List<OrderDTO> orderDTOList = getOrderDTOByStatus(EnumOrderStatus.UNPAID);

        for (OrderDTO item : orderDTOList) {
            CTable table = item.getTable().toTable();

            List<OrderItemKitchenTableDTO> orderItemList = orderItemService.getOrderItemByStatusAndTable(orderItemStatus, table.getId());

            if (orderItemList.size() != 0) {
                int countProduct = this.countProductInOrder(orderItemList);

                OrderKitchenDTO orderKitchenDTO = new OrderKitchenDTO();

                orderKitchenDTO.setOrderId(item.getId())
                        .setTableId(table.getId())
                        .setTableName(table.getName())
                        .setCountProduct(countProduct)
                        .setTimeWait(item.getCreatedAt())
                        .setOrderItems(orderItemList);

                orderList.add(orderKitchenDTO);
            }
        }
        return orderList;
    }

    @Override
    public int countProductInOrder(List<OrderItemKitchenTableDTO> orderItemList) {
        int count = 0;
        for (OrderItemKitchenTableDTO item : orderItemList) {
            count += item.getQuantity();
        }
        return count;
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.getById(id);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void remove(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderDTO> getOrderDTOByTableIdAndOrderStatus(Long tableId, EnumOrderStatus orderStatus) {
        return orderRepository.getOrderDTOByTableIdAndOrderStatus(tableId, orderStatus);
    }

    @Override
    public OrderResponseDTO createWithOrderItems(OrderCreateWithOrderItemDTO orderCreateWithOrderItemDTO) {

        Order order = new Order();

        Long tableId = orderCreateWithOrderItemDTO.getTableId();
        Long staffId = orderCreateWithOrderItemDTO.getStaffId();

        Optional<CTable> tableOptional = tableService.findById(tableId);

        Optional<Staff> staffOptional = staffService.findById(staffId);

        order.setId(null)
                .setTotalAmount(new BigDecimal(0L))
                .setTable(tableOptional.get())
                .setStaff(staffOptional.get())
                .setOrderStatus(EnumOrderStatus.UNPAID);

        order = orderRepository.save(order);

        List<OrderItemCreateDTO> orderItems = orderCreateWithOrderItemDTO.getOrderItems();

        BigDecimal totalAmount = order.getTotalAmount();

        for (OrderItemCreateDTO item : orderItems) {

            Long productId = item.getProductId();
            Optional<Product> productOptional = productService.findById(productId);

            if (!productOptional.isPresent()) {
                throw new DataInputException("ID sản phẩm không hợp lệ.");
            }

            BigDecimal price = item.getPrice();
            int quantity = item.getQuantity();
            BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));

            OrderItem orderItem = new OrderItem();

            orderItem.setId(null)
                    .setSize(item.getSize())
                    .setPrice(price)
                    .setQuantity(quantity)
                    .setQuantityDelivery(0)
                    .setAmount(amount)
                    .setNote(item.getNote())
                    .setTableId(tableId)
                    .setProduct(productOptional.get())
                    .setOrder(order)
                    .setOrderItemStatus(EnumOrderItemStatus.COOKING);

            orderItemService.save(orderItem);

            totalAmount = totalAmount.add(amount);

        }

        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        List<OrderItemResponseDTO> orderItemResponseDTOS = orderItemService.getOrderItemResponseDTOByOrderId(order.getId());

        return new OrderResponseDTO()
                .setOrderId(order.getId())
                .setStaffId(order.getStaff().getId())
                .setTableId(order.getTable().getId())
                .setTotalAmount(order.getTotalAmount())
                .setOrderItems(orderItemResponseDTOS);

    }

    @Override
    public OrderResponseDTO updateWithOrderItems(OrderCreateWithOrderItemDTO orderCreateWithOrderItemDTO) {

        Long orderId = orderCreateWithOrderItemDTO.getOrderId();

        Optional<Order> orderOptional = orderService.findById(orderId);

        if (!orderOptional.isPresent()) {
            throw new DataInputException("ID hóa đơn không hợp lệ.");
        }

        Order order = orderOptional.get();


        List<OrderItemResponseDTO> orderItemListCurrent = orderItemService.getOrderItemResponseDTOByOrderIdAndOrderItemStatus(orderId, EnumOrderItemStatus.COOKING);

        List<OrderItemCreateDTO> orderItemListNew = orderCreateWithOrderItemDTO.getOrderItems();

        List<OrderItemResponseDTO> orderItemResponseResult = orderItemListCurrent;

        for (OrderItemCreateDTO itemNew : orderItemListNew) {
            boolean existItem = false;
            for (int i = 0; i < orderItemListCurrent.size(); i++) {

                OrderItemResponseDTO itemCurrent = orderItemListCurrent.get(i);

                if (itemNew.getProductId().equals(itemCurrent.getProductId())
                        && itemNew.getSize().equals(itemCurrent.getSize())) {

                    existItem = true;

                    itemNew.setAmount(itemNew.getPrice().multiply(BigDecimal.valueOf(itemNew.getQuantity())));

                    itemCurrent.setQuantity(itemCurrent.getQuantity() + itemNew.getQuantity());

                    itemCurrent.setAmount(itemCurrent.getAmount().add(itemNew.getAmount()));

                    orderItemResponseResult.set(i, itemCurrent);

                    break;
                }
            }

            if (!existItem) {
                Long productId = itemNew.getProductId();
                Optional<Product> productOptional = productService.findById(productId);

                if (!productOptional.isPresent()) {
                    throw new DataInputException("ID sản phẩm không hợp lệ.");
                }

                Product product = productOptional.get();

                orderItemResponseResult.add(itemNew.toOrderItemResponseDTO(product));
            }
        }


        for (OrderItemResponseDTO item : orderItemResponseResult) {
            if (item.getId() != null) {
                orderItemService.remove(item.getId());
            }
        }

        for (OrderItemResponseDTO item : orderItemResponseResult) {
            Long productId = item.getProductId();
            Optional<Product> productOptional = productService.findById(productId);

            if (!productOptional.isPresent()) {
                throw new DataInputException("ID sản phẩm không hợp lệ.");
            }

            Product product = productOptional.get();
            item.setOrderItemStatus(String.valueOf(EnumOrderItemStatus.COOKING));
            orderItemService.save(item.toOrderItem(product, order));
        }

        order.setTotalAmount(this.calculateTotalAmount(orderId));
        order = orderRepository.save(order);

        List<OrderItemResponseDTO> orderItemReturnDTOS = orderItemService.getOrderItemResponseDTOByOrderId(order.getId());

        return new OrderResponseDTO().setOrderId(order.getId())
                .setStaffId(order.getStaff().getId())
                .setTableId(order.getTable().getId())
                .setTotalAmount(order.getTotalAmount())
                .setOrderItems(orderItemReturnDTOS);

    }

    @Override
    public BigDecimal calculateTotalAmount(Long orderId) {
        List<OrderItemDTO> orderItemList = orderItemService.getOrderItemDTOByOrderId(orderId);
        BigDecimal totalAmount = new BigDecimal(0L);
        for (OrderItemDTO item : orderItemList) {
            BigDecimal price = item.getPrice();
            int quantity = item.getQuantity();
            BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));
            item.setAmount(amount);
            totalAmount = totalAmount.add(amount);
        }

        return totalAmount;
    }

    @Override
    public List<OrderCountCurrentMonthDTO> countOrderOfCurrentDay(){
        return orderRepository.countOrderOfCurrentDay();
    }

    @Override
    public void softDelete(Long id) {
        orderRepository.softDelete(id);
    }


    @Override
    public List<ReportYearDTO> getReportByYear(int year) {
        return orderRepository.getReportByYear(year);
    }

    @Override
    public List<ReportYearDTO> getReportByMonth(int month, int year) {
        return orderRepository.getReportByMonth(month, year);
    }

    @Override
    public List<ReportDTO> getReportOfCurrentMonth() {
        return orderRepository.getReportOfCurrentMonth();
    }

    @Override
    public List<ReportDTO> getReportOfDay(String day) {
        return orderRepository.getReportOfDay(day);
    }

    @Override
    public List<ReportDayToDayDTO> getReportFromDayToDay(String startDay, String endDay) {
        return orderRepository.getReportFromDayToDay(startDay, endDay);
    }

    @Override
    public void deleteOrderById(Long orderId){
            List<OrderItemDTO> orderItemDTOList = orderItemService.getOrderItemDTOByOrderIdAndStatus(orderId, EnumOrderItemStatus.COOKING);

            for (OrderItemDTO orderItemDTO : orderItemDTOList) {
                orderItemService.remove(orderItemDTO.getId());
            }

            List<OrderItemDTO> orderItemList = orderItemService.getOrderItemDTOByOrderId(orderId);

            if (orderItemList.size() == 0) {
                orderService.remove(orderId);
            } else {
                throw new DataInputException("Hiện tại vẫn còn sản phẩm đang chờ làm hoặc chờ cung ứng! Không thể hủy!");
            }
    }


    @Override
    public OrderPayDTO pay(Order order) {


        List<OrderItemDTO> orderItemDTOList = orderItemService.getOrderItemDTOByOrderIdAndStatus(order.getId(), EnumOrderItemStatus.COOKING);

        for (OrderItemDTO orderItemDTO : orderItemDTOList) {
            orderItemService.remove(orderItemDTO.getId());
        }

        List<OrderItemDTO> orderItemList = orderItemService.getOrderItemDTOByOrderId(order.getId());

        for (OrderItemDTO item : orderItemList) {
            if (item.toOrderItem().getOrderItemStatus() == EnumOrderItemStatus.WAITER) {
                throw new DataInputException("Hiện tại vẫn còn sản phẩm đang chờ cung ứng! Không thể thanh toán!");
            }
        }

        orderItemList = orderItemService.getOrderItemDTOByOrderId(order.getId());

        for (OrderItemDTO item : orderItemList) {
            if (item.toOrderItem().getOrderItemStatus() == EnumOrderItemStatus.DELIVERY) {
                orderItemService.checkExitsOrderItemWithDone(item.toOrderItem());
            }
        }

        OrderPayDTO orderPayDTO = new OrderPayDTO();

        List<OrderItemResponseDTO> orderItemResponseDTOS = orderItemService.getOrderItemResponseDTOByOrderId(order.getId());

        Staff staff = staffService.findById(order.getStaff().getId()).get();
        CTable table = tableService.findById(order.getTable().getId()).get();
        order.setOrderStatus(EnumOrderStatus.PAID);
        table.setStatus(EnumTableStatus.EMPTY);
        orderService.save(order);
        tableService.save(table);

        orderPayDTO.setOrderId(order.getId())
                .setTotalAmount(order.getTotalAmount())
                .setStaffName(staff.getFullName())
                .setTableName(table.getName())
                .setCreatAt(order.getCreatedAt())
                .setOrderItems(orderItemResponseDTOS);

        return orderPayDTO;
    }

}
