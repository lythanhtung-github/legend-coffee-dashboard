package com.cg.domain.entity;

import com.cg.domain.dto.role.RoleDTO;
import com.cg.domain.enums.EnumRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EnumRole name;

    @OneToMany(targetEntity = User.class)
    private List<User> users;

    public RoleDTO toRoleDTO() {
        return new RoleDTO()
                .setId(id)
                .setCode(code);
    }
}
