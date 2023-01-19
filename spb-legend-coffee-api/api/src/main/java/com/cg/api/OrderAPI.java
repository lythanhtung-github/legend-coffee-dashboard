package com.cg.api;


import com.cg.domain.dto.order.*;
import com.cg.domain.entity.*;
import com.cg.domain.enums.EnumOrderStatus;
import com.cg.exception.DataInputException;
import com.cg.service.order.IOrderService;
import com.cg.service.staff.IStaffService;
import com.cg.service.table.ITableService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderAPI {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IStaffService staffService;

    @Autowired
    private ITableService tableService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    public ResponseEntity<?> getAllByDeletedIsFalse() {
            List<OrderDTO> orderDTOList = orderService.getAllOrderDTOWhereDeletedIsFalse();

        if (orderDTOList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderDTOList, HttpStatus.OK);
    }

    @GetMapping("/day/{startDay}/{endDay}")
    public ResponseEntity<?> getAllOrderDTOByFromDayToDay(@PathVariable String startDay, @PathVariable String endDay) {

        String[] startDayArray = startDay.split("-");
        String[] endDayArray = endDay.split("-");

        int startDayTemp = Integer.parseInt(startDayArray[startDayArray.length - 1]) - 1;
        if (startDayTemp < 10)
            startDayArray[startDayArray.length - 1] = "0" + startDayTemp;
        else
            startDayArray[startDayArray.length - 1] = String.valueOf(startDayTemp);

        startDay = String.join("-", startDayArray);

        int endDayTemp = Integer.parseInt(endDayArray[endDayArray.length - 1]) + 1;
        if (endDayTemp < 10)
            endDayArray[endDayArray.length - 1] = "0" + endDayTemp;
        else
            endDayArray[endDayArray.length - 1] = String.valueOf(endDayTemp);

        endDay = String.join("-", endDayArray);

        List<OrderDTO> orderDTOList = orderService.getAllOrderDTOByDayToDay(startDay, endDay);

        if (orderDTOList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderDTOList, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getById(@PathVariable String orderId) {
        long oid;
        try {
            oid = Long.parseLong(orderId);
        } catch (NumberFormatException e) {
            throw new DataInputException("ID hóa đơn không hợp lệ.");
        }

        Optional<Order> orderOptional = orderService.findById(oid);

        if (!orderOptional.isPresent()) {
            throw new DataInputException("ID hóa đơn không hợp lệ.");
        }

        return new ResponseEntity<>(orderOptional.get().toOrderDTO(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> createOrder(@Validated OrderCreateDTO orderCreateDTO, BindingResult bindingResult) {

        Long tableId = orderCreateDTO.getTableId();
        Long staffId = orderCreateDTO.getStaffId();

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<CTable> tableOptional = tableService.findById(tableId);

        if (!tableOptional.isPresent()) {
            throw new DataInputException("ID bàn không hợp lệ.");
        }

        Optional<Staff> staffOptional = staffService.findById(staffId);

        if (!staffOptional.isPresent()) {
            throw new DataInputException("ID nhân viên không hợp lệ.");
        }

        Order order = new Order();

        order.setId(null)
                .setTotalAmount(new BigDecimal(0L))
                .setTable(tableOptional.get())
                .setStaff(staffOptional.get());

        order = orderService.save(order);

        return new ResponseEntity<>(order.toOrderDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/create-with-order-item")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> createOrderWithOrderItem(@Validated @RequestBody OrderCreateWithOrderItemDTO orderCreateWithOrderItemDTO, BindingResult bindingResult) {


        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Long tableId = orderCreateWithOrderItemDTO.getTableId();
        Long staffId = orderCreateWithOrderItemDTO.getStaffId();

        Optional<CTable> tableOptional = tableService.findById(tableId);

        if (!tableOptional.isPresent()) {
            throw new DataInputException("ID bàn không hợp lệ.");
        }

        Optional<Staff> staffOptional = staffService.findById(staffId);

        if (!staffOptional.isPresent()) {
            throw new DataInputException("ID nhân viên không hợp lệ.");
        }


        try {
            OrderResponseDTO orderReturnDTO;
            if (orderCreateWithOrderItemDTO.getOrderId() != null) {

                orderReturnDTO = orderService.updateWithOrderItems(orderCreateWithOrderItemDTO);

            } else {

                orderReturnDTO = orderService.createWithOrderItems(orderCreateWithOrderItemDTO);

            }

            return new ResponseEntity<>(orderReturnDTO, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/delete/{orderId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> delete(@PathVariable Long orderId) {

        Optional<Order> orderOptional = orderService.findById(orderId);

        if (!orderOptional.isPresent()) {
            throw new DataInputException("ID hóa đơn không hợp lệ.");
        }

        orderService.deleteOrderById(orderId);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/pay/{orderId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> pay(@PathVariable Long orderId) {

        Optional<Order> orderOptional = orderService.findById(orderId);

        if (!orderOptional.isPresent()) {
            throw new DataInputException("ID hóa đơn không hợp lệ.");
        }

        Order order = orderOptional.get();

        if (order.getOrderStatus() == EnumOrderStatus.PAID) {
            throw new DataInputException("Hóa đơn đã thanh toán.");
        }

        OrderPayDTO orderPayDTO = orderService.pay(order);

        return new ResponseEntity<>(orderPayDTO, HttpStatus.OK);
    }


    @GetMapping("/count-order-current-day")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> countOrderOfCurrentDay() {
        return new ResponseEntity<>(orderService.countOrderOfCurrentDay().get(0).getCount(), HttpStatus.OK);
    }
}
