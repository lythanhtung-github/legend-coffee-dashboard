package com.cg.domain.entity;

import com.cg.domain.dto.otp.OtpDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="otps")
public class Otp extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public OtpDTO toOtpDTO(){
        return new OtpDTO()
                .setId(id)
                .setCode(code)
                .setUser(user.toUserDTO());
    }
}
