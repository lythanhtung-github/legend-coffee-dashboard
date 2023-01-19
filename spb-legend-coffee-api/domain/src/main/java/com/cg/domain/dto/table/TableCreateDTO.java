package com.cg.domain.dto.table;

import com.cg.domain.entity.CTable;
import com.cg.domain.enums.EnumTableStatus;
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
public class TableCreateDTO {

    private Long id;

    @NotEmpty(message = "Vui lòng nhập tên bàn.")
    @Size(min = 4, max = 20, message = "Tên bàn có độ dài nằm trong khoảng 4 - 20 ký tự.")
    private String name;


    private CTable toTable(){
        return new CTable()
                .setId(id)
                .setName(name)
                .setStatus(EnumTableStatus.EMPTY);
    }
}
