package com.cg.domain.entity;


import com.cg.domain.dto.customer.CustomerDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal balance;

    @OneToOne
    @JoinColumn(name = "location_region_id")
    private LocationRegion locationRegion;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public CustomerDTO toCustomerDTO() {

        return new CustomerDTO()
                .setId(id)
                .setFullName(fullName)
                .setPhone(phone)
                .setBalance(balance)
                .setLocationRegion(locationRegion.toLocationRegionDTO())
                .setUser(user.toUserDTO());
    }
}
