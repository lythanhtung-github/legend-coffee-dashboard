package com.cg.service.product;

import com.cg.domain.dto.product.ProductCashierDTO;
import com.cg.domain.dto.product.ProductCreateDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.entity.Product;
import com.cg.domain.entity.ProductImage;
import com.cg.service.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface IProductService extends IGeneralService<Product> {
    List<ProductDTO> getAllProductDTOWhereDeletedIsFalse();

    List<ProductCashierDTO> getAllProductCashierDTOWhereDeletedIsFalse();

    boolean existsByTitle(String title);

    boolean existsByProductNameAndIdNot(String title, Long id);

    Product createWithImage(ProductCreateDTO productCreateDTO, MultipartFile file);


    Product createNoImage(ProductCreateDTO productCreateDTO);

    Product saveWithImage(Product product, MultipartFile file);

    ProductImage uploadAndSaveProductImage(MultipartFile file, ProductImage productImage);

    void softDelete(Long productId);
}