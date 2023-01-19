package com.cg.domain.dto.product;

import com.cg.utils.JsonToMapConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductCashierDTO {
    private Long id;

    private String title;


    private String description;

    private Map<String,SizeDTO> sizes;

    private Long categoryId;

    private String photo;

    public ProductCashierDTO(Long id, String title, String description, String sizes, Long categoryId, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.sizes = (Map<String, SizeDTO>) JsonToMapConverter.convertToDatabaseColumn(sizes);
        this.categoryId = categoryId;
        this.photo = imageUrl;
    }
}
