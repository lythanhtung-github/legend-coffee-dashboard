package com.cg.repository;

import com.cg.domain.entity.CustomerAvatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAvatarRepository extends JpaRepository<CustomerAvatar, Long> {
}
