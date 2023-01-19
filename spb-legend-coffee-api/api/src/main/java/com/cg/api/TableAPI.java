package com.cg.api;

import com.cg.domain.dto.order.OrderDTO;
import com.cg.domain.dto.table.TableCreateDTO;
import com.cg.domain.dto.table.TableDTO;
import com.cg.domain.dto.table.TableUpdateDTO;
import com.cg.domain.entity.CTable;
import com.cg.domain.enums.EnumOrderStatus;
import com.cg.domain.enums.EnumTableStatus;
import com.cg.exception.DataInputException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.databaseCheck.IDatabaseCheckService;
import com.cg.service.order.IOrderService;
import com.cg.service.table.ITableService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tables")
public class TableAPI {

    @Autowired
    private ITableService tableService;

    @Autowired
    private IDatabaseCheckService databaseCheckService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    public ResponseEntity<?> getAllByDeletedIsFalse() {

        List<TableDTO> tableDTOS = tableService.getAllTableWhereDeletedIsFalse();

        if (tableDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tableDTOS, HttpStatus.OK);
    }

    @GetMapping("/status={status}")
    public ResponseEntity<?> getTablesByStatus(@PathVariable String status) {

        List<TableDTO> tableDTOS = tableService.getTablesWhereStatus(EnumTableStatus.valueOf(status));

        if (tableDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tableDTOS, HttpStatus.OK);
    }

    @GetMapping("{tableId}")
    public ResponseEntity<?> getTableById(@PathVariable Long tableId) {

        Optional<CTable> tableOptional = tableService.findById(tableId);

        if (!tableOptional.isPresent()) {
            throw new ResourceNotFoundException("Bàn không tồn tại!");
        }

        CTable table = tableOptional.get();

        return new ResponseEntity<>(table.toTableDTO(), HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> createTable(@Valid @RequestBody TableCreateDTO tableCreateDTO, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (tableService.existByName(tableCreateDTO.getName())) {
            throw new DataInputException("Bàn đã tồn tại trong hệ thống.");
        }

        CTable table = new CTable();
        table.setId(null)
                .setName(tableCreateDTO.getName())
                .setStatus(EnumTableStatus.EMPTY);

        table = tableService.save(table);

        databaseCheckService.updateWithTableCheck();

        return new ResponseEntity<>(table.toTableDTO(), HttpStatus.OK);
    }

    @PatchMapping("/{tableId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> updateTable(@PathVariable Long tableId, @Validated @RequestBody TableUpdateDTO tableUpdateDTO, BindingResult bindingResult) {

        Optional<CTable> tableOptional = tableService.findById(tableId);

        if (!tableOptional.isPresent()) {
            throw new DataInputException("ID bàn không tồn tại.");
        }

        CTable table = tableOptional.get();

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (tableService.existByNameAndIdIsNot(tableUpdateDTO.getName(), tableUpdateDTO.getId())) {
            throw new DataInputException("Bàn đã tồn tại trong hệ thống.");
        }

        String name = tableUpdateDTO.getName();

        EnumTableStatus oldStatus = table.getStatus();
        EnumTableStatus newStatus = EnumTableStatus.valueOf(tableUpdateDTO.getStatus());

        if (oldStatus == newStatus) {
            throw new DataInputException("Bàn đang " + oldStatus.getValue() + ", không thể chỉnh sửa trạng thái bàn.");
        }

        if (oldStatus == EnumTableStatus.OPEN) {
            List<OrderDTO> orderDTOList = orderService.getOrderDTOByTableIdAndOrderStatus(tableId, EnumOrderStatus.UNPAID);
            if (orderDTOList.size() != 0) {
                throw new DataInputException("Bàn chưa thanh toán, không thể đóng bàn.");
            }
        }

        table.setName(name)
                .setStatus(newStatus);
        table = tableService.save(table);

        return new ResponseEntity<>(table.toTableDTO(), HttpStatus.OK);
    }

    @PatchMapping("/update/{tableId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> updateTableNoStatus(@PathVariable Long tableId, @Validated @RequestBody TableUpdateDTO tableUpdateDTO, BindingResult bindingResult) {

        Optional<CTable> tableOptional = tableService.findById(tableId);

        if (!tableOptional.isPresent()) {
            throw new DataInputException("ID bàn không tồn tại.");
        }

        CTable table = tableOptional.get();

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (tableService.existByNameAndIdIsNot(tableUpdateDTO.getName(), tableUpdateDTO.getId())) {
            throw new DataInputException("Bàn đã tồn tại trong hệ thống.");
        }

        String name = tableUpdateDTO.getName();

        table.setName(name);
        table = tableService.save(table);

        databaseCheckService.updateWithTableCheck();

        return new ResponseEntity<>(table.toTableDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{tableId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> deleteTable(@PathVariable Long tableId) {

        Optional<CTable> productOptional = tableService.findById(tableId);

        if (!productOptional.isPresent()) {
            throw new DataInputException("ID bàn không hợp lệ.");
        }

        try {
            tableService.softDelete(tableId);

            databaseCheckService.updateWithTableCheck();

            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } catch (PersistenceException e) {
            e.printStackTrace();
            throw new DataInputException("Vui lòng liên hệ Administrator.");
        }
    }

}
