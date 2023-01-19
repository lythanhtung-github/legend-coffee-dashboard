package com.cg.repository;

import com.cg.domain.entity.StaffAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StaffAvatarRepository extends JpaRepository<StaffAvatar, String> {
    @Override
    Optional<StaffAvatar> findById(String id);
}

