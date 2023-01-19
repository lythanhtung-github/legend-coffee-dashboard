package com.cg.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserUpdatePasswordDTO {

    @NotBlank(message = "Vui lòng nhập mật khẩu.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Mật khẩu không đúng định dạng (Mật khẩu gồm 1 ít nhất ký tự hoa, thường, số, ký tự đặc biệt).")
    @Size(min = 8, max = 50, message = "Mật khẩu có độ dài nằm trong khoảng 8 - 50 ký tự.")
    private String password;


    @NotBlank(message = "Vui lòng nhập lại mật khẩu.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Mật khẩu không đúng định dạng (Mật khẩu gồm 1 ít nhất ký tự hoa, thường, số, ký tự đặc biệt).")
    @Size(min = 8, max = 50, message = "Mật khẩu có độ dài nằm trong khoảng 8 - 50 ký tự.")
    private String passwordConfirm;


    private String codeFirstLogin;
}
