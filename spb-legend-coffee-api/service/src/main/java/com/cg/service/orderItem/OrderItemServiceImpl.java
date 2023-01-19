package com.cg.service.orderItem;

import com.cg.domain.dto.orderItem.*;
import com.cg.domain.dto.report.ReportProductDTO;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.OrderItem;
import com.cg.domain.enums.EnumOrderItemStatus;
import com.cg.repository.OrderItemRepository;
import com.cg.service.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class OrderItemServiceImpl implements IOrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private IOrderService orderService;

    @Override
    public List<OrderItem> findAll() {
        return null;
    }

    @Override
    public List<OrderItemDTO> getOrderItemDTOByOrderIdAndStatus(Long orderId, EnumOrderItemStatus enumOrderItemStatus) {
        return orderItemRepository.getOrderItemDTOByOrderIdAndStatus(orderId, enumOrderItemStatus);
    }

    @Override
    public List<OrderItemDTO> getOrderItemDTOByOrderId(Long orderId) {
        return orderItemRepository.getOrderItemDTOByOrderId(orderId);
    }

    @Override
    public List<OrderItemDTO> getOrderItemByProductAndSizeAndStatus(EnumOrderItemStatus orderItemStatus,
                                                                    Long productId,
                                                                    String size) {
        return orderItemRepository.getOrderItemByProductAndSizeAndStatus(orderItemStatus, productId, size);
    }

    @Override
    public List<OrderItemDTO> getOrderItemByProductAndSizeAndStatusAndTable(EnumOrderItemStatus orderItemStatus,
                                                                            Long productId,
                                                                            String size,
                                                                            Long orderId) {
        return orderItemRepository.getOrderItemByProductAndSizeAndStatusAndTable(orderItemStatus, productId, size, orderId);
    }


    @Override
    public List<OrderItemResponseDTO> getOrderItemResponseDTOByOrderIdAndOrderItemStatus(Long orderId,
                                                                                         EnumOrderItemStatus enumOrderItemStatus) {
        return orderItemRepository.getOrderItemResponseDTOByOrderIdAndOrderItemStatus(orderId, enumOrderItemStatus);
    }

    @Override
    public List<OrderItemResponseDTO> getOrderItemResponseDTOByOrderId(Long orderId) {
        return orderItemRepository.getOrderItemResponseDTOByOrderId(orderId);
    }

    @Override
    public List<OrderItemKitchenDTO> getOrderItemByStatusGroupByProduct(EnumOrderItemStatus orderItemStatus) {
        return orderItemRepository.getOrderItemByStatusGroupByProduct(orderItemStatus);
    }

    @Override
    public List<OrderItemKitchenDTO> getOrderItemByStatusWithTable(EnumOrderItemStatus orderItemStatus) {
        return orderItemRepository.getOrderItemByStatusWithTable(orderItemStatus);
    }

    @Override
    public List<OrderItemKitchenDTO> getOrderItemByStatusWithTable2(EnumOrderItemStatus orderItemStatus) {
        return orderItemRepository.getOrderItemByStatusWithTable2(orderItemStatus);
    }



    @Override
    public List<OrderItemKitchenTableDTO> getOrderItemByStatusAndTable(EnumOrderItemStatus orderItemStatus,
                                                                       Long tableId) {
        return orderItemRepository.getOrderItemByStatusAndTable(orderItemStatus, tableId);
    }


    @Override
    public void changeStatusFromCookingToWaiterToProduct(Long productId, String size) {

        List<OrderItemDTO> orderItemList = orderItemRepository.getOrderItemByProductAndSizeAndStatus(EnumOrderItemStatus.COOKING, productId, size);

        OrderItemDTO orderItemDTO = orderItemList.get(0);
        OrderItem newOrderItem;
        if (orderItemDTO.getQuantity() == 1) {

            newOrderItem = orderItemDTO.toOrderItem();
            BigDecimal amount = orderItemDTO.getPrice().multiply(BigDecimal.valueOf(orderItemDTO.getQuantity()));
            orderItemRepository.updateAmount(amount, orderItemDTO.getId());

        } else {

            int quantity = orderItemDTO.getQuantity() - 1;

            orderItemRepository.updateQuantity(quantity, orderItemDTO.getId());

            BigDecimal amount = orderItemDTO.getPrice().multiply(BigDecimal.valueOf(quantity));

            orderItemRepository.updateAmount(amount, orderItemDTO.getId());


            Date timeCreated = orderItemDTO.getCreatedAt();
            newOrderItem = (OrderItem) new OrderItem()
                    .setId(null)
                    .setSize(orderItemDTO.getSize())
                    .setPrice(orderItemDTO.getPrice())
                    .setQuantity(1)
                    .setQuantityDelivery(0)
                    .setAmount(orderItemDTO.getPrice().multiply(new BigDecimal(1L)))
                    .setNote(orderItemDTO.getNote())
                    .setTableId(orderItemDTO.getTableId())
                    .setProduct(orderItemDTO.getProduct().toProduct())
                    .setOrder(orderItemDTO.getOrder().toOrder())
                    .setOrderItemStatus(EnumOrderItemStatus.WAITER)
                    .setCreatedAt(timeCreated);

        }
        checkExitsOrderItemWithStatus(newOrderItem, EnumOrderItemStatus.WAITER);
    }


    @Override
    public void changeStatusFromCookingToWaiterToTable(OrderItem orderItem) {

        OrderItem newOrderItem;
        if (orderItem.getQuantity() == 1) {

            newOrderItem = orderItem;
            orderItemRepository.delete(orderItem);

        } else {
            int quantity = orderItem.getQuantity() - 1;

            orderItemRepository.updateQuantity(quantity, orderItem.getId());

            BigDecimal amount = orderItem.getPrice().multiply(BigDecimal.valueOf(quantity));

            orderItemRepository.updateAmount(amount, orderItem.getId());

            newOrderItem = (OrderItem) new OrderItem()
                    .setId(null)
                    .setSize(orderItem.getSize())
                    .setPrice(orderItem.getPrice())
                    .setQuantity(1)
                    .setQuantityDelivery(0)
                    .setAmount(orderItem.getPrice().multiply(new BigDecimal(1L)))
                    .setNote(orderItem.getNote())
                    .setTableId(orderItem.getTableId())
                    .setProduct(orderItem.getProduct())
                    .setOrder(orderItem.getOrder())
                    .setOrderItemStatus(EnumOrderItemStatus.WAITER)
                    .setCreatedAt(orderItem.getCreatedAt());

        }
        checkExitsOrderItemWithStatus(newOrderItem, EnumOrderItemStatus.WAITER);
    }

    @Override
    public void changeStatusFromWaiterToDeliveryToTable(OrderItem orderItem) {

        OrderItem newOrderItem;
        if (orderItem.getQuantity() == 1) {

            newOrderItem = orderItem;
            orderItemRepository.delete(orderItem);

        } else {
            int quantity = orderItem.getQuantity() - 1;

            orderItemRepository.updateQuantity(quantity, orderItem.getId());

            BigDecimal amount = orderItem.getPrice().multiply(BigDecimal.valueOf(quantity));

            orderItemRepository.updateAmount(amount, orderItem.getId());

            newOrderItem = (OrderItem) new OrderItem()
                    .setId(null)
                    .setSize(orderItem.getSize())
                    .setPrice(orderItem.getPrice())
                    .setQuantity(1)
                    .setQuantityDelivery(0)
                    .setAmount(orderItem.getPrice().multiply(new BigDecimal(1L)))
                    .setNote(orderItem.getNote())
                    .setTableId(orderItem.getTableId())
                    .setProduct(orderItem.getProduct())
                    .setOrder(orderItem.getOrder())
                    .setOrderItemStatus(EnumOrderItemStatus.WAITER)
                    .setCreatedAt(orderItem.getCreatedAt());

        }
        checkExitsOrderItemWithStatus(newOrderItem, EnumOrderItemStatus.DELIVERY);
    }


    @Override
    public void checkExitsOrderItemWithStatus(OrderItem itemNew, EnumOrderItemStatus orderItemStatus) {

        itemNew.setOrderItemStatus(orderItemStatus);

        Long orderId = itemNew.getOrder().getId();

        Order order = orderService.findById(orderId).get();

        List<OrderItemDTO> orderItemListCurrent =
                orderItemRepository.getAllByOrderIdAndStatus(orderId, orderItemStatus);

        if (orderItemListCurrent.size() == 0) {

            Date createdAt = itemNew.getCreatedAt();
            itemNew = orderItemRepository.save(itemNew);
            orderItemRepository.updateCreatedAt(createdAt, itemNew.getId());

        } else {
            for (OrderItemDTO itemCurrent : orderItemListCurrent) {

                if (itemNew.getProduct().getId().equals(itemCurrent.getProduct().getId())
                        && itemNew.getSize().equals(itemCurrent.getSize())) {

                    itemNew.setAmount(itemNew.getPrice().multiply(BigDecimal.valueOf(itemNew.getQuantity())));

                    itemCurrent.setQuantity(itemCurrent.getQuantity() + itemNew.getQuantity());

                    itemCurrent.setAmount(itemCurrent.getAmount().add(itemNew.getAmount()));

                    if (itemNew.getId() != null) {
                        orderItemRepository.delete(itemNew);
                    }

                    orderItemRepository.save(itemCurrent.toOrderItem());

                    break;
                } else {
                    Date createdAt = itemNew.getCreatedAt();
                    itemNew = orderItemRepository.save(itemNew);
                    orderItemRepository.updateCreatedAt(createdAt, itemNew.getId());
                }
            }
        }

        order.setTotalAmount(orderService.calculateTotalAmount(orderId));
        orderService.save(order);

    }

    @Override
    public void checkExitsOrderItemWithWaiter(OrderItem itemNew) {

        itemNew.setOrderItemStatus(EnumOrderItemStatus.WAITER);

        Long orderId = itemNew.getOrder().getId();

        Order order = orderService.findById(orderId).get();

        List<OrderItemDTO> orderItemListCurrent =
                orderItemRepository.getAllByOrderIdAndStatus(orderId, EnumOrderItemStatus.WAITER);

        if (orderItemListCurrent.size() == 0) {

            Date createdAt = itemNew.getCreatedAt();
            itemNew = orderItemRepository.save(itemNew);
            orderItemRepository.updateCreatedAt(createdAt, itemNew.getId());

        } else {
            for (OrderItemDTO itemCurrent : orderItemListCurrent) {

                if (itemNew.getProduct().getId().equals(itemCurrent.getProduct().getId())
                        && itemNew.getSize().equals(itemCurrent.getSize())) {

                    itemCurrent.setQuantity(itemCurrent.getQuantity() + itemNew.getQuantity());

                    itemCurrent.setAmount(itemCurrent.getAmount().add(itemNew.getAmount()));

                    orderItemRepository.delete(itemNew);

                    orderItemRepository.save(itemCurrent.toOrderItem());

                    break;

                } else {
                    Date createdAt = itemNew.getCreatedAt();
                    itemNew = orderItemRepository.save(itemNew);
                    orderItemRepository.updateCreatedAt(createdAt, itemNew.getId());
                }
            }
        }

        order.setTotalAmount(orderService.calculateTotalAmount(orderId));
        orderService.save(order);

    }

    @Override
    public void checkExitsOrderItemWithDelivery(OrderItem itemNew) {


        Long orderId = itemNew.getOrder().getId();

        Order order = orderService.findById(orderId).get();

        List<OrderItemDTO> orderItemListCurrent =
                orderItemRepository.getAllByOrderIdAndStatus(orderId, EnumOrderItemStatus.DELIVERY);

        if (orderItemListCurrent.size() == 0) {
            itemNew.setOrderItemStatus(EnumOrderItemStatus.DELIVERY);
            Date createdAt = itemNew.getCreatedAt();
            itemNew = orderItemRepository.save(itemNew);
            orderItemRepository.updateCreatedAt(createdAt, itemNew.getId());

        } else {
            boolean check = false;
            for (OrderItemDTO itemCurrent : orderItemListCurrent) {

                if (itemNew.getProduct().getId().equals(itemCurrent.getProduct().getId())
                        && itemNew.getSize().equals(itemCurrent.getSize())) {

                    itemCurrent.setQuantity(itemCurrent.getQuantity() + itemNew.getQuantity());

                    itemCurrent.setAmount(itemCurrent.getAmount().add(itemNew.getAmount()));

                    orderItemRepository.delete(itemNew);

                    orderItemRepository.save(itemCurrent.toOrderItem());
                    check = true;
                    break;

                }
            }
            if (!check) {
                itemNew.setOrderItemStatus(EnumOrderItemStatus.DELIVERY);
                Date createdAt = itemNew.getCreatedAt();
                itemNew = orderItemRepository.save(itemNew);
                orderItemRepository.updateCreatedAt(createdAt, itemNew.getId());
            }

        }

        order.setTotalAmount(orderService.calculateTotalAmount(orderId));
        orderService.save(order);

    }

    @Override
    public void checkExitsOrderItemWithDone(OrderItem itemNew) {


        Long orderId = itemNew.getOrder().getId();

        Order order = orderService.findById(orderId).get();

        List<OrderItemDTO> orderItemListCurrent =
                orderItemRepository.getAllByOrderIdAndStatus(orderId, EnumOrderItemStatus.DONE);

        if (orderItemListCurrent.size() == 0) {
            itemNew.setOrderItemStatus(EnumOrderItemStatus.DONE);
            Date createdAt = itemNew.getCreatedAt();
            itemNew = orderItemRepository.save(itemNew);
            orderItemRepository.updateCreatedAt(createdAt, itemNew.getId());

        } else {
            boolean check = false;
            for (OrderItemDTO itemCurrent : orderItemListCurrent) {

                if (itemNew.getProduct().getId().equals(itemCurrent.getProduct().getId())
                        && itemNew.getSize().equals(itemCurrent.getSize())) {

                    itemCurrent.setQuantity(itemCurrent.getQuantity() + itemNew.getQuantity());

                    itemCurrent.setAmount(itemCurrent.getAmount().add(itemNew.getAmount()));

                    orderItemRepository.delete(itemNew);

                    orderItemRepository.save(itemCurrent.toOrderItem());
                    check = true;
                    break;

                }
            }
            if (!check) {
                itemNew.setOrderItemStatus(EnumOrderItemStatus.DONE);
                Date createdAt = itemNew.getCreatedAt();
                itemNew = orderItemRepository.save(itemNew);
                orderItemRepository.updateCreatedAt(createdAt, itemNew.getId());
            }

        }

        order.setTotalAmount(orderService.calculateTotalAmount(orderId));
        orderService.save(order);

    }


    @Override
    public OrderItem getById(Long id) {
        return null;
    }

    @Override
    public Optional<OrderItem> findById(Long id) {
        return orderItemRepository.findById(id);
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void remove(Long id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public void removeWithQuantity(OrderItem orderItem, int quantity) {

        if (quantity == orderItem.getQuantity()) {
            this.remove(orderItem.getId());
        } else {
            int newQuantity = orderItem.getQuantity() - quantity;
            this.updateQuantity(newQuantity, orderItem.getId());
        }
    }

    @Override
    public void softDelete(Long id) {
        orderItemRepository.softDelete(id);
    }

    @Override
    public void updateStatus(EnumOrderItemStatus orderItemStatus, Long orderItemId) {
        orderItemRepository.updateStatus(orderItemStatus, orderItemId);
    }

    @Override
    public void updateQuantity(int quantity, Long orderItemId) {
        orderItemRepository.updateQuantity(quantity, orderItemId);
    }

    @Override
    public List<ReportProductDTO> getTop5Product(){
        Pageable pageable = PageRequest.of(0, 5);
        return orderItemRepository.getTop5Product(pageable);
    }
}
