package com.cg.repository;

import com.cg.domain.dto.orderItem.OrderItemDTO;
import com.cg.domain.dto.orderItem.OrderItemKitchenDTO;
import com.cg.domain.dto.orderItem.OrderItemKitchenTableDTO;
import com.cg.domain.dto.orderItem.OrderItemResponseDTO;
import com.cg.domain.dto.report.ReportProductDTO;
import com.cg.domain.entity.OrderItem;
import com.cg.domain.enums.EnumOrderItemStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {


    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemDTO (" +
                "oi.id, " +
                "oi.size, " +
                "oi.price, " +
                "oi.quantity, " +
                "oi.quantityDelivery, " +
                "oi.amount, " +
                "oi.note, " +
                "oi.tableId, " +
                "oi.product, " +
                "oi.order, " +
                "oi.orderItemStatus " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.order.id = :orderId " +
            "AND oi.orderItemStatus = :orderItemStatus "
    )
    List<OrderItemDTO> getAllByOrderIdAndStatus(@Param("orderId") Long orderId,
                                             @Param("orderItemStatus") EnumOrderItemStatus orderItemStatus);

    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemDTO (" +
                "oi.id, " +
                "oi.size, " +
                "oi.price, " +
                "oi.quantity, " +
                "oi.quantityDelivery, " +
                "oi.amount, " +
                "oi.note, " +
                "oi.tableId, " +
                "oi.product, " +
                "oi.order, " +
                "oi.orderItemStatus, " +
                "oi.createdAt " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.order.id = :orderId "
    )
    List<OrderItemDTO> getOrderItemDTOByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemDTO (" +
                "oi.id, " +
                "oi.size, " +
                "oi.price, " +
                "oi.quantity, " +
                "oi.quantityDelivery, " +
                "oi.amount, " +
                "oi.note, " +
                "oi.tableId, " +
                "oi.product, " +
                "oi.order, " +
                "oi.orderItemStatus, " +
                "oi.createdAt " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.order.id = :orderId " +
            "AND oi.orderItemStatus = :orderItemStatus"
    )
    List<OrderItemDTO> getOrderItemDTOByOrderIdAndStatus(@Param("orderId") Long orderId,
                                                         @Param("orderItemStatus") EnumOrderItemStatus orderItemStatus);

    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemDTO (" +
                "oi.id, " +
                "oi.size, " +
                "oi.price, " +
                "oi.quantity, " +
                "oi.quantityDelivery, " +
                "oi.amount, " +
                "oi.note, " +
                "oi.tableId, " +
                "oi.product, " +
                "oi.order, " +
                "oi.orderItemStatus, " +
                "oi.createdAt " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.orderItemStatus = :orderItemStatus " +
            "AND oi.product.id = :productId " +
            "AND oi.size = :size " +
            "ORDER BY oi.createdAt"
    )
    List<OrderItemDTO> getOrderItemByProductAndSizeAndStatus(@Param("orderItemStatus") EnumOrderItemStatus orderItemStatus,
                                                             @Param("productId") Long productId,
                                                             @Param("size") String size);

    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemDTO (" +
                "oi.id, " +
                "oi.size, " +
                "oi.price, " +
                "oi.quantity, " +
                "oi.quantityDelivery, " +
                "oi.amount, " +
                "oi.note, " +
                "oi.tableId, " +
                "oi.product, " +
                "oi.order, " +
                "oi.orderItemStatus, " +
                "oi.createdAt " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.orderItemStatus = :orderItemStatus " +
            "AND oi.product.id = :productId " +
            "AND oi.size = :size " +
            "AND oi.order.id = :orderId " +
            "ORDER BY oi.createdAt"
    )
    List<OrderItemDTO> getOrderItemByProductAndSizeAndStatusAndTable(@Param("orderItemStatus") EnumOrderItemStatus orderItemStatus,
                                                                     @Param("productId") Long productId,
                                                                     @Param("size") String size,
                                                                     @Param("orderId") Long orderId
    );

    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemResponseDTO (" +
                "oi.id, " +
                "oi.size, " +
                "oi.price, " +
                "oi.quantity, " +
                "oi.quantityDelivery, " +
                "oi.amount, " +
                "oi.note, " +
                "oi.tableId, " +
                "oi.product, " +
                "oi.order, " +
                "oi.orderItemStatus " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.order.id = :orderId " +
            "AND oi.orderItemStatus = :orderItemStatus "
    )
    List<OrderItemResponseDTO> getOrderItemResponseDTOByOrderIdAndOrderItemStatus(@Param("orderId") Long orderId,
                                                                                  @Param("orderItemStatus") EnumOrderItemStatus orderItemStatus);


    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemResponseDTO (" +
                "oi.id, " +
                "oi.size, " +
                "oi.price, " +
                "oi.quantity, " +
                "oi.quantityDelivery, " +
                "oi.amount, " +
                "oi.note, " +
                "oi.tableId, " +
                "oi.product, " +
                "oi.order, " +
                "oi.orderItemStatus " +
            ") " +
            "FROM OrderItem AS oi " +
            "WHERE oi.deleted = false " +
            "AND oi.order.id = :orderId "
    )
    List<OrderItemResponseDTO> getOrderItemResponseDTOByOrderId(@Param("orderId") Long orderId);


    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemKitchenDTO (" +
                "pd.id, " +
                "pd.title, " +
                "oi.size, " +
                "SUM(oi.quantity), " +
                "SUM(oi.quantityDelivery) " +
            ") " +
            "FROM OrderItem AS oi " +
            "JOIN Product AS pd " +
            "ON oi.product.id = pd.id " +
            "WHERE oi.orderItemStatus = :orderItemStatus " +
            "GROUP BY oi.product.id, oi.size ")
    List<OrderItemKitchenDTO> getOrderItemByStatusGroupByProduct(@Param("orderItemStatus") EnumOrderItemStatus orderItemStatus);


    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemKitchenDTO (" +
                "oi.id, " +
                "oi.product.id, " +
                "pd.title, " +
                "tb.name, " +
                "oi.size, " +
                "oi.quantity, " +
                "oi.quantityDelivery, " +
                "oi.createdAt, " +
                "oi.updatedAt " +
            ") " +
            "FROM OrderItem AS oi " +
            "JOIN Product AS pd " +
            "ON oi.product.id = pd.id " +
            "JOIN CTable AS tb " +
            "ON oi.tableId = tb.id " +
            "WHERE oi.orderItemStatus = :orderItemStatus")
    List<OrderItemKitchenDTO> getOrderItemByStatusWithTable(@Param("orderItemStatus") EnumOrderItemStatus orderItemStatus);

    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemKitchenDTO (" +
                "oi.id, " +
                "oi.product.id, " +
                "pd.title, " +
                "tb.name, " +
                "oi.size, " +
                "oi.quantity, " +
                "oi.quantityDelivery, " +
                "oi.createdAt, " +
                "oi.updatedAt, " +
                "oi.order.id, " +
                "oi.tableId " +
            ") " +
            "FROM OrderItem AS oi " +
            "JOIN Product AS pd " +
            "ON oi.product.id = pd.id " +
            "JOIN CTable AS tb " +
            "ON oi.tableId = tb.id " +
            "WHERE oi.orderItemStatus = :orderItemStatus")
    List<OrderItemKitchenDTO> getOrderItemByStatusWithTable2(@Param("orderItemStatus") EnumOrderItemStatus orderItemStatus);


    @Query("SELECT NEW com.cg.domain.dto.orderItem.OrderItemKitchenTableDTO (" +
                "oi.id, " +
                "tb.name, " +
                "pd.id, " +
                "pd.title, " +
                "oi.size, " +
                "oi.quantity, " +
                "oi.quantityDelivery, " +
                "oi.createdAt " +
            ") " +
            "FROM OrderItem AS oi " +
            "JOIN Product AS pd " +
            "ON oi.product.id = pd.id " +
            "JOIN CTable As tb " +
            "ON oi.tableId = tb.id " +
            "WHERE oi.orderItemStatus = :orderItemStatus " +
            "AND oi.tableId = :tableId " +
            "ORDER BY oi.createdAt ASC ")
    List<OrderItemKitchenTableDTO> getOrderItemByStatusAndTable(@Param("orderItemStatus") EnumOrderItemStatus orderItemStatus, @Param("tableId") Long tableId);

    @Modifying
    @Query("UPDATE OrderItem AS oi " +
            "SET oi.orderItemStatus = :orderItemStatus " +
            "WHERE oi.id = :orderItemId")
    void updateStatus(@Param("orderItemStatus") EnumOrderItemStatus orderItemStatus,
                      @Param("orderItemId") Long orderItemId);

    @Modifying
    @Query("UPDATE OrderItem AS oi " +
            "SET oi.quantity = :quantity " +
            "WHERE oi.id = :orderItemId")
    void updateQuantity(@Param("quantity") int quantity,
                        @Param("orderItemId") Long orderItemId);

    @Modifying
    @Query("UPDATE OrderItem AS oi " +
            "SET oi.createdAt = :createdAt " +
            "WHERE oi.id = :orderItemId")
    void updateCreatedAt(@Param("createdAt") Date createdAt,
                        @Param("orderItemId") Long orderItemId);

    @Modifying
    @Query("UPDATE OrderItem AS oi " +
            "SET oi.amount = :amount " +
            "WHERE oi.id = :orderItemId")
    void updateAmount(@Param("amount") BigDecimal amount,
                        @Param("orderItemId") Long orderItemId);

    @Modifying
    @Query("UPDATE OrderItem AS oi " +
            "SET oi.deleted = true " +
            "WHERE oi.id = :orderItemId")
    void softDelete(@Param("orderItemId") Long orderItemId);


    @Query("SELECT NEW com.cg.domain.dto.report.ReportProductDTO (" +
            "pd.title, " +
            "pd.productImage.fileFolder, " +
            "pd.productImage.fileName, " +
            "oi.size, " +
            "SUM(oi.quantity), " +
            "SUM(oi.amount) " +
            ") " +
            "FROM OrderItem AS oi " +
            "JOIN Product AS pd " +
            "ON oi.product.id = pd.id " +
            "GROUP BY oi.product.id, oi.size " +
            "ORDER BY SUM(oi.quantity) DESC"
    )
    List<ReportProductDTO> getTop5Product(Pageable pageable);


}
