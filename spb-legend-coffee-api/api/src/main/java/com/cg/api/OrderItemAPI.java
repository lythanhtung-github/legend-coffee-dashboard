package com.cg.api;

import com.cg.domain.dto.order.OrderKitChenResponseDTO;
import com.cg.domain.dto.order.OrderKitchenDTO;
import com.cg.domain.dto.orderItem.OrderItemDTO;
import com.cg.domain.dto.orderItem.OrderItemKitchenDTO;
import com.cg.domain.dto.orderItem.OrderItemResponseDTO;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.OrderItem;
import com.cg.domain.enums.EnumOrderItemStatus;
import com.cg.domain.enums.EnumOrderStatus;
import com.cg.exception.DataInputException;
import com.cg.service.order.IOrderService;
import com.cg.service.orderItem.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemAPI {

    @Autowired
    private IOrderItemService orderItemService;


    @Autowired
    private IOrderService orderService;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getByOrderId(@RequestParam String orderId) {

        long oid;

        try {

            oid = Long.parseLong(orderId);

            Optional<Order> orderOptional = orderService.findById(oid);

            if (!orderOptional.isPresent()) throw new DataInputException("Hóa đơn không tồn tại.");


        } catch (NumberFormatException e) {
            throw new DataInputException("ID hóa đơn không hợp lệ.");
        }

        List<OrderItemDTO> orderItems = orderItemService.getOrderItemDTOByOrderId(oid);

        if (orderItems.size() == 0) {

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }


    @GetMapping("/cashier")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getOrderItemResponseDTOByOrderId(@RequestParam String orderId) {

        long oid;

        try {

            oid = Long.parseLong(orderId);

            Optional<Order> orderOptional = orderService.findById(oid);

            if (!orderOptional.isPresent()) throw new DataInputException("Hóa đơn không tồn tại.");

            if (orderOptional.get().getOrderStatus() == EnumOrderStatus.PAID)
                throw new DataInputException("Hóa đơn đã thanh toán.");


        } catch (NumberFormatException e) {

            throw new DataInputException("ID hóa đơn không hợp lệ.");

        }

        List<OrderItemResponseDTO> orderItems = orderItemService.getOrderItemResponseDTOByOrderId(oid);

        if (orderItems.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }


    @GetMapping("/kitchen/get-by-status-cooking")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getByStatusCooking() {

        List<OrderItemKitchenDTO> orderItemList = orderItemService.getOrderItemByStatusGroupByProduct(EnumOrderItemStatus.COOKING);

        if (orderItemList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderItemList, HttpStatus.OK);
    }


    @GetMapping("/kitchen/get-by-status-waiter")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getByStatusWaiter() {

        List<OrderItemKitchenDTO> orderItemList = orderItemService.getOrderItemByStatusWithTable2(EnumOrderItemStatus.WAITER);

        if (orderItemList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderItemList, HttpStatus.OK);
    }


    //lấy ra danh sách orderitem có trạng thái cooking gộp theo bàn
    @GetMapping("/kitchen/get-by-status-cooking-group-table")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getByStatusCookingGroupTable() {

        List<OrderKitchenDTO> orderList = orderService.getAllOrderKitchenByTable(EnumOrderItemStatus.COOKING);

        if (orderList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderList, HttpStatus.OK);

    }


    // Chuyển trạng thái toàn bộ của 1 sản phẩm từ cooking sang waiter
    @PostMapping("/kitchen/change-status-cooking-to-waiter-to-product-all")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromCookingToWaiterToProductAll(String productId, String size) {

        long pid;
        try {
            pid = Long.parseLong(productId);
        } catch (NumberFormatException e) {
            throw new DataInputException("ID sản phẩm không hợp lệ.");
        }

        List<OrderItemDTO> orderItemList = orderItemService.getOrderItemByProductAndSizeAndStatus(EnumOrderItemStatus.COOKING, pid, size.toUpperCase().trim());

        if (orderItemList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<Long> orderIdListChangeCurrent = new ArrayList<>();

        for (OrderItemDTO orderItemDTO : orderItemList) {
            orderItemService.checkExitsOrderItemWithWaiter(orderItemDTO.toOrderItem());

            orderIdListChangeCurrent.add(orderItemDTO.getOrder().getId());

        }

        Set<Long> set = new HashSet<Long>(orderIdListChangeCurrent);

        List<Long> orderIdListChange = new ArrayList<>(set);

        List<OrderItemKitchenDTO> orderItemResponseList = orderItemService.getOrderItemByStatusWithTable(EnumOrderItemStatus.WAITER);

        OrderKitChenResponseDTO orderKitChenResponseDTO = new OrderKitChenResponseDTO(orderIdListChange, orderItemResponseList);

        return new ResponseEntity<>(orderKitChenResponseDTO, HttpStatus.OK);
    }


    // Chuyển trạng thái toàn bộ của sản phẩm trong bàn từ cooking sang waiter
    @PostMapping("/kitchen/change-status-cooking-to-waiter-to-table-all")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromCookingToWaiterToTableAll(String orderId) {

        long oid;

        try {

            oid = Long.parseLong(orderId);

            Optional<Order> orderOptional = orderService.findById(oid);

            if (!orderOptional.isPresent())
                throw new DataInputException("Hóa đơn không tồn tại.");

            if (orderOptional.get().getOrderStatus() == EnumOrderStatus.PAID)
                throw new DataInputException("Hóa đơn đã thanh toán.");

        } catch (NumberFormatException e) {

            throw new DataInputException("ID hóa đơn không hợp lệ.");

        }

        List<OrderItemDTO> orderItemList = orderItemService.getOrderItemDTOByOrderIdAndStatus(oid, EnumOrderItemStatus.COOKING);

        if (orderItemList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        for (OrderItemDTO orderItemDTO : orderItemList) {
            orderItemService.checkExitsOrderItemWithWaiter(orderItemDTO.toOrderItem());
        }

        List<OrderItemKitchenDTO> orderItemResponseList = orderItemService.getOrderItemByStatusWithTable(EnumOrderItemStatus.WAITER);

        return new ResponseEntity<>(orderItemResponseList, HttpStatus.OK);
    }


    // Chuyển trạng thái toàn bộ của 1 sản phẩm trong bàn từ cooking sang waiter
    @PostMapping("/kitchen/change-status-cooking-to-waiter-to-product-of-table")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromCookingToWaiterToProductOfOrder(String orderId,
                                                                             String productId,
                                                                             String size) {

        long pid;

        try {

            pid = Long.parseLong(productId);

        } catch (NumberFormatException e) {

            throw new DataInputException("ID sản phẩm không hợp lệ.");
        }

        long oid;

        try {

            oid = Long.parseLong(orderId);

            Optional<Order> orderOptional = orderService.findById(oid);

            if (!orderOptional.isPresent()) throw new DataInputException("Hóa đơn không tồn tại.");

            if (orderOptional.get().getOrderStatus() == EnumOrderStatus.PAID)
                throw new DataInputException("Hóa đơn đã thanh toán.");

        } catch (NumberFormatException e) {
            throw new DataInputException("ID hóa đơn không hợp lệ.");
        }

        List<OrderItemDTO> orderItemList = orderItemService.getOrderItemByProductAndSizeAndStatusAndTable(EnumOrderItemStatus.COOKING,
                pid,
                size.toUpperCase().trim(),
                oid);

        if (orderItemList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        for (OrderItemDTO orderItemDTO : orderItemList) {
            orderItemService.checkExitsOrderItemWithWaiter(orderItemDTO.toOrderItem());

        }

        List<OrderItemKitchenDTO> orderItemResponseList = orderItemService.getOrderItemByStatusWithTable(EnumOrderItemStatus.WAITER);

        return new ResponseEntity<>(orderItemResponseList, HttpStatus.OK);
    }


    //Chuyển trạng thái cooking sang waiter 1 sản phẩm
    @PostMapping("/kitchen/change-status-cooking-to-waiter-to-product")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromCookingToWaiterToProduct(String productId, String size) {

        long pid;

        try {

            pid = Long.parseLong(productId);

        } catch (NumberFormatException e) {

            throw new DataInputException("ID sản phẩm không hợp lệ.");
        }

        List<OrderItemDTO> orderItemList =
                orderItemService.getOrderItemByProductAndSizeAndStatus(EnumOrderItemStatus.COOKING,
                        pid,
                        size.toUpperCase().trim());

        if (orderItemList.size() == 0) {

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }

        List<Long> orderIdListChange = new ArrayList<>();

        orderIdListChange.add(orderItemList.get(0).getOrder().getId());

        orderItemService.changeStatusFromCookingToWaiterToProduct(pid, size);

        List<OrderItemKitchenDTO> orderItemResponseList = orderItemService.getOrderItemByStatusWithTable(EnumOrderItemStatus.WAITER);

        OrderKitChenResponseDTO orderKitChenResponseDTO = new OrderKitChenResponseDTO(orderIdListChange, orderItemResponseList);

        return new ResponseEntity<>(orderKitChenResponseDTO, HttpStatus.OK);
    }


    @PostMapping("/kitchen/change-status-cooking-to-waiter-to-table")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromCookingToWaiterToTable(String orderItemId) {

        long oiId;

        try {

            oiId = Long.parseLong(orderItemId);

            Optional<OrderItem> orderItemOptional = orderItemService.findById(oiId);

            if (!orderItemOptional.isPresent()) throw new DataInputException("Id chi tiết hóa đơn không tồn tại.");

            OrderItem orderItem = orderItemOptional.get();

            if (orderItem.getOrderItemStatus() == EnumOrderItemStatus.WAITER) {
                throw new DataInputException("Sản phẩm đang chờ cung ứng.");
            }

            orderItemService.changeStatusFromCookingToWaiterToTable(orderItem);


        } catch (NumberFormatException e) {

            throw new DataInputException("ID chi tiết hóa đơn không hợp lệ.");

        }

        List<OrderItemKitchenDTO> orderItemResponseList = orderItemService.getOrderItemByStatusWithTable(EnumOrderItemStatus.WAITER);

        return new ResponseEntity<>(orderItemResponseList, HttpStatus.OK);
    }

    //Chuyển từ waiter sang delivery từng orderitem
    @PostMapping("/kitchen/change-status-waiter-to-delivery-to-table")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromWaiterToDeliveryToTable(String orderItemId) {

        long oiId;

        try {

            oiId = Long.parseLong(orderItemId);

            Optional<OrderItem> orderItemOptional = orderItemService.findById(oiId);

            if (!orderItemOptional.isPresent()) throw new DataInputException("Id chi tiết hóa đơn không tồn tại.");

            OrderItem orderItem = orderItemOptional.get();

            if (orderItem.getOrderItemStatus() == EnumOrderItemStatus.DELIVERY) {
                throw new DataInputException("Sản phẩm đang được giao.");
            }

            orderItemService.changeStatusFromWaiterToDeliveryToTable(orderItem);


        } catch (NumberFormatException e) {

            throw new DataInputException("ID chi tiết hóa đơn không hợp lệ.");

        }

        List<OrderItemKitchenDTO> orderItemResponseList = orderItemService.getOrderItemByStatusWithTable(EnumOrderItemStatus.WAITER);

        return new ResponseEntity<>(orderItemResponseList, HttpStatus.OK);
    }

    // Chuyển trạng thái toàn bộ của 1 sản phẩm trong bàn từ waiter sang delivery
    @PostMapping("/kitchen/change-status-waiter-to-delivery-to-product-of-table")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> changeStatusFromWaiterToDeliveryToProductOfOrder(String orderItemId) {

        long oiId;

        try {

            oiId = Long.parseLong(orderItemId);

            Optional<OrderItem> orderItemOptional = orderItemService.findById(oiId);

            if (!orderItemOptional.isPresent()) throw new DataInputException("Id chi tiết hóa đơn không tồn tại.");

            OrderItem orderItem = orderItemOptional.get();

            if (orderItem.getOrderItemStatus() == EnumOrderItemStatus.DELIVERY) {
                throw new DataInputException("Sản phẩm đã làm xong đang chờ phục vụ.");
            }

            if (orderItem.getOrderItemStatus() != EnumOrderItemStatus.WAITER) {
                throw new DataInputException("Sản phẩm chưa làm không thể phục vụ.");
            }


            orderItemService.checkExitsOrderItemWithDelivery(orderItem);


        } catch (NumberFormatException e) {

            throw new DataInputException("ID chi tiết hóa đơn không hợp lệ.");

        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    //Xóa orderitem theo số lượng
    @DeleteMapping("/delete/{orderItemId}/quantity/{quantity}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> deleteWithQuantity(@PathVariable Long orderItemId, @PathVariable int quantity) {

        Optional<OrderItem> orderItemOptional = orderItemService.findById(orderItemId);

        if (!orderItemOptional.isPresent()) {
            throw new DataInputException("ID chi tiết hóa đơn không hợp lệ.");
        }

        OrderItem orderItem = orderItemOptional.get();
        if (orderItem.getOrderItemStatus() != EnumOrderItemStatus.COOKING) {
            throw new DataInputException("Đơn hàng đang chờ cung ứng hoặc chờ giao không thể xóa!");
        }

        if (quantity < 0) {
            throw new DataInputException("Số lượng âm không thể xóa.");
        }

        if (orderItem.getQuantity() < quantity) {
            throw new DataInputException("Số lượng sản phẩm muốn xóa lớn hơn số lượng hiện có.");
        }

        try {
            orderItemService.removeWithQuantity(orderItem, quantity);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } catch (Exception e) {

            throw new DataInputException("Hệ thống đang có lỗi! Vui lòng thử lại sau!");
        }
    }

}
