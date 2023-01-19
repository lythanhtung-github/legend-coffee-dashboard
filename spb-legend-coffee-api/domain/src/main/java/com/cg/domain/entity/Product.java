package com.cg.domain.entity;

import com.cg.domain.dto.product.ProductCashierDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.dto.product.SizeDTO;
import com.cg.utils.JsonToMapConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    private String summary;

    private String description;

    @Column(columnDefinition = "JSON")
    private String sizes;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "product_image_id")
    private ProductImage productImage;

    public ProductDTO toProductDTO() {
        return new ProductDTO()
                .setId(id)
                .setTitle(title)
                .setDescription(description)
                .setSummary(summary)
                .setSizes((Map<String, SizeDTO>) JsonToMapConverter.convertToDatabaseColumn(sizes))
                .setCategory(category.toCategoryDTO())
                .setProductImage(productImage.toProductImageDTO());
    }

    public ProductCashierDTO toProductCashierDTO() {
        return new ProductCashierDTO()
                .setId(id)
                .setTitle(title)
                .setDescription(description)
                .setSizes((Map<String, SizeDTO>) JsonToMapConverter.convertToDatabaseColumn(sizes))
                .setCategoryId(category.getId())
                .setPhoto(productImage.getFileUrl());
    }
}
