package com.cg.domain.dto.product;

import com.cg.domain.entity.Category;
import com.cg.domain.entity.Product;
import com.cg.domain.entity.ProductImage;
import com.cg.utils.JsonToMapConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductCreateDTO {
    private Long id;

    @NotEmpty(message = "Vui lòng nhập tên sản phẩm.")
    @Size(min = 5, max = 50, message = "Tên sản phẩm có độ dài nằm trong khoảng 5 - 50 ký tự.")
    private String title;
    @NotEmpty(message = "Vui lòng nhập mô tả sản phẩm.")
    @Size(min = 5, max = 500, message = "Mô tả sản phẩm có độ dài nằm trong khoảng 5 - 500 ký tự.")
    private String description;

    private String summary;
    private String sizes;

    @NotNull(message = "Vui lòng chọn danh mục sản phẩm.")
    @Pattern(regexp = "^\\d+$", message = "ID danh mục sản phẩm phải là số.")
    private String categoryId;

    public Product toProduct(Category category, ProductImage productImage) {
        return new Product()
                .setId(id)
                .setTitle(title)
                .setDescription(description)
                .setSummary(summary)
                .setCategory(category)
                .setSizes(sizes)
                .setProductImage(productImage);
    }
}
