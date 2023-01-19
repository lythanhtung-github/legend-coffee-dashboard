package com.cg.repository;

import com.cg.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<User> findByCodeFirstLogin(String codeFirstLogin);


    @Modifying
    @Query("UPDATE User AS us SET us.deleted = true WHERE us.id = :userId")
    void softDelete(@Param("userId") long userId);

    @Modifying
    @Query("UPDATE User AS us SET us.deleted = false WHERE us.id = :userId")
    void recovery(@Param("userId") long userId);

}
