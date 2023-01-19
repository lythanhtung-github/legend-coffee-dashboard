package com.cg.domain.dto.order;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class OrderCreateDTO {

    private Long id;


    @Pattern(regexp = "^\\d+$", message = "Id bàn phải là số.")
    private Long tableId;

    @Pattern(regexp = "^\\d+$", message = "Id nhân viên phải là số.")
    private Long staffId;

}
