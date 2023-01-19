package com.cg.domain.dto.category;

import com.cg.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDTO {
    private Long id;

    @NotEmpty(message = "Vui lòng nhập tên loại sản phẩm!")
    @Size(min = 3, max = 30, message = "Loại sản phẩm có độ dài nằm trong khoảng 3 - 30 ký tự.")
    private String title;

    public Category toCategory() {
        return new Category()
                .setId(id)
                .setTitle(title);
    }
}
