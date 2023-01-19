package com.cg.domain.dto.customer;

import com.cg.domain.dto.locationRegion.LocationRegionDTO;
import com.cg.domain.dto.user.UserDTO;
import com.cg.domain.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDTO {

    private Long id;


    private String fullName;


    private String phone;


    private BigDecimal balance;


    private LocationRegionDTO locationRegion;


    private UserDTO user;

    public Customer toCustomer() {

        return new Customer()
                .setId(id)
                .setFullName(fullName)
                .setPhone(phone)
                .setBalance(balance)
                .setLocationRegion(locationRegion.toLocationRegion())
                .setUser(user.toUser());
    }
}
