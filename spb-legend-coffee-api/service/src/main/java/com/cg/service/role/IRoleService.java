package com.cg.service.role;

import com.cg.domain.dto.role.RoleDTO;
import com.cg.domain.entity.Role;
import com.cg.service.IGeneralService;

import java.util.List;


public interface IRoleService extends IGeneralService<Role> {
    Role findByCode(String code);

    List<RoleDTO> getAllRoleDTONoCustomer();

    List<RoleDTO> getAllRoleDTONoAdminAndCustomer();
}
