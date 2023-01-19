package com.cg.service.table;

import com.cg.domain.dto.table.TableDTO;
import com.cg.domain.entity.CTable;
import com.cg.domain.enums.EnumTableStatus;
import com.cg.service.IGeneralService;

import java.util.List;


public interface ITableService extends IGeneralService<CTable> {
    List<TableDTO> getAllTableWhereDeletedIsFalse();

    List<TableDTO> getTablesWhereStatus(EnumTableStatus status);

    boolean existByName(String name);

    boolean existByNameAndIdIsNot(String name, Long id);

    void softDelete(Long id);
}
