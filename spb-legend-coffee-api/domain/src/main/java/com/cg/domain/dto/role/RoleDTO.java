package com.cg.domain.dto.role;

import com.cg.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleDTO {
    @NotNull(message = "Bạn chưa chọn quyền!")
    private Long id;

    private String code;

    public Role toRole() {
        return new Role()
                .setId(id)
                .setCode(code);
    }
}
