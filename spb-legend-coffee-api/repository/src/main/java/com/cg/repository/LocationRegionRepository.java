package com.cg.repository;

import com.cg.domain.entity.LocationRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LocationRegionRepository extends JpaRepository<LocationRegion, Long> {
}
