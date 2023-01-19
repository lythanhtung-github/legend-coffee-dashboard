package com.cg.repository;

import com.cg.domain.dto.order.OrderCountCurrentMonthDTO;
import com.cg.domain.dto.order.OrderDTO;
import com.cg.domain.dto.report.ReportDayToDayDTO;
import com.cg.domain.dto.report.ReportDTO;
import com.cg.domain.dto.report.ReportYearDTO;
import com.cg.domain.entity.Order;
import com.cg.domain.enums.EnumOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT NEW com.cg.domain.dto.order.OrderDTO (" +
            "od.id, " +
            "od.totalAmount, " +
            "od.orderStatus, " +
            "od.table, " +
            "od.staff, " +
            "od.createdAt " +
            ") " +
            "FROM Order AS od " +
            "WHERE od.deleted = false "
    )
    List<OrderDTO> getAllOrderDTOWhereDeletedIsFalse();

    @Query("SELECT NEW com.cg.domain.dto.order.OrderDTO (" +
            "od.id, " +
            "od.totalAmount, " +
            "od.orderStatus, " +
            "od.table, " +
            "od.staff, " +
            "od.createdAt " +
            ") " +
            "FROM Order AS od " +
            "WHERE od.deleted = false " +
            "AND DATE_FORMAT(od.createdAt,'%Y-%m-%d') > :startDay " +
            "AND DATE_FORMAT(od.createdAt,'%Y-%m-%d') < :endDay "
    )
    List<OrderDTO> getAllOrderDTOByDayToDay(@Param("startDay") String startDay, @Param("endDay") String endDay);


    @Query("SELECT NEW com.cg.domain.dto.order.OrderDTO (" +
            "od.id, " +
            "od.totalAmount, " +
            "od.orderStatus, " +
            "od.table, " +
            "od.staff, " +
            "od.createdAt " +
            ") " +
            "FROM Order AS od " +
            "WHERE od.deleted = false " +
            "AND od.orderStatus = :orderStatus"
    )
    List<OrderDTO> getOrderDTOByStatus(@Param("orderStatus") EnumOrderStatus orderStatus);

    @Query("SELECT NEW com.cg.domain.dto.order.OrderDTO (" +
            "od.id, " +
            "od.totalAmount, " +
            "od.orderStatus, " +
            "od.table, " +
            "od.staff " +
            ") " +
            "FROM Order AS od " +
            "WHERE od.deleted = false " +
            "AND od.table.id = :tableId " +
            "AND od.orderStatus = :orderStatus"
    )
    List<OrderDTO> getOrderDTOByTableIdAndOrderStatus(@Param("tableId") Long tableId, @Param("orderStatus") EnumOrderStatus orderStatus);


    @Query("SELECT NEW com.cg.domain.dto.report.ReportYearDTO (" +
            "MONTH(od.createdAt), " +
            "sum(od.totalAmount) " +
            ") " +
            "FROM Order AS od " +
            "WHERE MONTH(od.createdAt) = :month " +
            "AND YEAR(od.createdAt) = :year " +
            "GROUP BY MONTH(od.createdAt)"
    )
    List<ReportYearDTO> getReportByMonth(@Param("month") int month, @Param("year") int year);


    @Query("SELECT NEW com.cg.domain.dto.report.ReportYearDTO (" +
            "MONTH(od.createdAt), " +
            "sum(od.totalAmount) " +
            ") " +
            "FROM Order AS od " +
            "WHERE YEAR(od.createdAt) = :year " +
            "AND od.orderStatus = 'PAID' " +
            "GROUP BY MONTH(od.createdAt) " +
            "ORDER BY MONTH(od.createdAt) ASC"
    )
    List<ReportYearDTO> getReportByYear(@Param("year") int year);

    @Query("SELECT NEW com.cg.domain.dto.report.ReportDTO (" +
            "sum(od.totalAmount) " +
            ") " +
            "FROM Order AS od " +
            "WHERE DATE_FORMAT(od.createdAt,'%Y-%m-%d') = :day " +
            "AND od.orderStatus = 'PAID' "
    )
    List<ReportDTO> getReportOfDay(@Param("day") String day);

    @Query("SELECT NEW com.cg.domain.dto.report.ReportDayToDayDTO (" +
            "DATE_FORMAT(od.createdAt,'%d/%m/%Y'), " +
            "sum(od.totalAmount) " +
            ") " +
            "FROM Order AS od " +
            "WHERE DATE_FORMAT(od.createdAt,'%Y-%m-%d') > :startDay " +
            "AND DATE_FORMAT(od.createdAt,'%Y-%m-%d') < :endDay " +
            "AND od.orderStatus = 'PAID' " +
            "GROUP BY DATE_FORMAT(od.createdAt,'%d/%m/%Y') " +
            "ORDER BY DATE_FORMAT(od.createdAt,'%d/%m/%Y')"
    )
    List<ReportDayToDayDTO> getReportFromDayToDay(@Param("startDay") String startDay,
                                                  @Param("endDay") String endDay);

    @Query("SELECT NEW com.cg.domain.dto.order.OrderCountCurrentMonthDTO (" +
            "count(od.id) " +
            ") " +
            "FROM Order AS od " +
            "WHERE DATE(Date_Format(od.createdAt,'%Y/%m/%d')) =CURRENT_DATE() "
    )
    List<OrderCountCurrentMonthDTO> countOrderOfCurrentDay();


    @Query("SELECT NEW com.cg.domain.dto.report.ReportDTO (" +
            "sum(od.totalAmount) " +
            ") " +
            "FROM Order AS od " +
            "WHERE MONTH(Date_Format(od.createdAt,'%Y/%m/%d')) = MONTH(CURRENT_DATE()) " +
            "AND YEAR(Date_Format(od.createdAt,'%Y/%m/%d')) = YEAR(CURRENT_DATE()) " +
            "AND od.orderStatus = 'PAID'"
    )
    List<ReportDTO> getReportOfCurrentMonth();

    @Modifying
    @Query("UPDATE Order AS o SET o.deleted = true WHERE o.id = :orderId")
    void softDelete(@Param("orderId") Long orderId);

}
