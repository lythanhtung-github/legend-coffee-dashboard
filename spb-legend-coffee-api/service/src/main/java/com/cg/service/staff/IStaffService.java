package com.cg.service.staff;

import com.cg.domain.dto.staff.StaffCreateDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.entity.LocationRegion;
import com.cg.domain.entity.Staff;
import com.cg.domain.entity.StaffAvatar;
import com.cg.domain.entity.User;
import com.cg.service.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


public interface IStaffService extends IGeneralService<Staff> {

    List<StaffDTO> getAllStaffDTOWhereDeletedIsFalse();

    List<StaffDTO> getAllStaffDTOWhereDeletedIsTrue();

    List<StaffDTO> getAllStaffDTOWhereDeletedIsFalseAndIdNot(Long staffId);

    Optional<StaffDTO> getByUsernameDTO(String username);

    Staff createWithAvatar(StaffCreateDTO staffCreateDTO, LocationRegion locationRegion, User user, MultipartFile file);

    Staff createNoAvatar(StaffCreateDTO staffCreateDTO, LocationRegion locationRegion, User user);

    Staff saveWithLocationRegion(Staff staff);

    Staff saveNoAvatar(Staff staff);

    Staff saveWithAvatar(Staff staff, MultipartFile file);

    StaffAvatar uploadAndSaveStaffAvatar(MultipartFile file, StaffAvatar staffAvatar);

    void softDelete(Long staffId);

    void recoveryAccount(Long staffId);

    Optional<Staff> findByPhone(String phone);

    Boolean existsByPhoneAndIdNot(String phone, Long id);
}

