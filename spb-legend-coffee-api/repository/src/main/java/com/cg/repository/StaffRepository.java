package com.cg.repository;

import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("SELECT NEW com.cg.domain.dto.staff.StaffDTO (" +
                "st.id, " +
                "st.fullName, " +
                "st.phone, " +
                "st.dob, " +
                "st.gender, " +
                "st.locationRegion, " +
                "st.user, " +
                "st.avatar" +
            ") " +
            "FROM Staff AS st " +
            "WHERE st.deleted = false "
    )
    List<StaffDTO> getAllStaffDTOWhereDeletedIsFalse();

    @Query("SELECT NEW com.cg.domain.dto.staff.StaffDTO (" +
                "st.id, " +
                "st.fullName, " +
                "st.phone, " +
                "st.dob, " +
                "st.gender, " +
                "st.locationRegion, " +
                "st.user, " +
                "st.avatar" +
            ") " +
            "FROM Staff AS st " +
            "WHERE st.deleted = true "
    )
    List<StaffDTO> getAllStaffDTOWhereDeletedIsTrue();

    @Query("SELECT NEW com.cg.domain.dto.staff.StaffDTO (" +
                "st.id, " +
                "st.fullName, " +
                "st.phone, " +
                "st.dob, " +
                "st.gender, " +
                "st.locationRegion, " +
                "st.user, " +
                "st.avatar" +
            ") " +
            "FROM Staff AS st " +
            "WHERE st.deleted = false " +
            "AND st.id <> :staffId " +
            "AND st.user.role.code <> 'CUSTOMER'"
    )
    List<StaffDTO> getAllStaffDTOWhereDeletedIsFalseAndIdNot(@Param("staffId") Long staffId);



    @Query("SELECT NEW com.cg.domain.dto.staff.StaffDTO (" +
                "st.id, " +
                "st.fullName, " +
                "st.phone, " +
                "st.dob, " +
                "st.gender, " +
                "st.locationRegion, " +
                "st.user, " +
                "st.avatar" +
            ") " +
            "FROM Staff AS st " +
            "WHERE st.deleted = false " +
            "AND st.user.username = :username"
    )
    Optional<StaffDTO> getByUsernameDTO(@Param("username") String username);

    @Modifying
    @Query("UPDATE Staff AS st SET st.deleted = true WHERE st.id = :staffId")
    void softDelete(@Param("staffId") long staffId);

    @Modifying
    @Query("UPDATE Staff AS st SET st.deleted = false WHERE st.id = :staffId")
    void recoveryAccount(@Param("staffId") long staffId);

    Boolean existsByPhoneAndIdNot(String phone, Long id);

    Optional<Staff> findByPhone(String phone);

    List<Staff> findAllByIdNot(long id);
}

