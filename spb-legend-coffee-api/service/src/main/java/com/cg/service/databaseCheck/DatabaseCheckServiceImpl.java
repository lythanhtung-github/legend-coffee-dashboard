package com.cg.service.databaseCheck;

import com.cg.domain.entity.DatabaseCheck;
import com.cg.repository.DatabaseCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DatabaseCheckServiceImpl implements IDatabaseCheckService{

    private static final Long DATABASE_CHECK_ID = 0L;
    @Autowired
    private DatabaseCheckRepository databaseCheckRepository;

    @Override
    public List<DatabaseCheck> findAll() {
        return null;
    }

    @Override
    public DatabaseCheck getById(Long id) {
        return databaseCheckRepository.getById(id);
    }

    @Override
    public DatabaseCheck getDatabaseCheck(){
        return databaseCheckRepository.findById(DATABASE_CHECK_ID).get();
    }

    @Override
    public Optional<DatabaseCheck> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public DatabaseCheck save(DatabaseCheck databaseCheck) {
        return null;
    }

    @Override
    public DatabaseCheck updateWithTableCheck(){
        DatabaseCheck databaseCheck = this.getDatabaseCheck();
        int tableCheck = databaseCheck.getTableCheck() + 1;
        databaseCheck.setTableCheck(tableCheck);
        return databaseCheckRepository.save(databaseCheck);
    }

    @Override
    public DatabaseCheck updateWithProductCheck(){
        DatabaseCheck databaseCheck = this.getDatabaseCheck();
        int productCheck = databaseCheck.getProductCheck() + 1;
        databaseCheck.setProductCheck(productCheck);
        return databaseCheckRepository.save(databaseCheck);
    }

    @Override
    public void remove(Long id) {

    }
}
