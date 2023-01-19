package com.cg.service.role;

import com.cg.domain.dto.role.RoleDTO;
import com.cg.domain.entity.Role;
import com.cg.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }


    @Override
    public Optional<Role> findById(Long id) {

        return roleRepository.findById(id);
    }

    @Override
    public Role getById(Long id) {
        return null;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void remove(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role findByCode(String name) {
        return roleRepository.findByCode(name);
    }

    @Override
    public List<RoleDTO> getAllRoleDTONoCustomer() {
        return roleRepository.getAllRoleDTONoCustomer();
    }

    @Override
    public List<RoleDTO> getAllRoleDTONoAdminAndCustomer() {
        return roleRepository.getAllRoleDTONoAdminAndCustomer();
    }
}
