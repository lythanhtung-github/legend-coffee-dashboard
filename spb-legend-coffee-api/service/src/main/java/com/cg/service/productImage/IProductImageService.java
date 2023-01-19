package com.cg.service.productImage;

import com.cg.domain.entity.ProductImage;
import com.cg.service.IGeneralService;

import java.util.Optional;


public interface IProductImageService extends IGeneralService<ProductImage> {

    Optional<ProductImage> findById(String id);

    void delete(String id);
}

