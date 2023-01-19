package com.cg.repository;

import com.cg.domain.entity.DatabaseCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseCheckRepository extends JpaRepository<DatabaseCheck, Long> {
}
