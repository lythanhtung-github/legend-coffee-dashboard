package com.cg.api;


import com.cg.domain.dto.role.RoleDTO;
import com.cg.service.role.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleAPI {

    @Autowired
    private IRoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllRoleDTONoCustomer() {

        List<RoleDTO> roleDTOS = roleService.getAllRoleDTONoCustomer();

        if (roleDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(roleDTOS, HttpStatus.OK);
    }

    @GetMapping("/no-ad-and-cus")
    public ResponseEntity<?> getAllRoleDTONoAdminAndCustomer() {

        List<RoleDTO> roleDTOS = roleService.getAllRoleDTONoAdminAndCustomer();

        if (roleDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(roleDTOS, HttpStatus.OK);
    }
}
