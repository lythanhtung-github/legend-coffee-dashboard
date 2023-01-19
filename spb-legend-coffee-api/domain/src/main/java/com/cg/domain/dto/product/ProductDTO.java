package com.cg.domain.dto.product;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.dto.productImage.ProductImageDTO;
import com.cg.domain.entity.Category;
import com.cg.domain.entity.Product;
import com.cg.domain.entity.ProductImage;

import com.cg.utils.JsonToMapConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String title;

    private String summary;
    private String description;

    private Map<String,SizeDTO> sizes;
    private CategoryDTO category;
    private ProductImageDTO productImage;

    public ProductDTO(Long id, String title, String description, String summary, String sizes, Category category, ProductImage productImage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.summary = summary;
        this.sizes = (Map<String, SizeDTO>) JsonToMapConverter.convertToDatabaseColumn(sizes);
        this.category = category.toCategoryDTO();
        this.productImage = productImage.toProductImageDTO();
    }

    public Product toProduct() {
        return new Product()
                .setId(id)
                .setTitle(title)
                .setDescription(description)
                .setSummary(summary)
                .setSizes(JsonToMapConverter.convertToEntityAttribute(sizes))
                .setCategory(category.toCategory())
                .setProductImage(productImage.toProductImage());
    }
}
