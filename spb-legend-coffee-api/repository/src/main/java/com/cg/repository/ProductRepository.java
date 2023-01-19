package com.cg.repository;

import com.cg.domain.dto.product.ProductCashierDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT NEW com.cg.domain.dto.product.ProductDTO (" +
                "pd.id, " +
                "pd.title, " +
                "pd.description, " +
                "pd.summary, " +
                "pd.sizes, " +
                "pd.category, " +
                "pd.productImage " +
            ") " +
            "FROM Product AS pd " +
            "WHERE pd.deleted = false "
    )
    List<ProductDTO> getAllProductDTOWhereDeletedIsFalse();

    @Query("SELECT NEW com.cg.domain.dto.product.ProductCashierDTO (" +
                "pd.id, " +
                "pd.title, " +
                "pd.description, " +
                "pd.sizes, " +
                "pd.category.id, " +
                "pd.productImage.fileUrl " +
            ") " +
            "FROM Product AS pd " +
            "WHERE pd.deleted = false "
    )
    List<ProductCashierDTO> getAllProductCashierDTOWhereDeletedIsFalse();


    @Modifying
    @Query("UPDATE Product AS pd SET pd.deleted = true WHERE pd.id = :productId")
    void softDelete(@Param("productId") long productId);

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);
}

