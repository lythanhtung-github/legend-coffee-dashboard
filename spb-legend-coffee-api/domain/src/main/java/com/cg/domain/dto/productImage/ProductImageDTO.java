package com.cg.domain.dto.productImage;

import com.cg.domain.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductImageDTO {
    private String id;
    private String fileName;

    private String fileFolder;

    private String fileUrl;

    private String fileType;

    private String cloudId;

    private String height;

    private String width;
    private Long ts;

    public ProductImage toProductImage() {
        return new ProductImage()
                .setId(id)
                .setFileName(fileName)
                .setFileFolder(fileFolder)
                .setFileUrl(fileUrl)
                .setFileType(fileType)
                .setCloudId(cloudId)
                .setHeight(Integer.parseInt(height))
                .setWidth(Integer.parseInt(width))
                .setTs(ts);
    }
}
