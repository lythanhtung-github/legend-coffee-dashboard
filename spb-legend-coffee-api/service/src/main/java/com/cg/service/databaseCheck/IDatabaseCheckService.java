package com.cg.service.databaseCheck;

import com.cg.domain.entity.DatabaseCheck;
import com.cg.service.IGeneralService;

public interface IDatabaseCheckService extends IGeneralService<DatabaseCheck> {
    DatabaseCheck getDatabaseCheck();

    DatabaseCheck updateWithTableCheck();

    DatabaseCheck updateWithProductCheck();
}
