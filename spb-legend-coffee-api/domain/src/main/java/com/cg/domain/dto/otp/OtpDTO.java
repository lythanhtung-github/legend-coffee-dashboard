package com.cg.domain.dto.otp;


import com.cg.domain.dto.user.UserDTO;
import com.cg.domain.entity.Otp;
import com.cg.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpDTO {
    private Long id;

    private String code;

    private UserDTO user;

    public OtpDTO(Long id, String code, User user) {
        this.id = id;
        this.code = code;
        this.user = user.toUserDTO();
    }

    public Otp toOtp(){
        return new Otp()
                .setId(id)
                .setCode(code)
                .setUser(user.toUser());
    }
}
