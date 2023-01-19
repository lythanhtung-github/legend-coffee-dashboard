package com.cg.service.staffAvatar;

import com.cg.domain.entity.StaffAvatar;
import com.cg.service.IGeneralService;

import java.util.Optional;


public interface IStaffAvatarService extends IGeneralService<StaffAvatar> {

    Optional<StaffAvatar> findById(String id);

    void delete(String id);
}
