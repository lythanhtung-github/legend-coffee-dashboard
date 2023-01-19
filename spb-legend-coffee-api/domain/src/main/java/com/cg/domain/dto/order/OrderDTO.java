package com.cg.domain.dto.order;

import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.table.TableDTO;
import com.cg.domain.entity.CTable;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.Staff;
import com.cg.domain.enums.EnumOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDTO {

    private Long id;

    private BigDecimal totalAmount;

    private String orderStatus;

    private TableDTO table;

    private StaffDTO staff;

    private Date createdAt;


    public OrderDTO(Long id, BigDecimal totalAmount, EnumOrderStatus orderStatus, CTable table, Staff staff, Date createdAt) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.orderStatus = String.valueOf(orderStatus);
        this.table = table.toTableDTO();
        this.staff = staff.toStaffDTO();
        this.createdAt = createdAt;
    }

    public OrderDTO(Long id, BigDecimal totalAmount, EnumOrderStatus orderStatus, CTable table, Staff staff) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.orderStatus = String.valueOf(orderStatus);
        this.table = table.toTableDTO();
        this.staff = staff.toStaffDTO();
    }

    public Order toOrder(){

        return new Order()
                .setId(id)
                .setTotalAmount(totalAmount)
                .setOrderStatus(EnumOrderStatus.valueOf(orderStatus))
                .setTable(table.toTable())
                .setStaff(staff.toStaff());
    }
}
