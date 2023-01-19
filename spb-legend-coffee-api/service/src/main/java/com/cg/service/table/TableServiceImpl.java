package com.cg.service.table;

import com.cg.domain.dto.table.TableDTO;
import com.cg.domain.entity.CTable;
import com.cg.domain.enums.EnumTableStatus;
import com.cg.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class TableServiceImpl implements ITableService{

    @Autowired
    private TableRepository tableRepository;

    @Override
    public List<CTable> findAll() {
        return tableRepository.findAll();
    }

    @Override
    public List<TableDTO> getAllTableWhereDeletedIsFalse(){
        return tableRepository.getAllTableWhereDeletedIsFalse();
    }

    @Override
    public List<TableDTO> getTablesWhereStatus(EnumTableStatus status){
        return tableRepository.getTablesWhereStatus(status);
    }

    @Override
    public CTable getById(Long id) {
        return tableRepository.getById(id);
    }

    @Override
    public Optional<CTable> findById(Long id) {
        return tableRepository.findById(id);
    }

    @Override
    public boolean existByName(String name){
        return tableRepository.existsByName(name);
    }
    @Override
    public boolean existByNameAndIdIsNot(String name, Long id){
        return tableRepository.existsByNameAndIdIsNot(name, id);
    }

    @Override
    public CTable save(CTable cTable) {
        return tableRepository.save(cTable);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void softDelete(Long tableId){
        tableRepository.softDelete(tableId);
    }
}
