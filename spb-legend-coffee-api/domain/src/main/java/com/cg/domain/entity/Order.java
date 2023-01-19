package com.cg.domain.entity;

import com.cg.domain.dto.order.OrderDTO;
import com.cg.domain.enums.EnumOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_amount", precision = 12, scale = 0, nullable = false)
    private BigDecimal totalAmount;

    @Column(name="status_order", length = 50,nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumOrderStatus orderStatus;

    @OneToOne
    @JoinColumn(name = "table_id", nullable = false)
    private CTable table;

    @OneToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    public OrderDTO toOrderDTO(){
        return new OrderDTO()
                .setId(id)
                .setTotalAmount(totalAmount)
                .setOrderStatus(String.valueOf(orderStatus))
                .setTable(table.toTableDTO())
                .setStaff(staff.toStaffDTO())
                .setCreatedAt(getCreatedAt());
    }

}
