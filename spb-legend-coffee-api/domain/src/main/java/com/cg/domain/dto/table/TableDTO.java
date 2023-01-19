package com.cg.domain.dto.table;

import com.cg.domain.entity.CTable;
import com.cg.domain.enums.EnumTableStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TableDTO {

    private Long id;

    private String name;

    private String status;

    private String statusValue;

    public TableDTO(Long id, String name, EnumTableStatus status) {
        this.id = id;
        this.name = name;
        this.status = String.valueOf(status);
        this.statusValue = status.getValue();
    }

    public CTable toTable(){
        return new CTable()
                .setId(id)
                .setName(name)
                .setStatus(EnumTableStatus.valueOf(status));
    }
}
