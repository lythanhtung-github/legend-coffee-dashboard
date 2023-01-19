package com.cg.service.productImage;

import com.cg.domain.entity.ProductImage;
import com.cg.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ProductImageImpl implements IProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public List<ProductImage> findAll() {
        return productImageRepository.findAll();
    }

    @Override
    public ProductImage getById(Long id) {
        return null;
    }

    @Override
    public Optional<ProductImage> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<ProductImage> findById(String id) {
        return productImageRepository.findById(id);
    }

    @Override
    public ProductImage save(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void delete(String id) {
        productImageRepository.deleteById(id);
    }
}
