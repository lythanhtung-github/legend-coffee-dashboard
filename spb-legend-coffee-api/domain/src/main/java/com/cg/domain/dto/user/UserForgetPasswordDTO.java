package com.cg.domain.dto.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserForgetPasswordDTO {

    @NotBlank(message = "Vui lòng nhập email.")
    @Email(message = "Email không đúng định dạng.")
    private String username;

    @NotEmpty(message = "Vui lòng nhập mật khẩu mới.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Mật khẩu không đúng định dạng (Mật khẩu gồm 1 ít nhất ký tự hoa, thường, số, ký tự đặc biệt).")
    @Size(min = 8, max = 50, message = "Mật khẩu có độ dài nằm trong khoảng 8 - 50 ký tự.")
    private String password;

    @NotEmpty(message = "Vui lòng nhập lại mật khẩu mới.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Mật khẩu không đúng định dạng (Mật khẩu gồm 1 ít nhất ký tự hoa, thường, số, ký tự đặc biệt).")
    @Size(min = 8, max = 50, message = "Mật khẩu có độ dài nằm trong khoảng 8 - 50 ký tự.")
    private String passwordConfirm;

    @NotBlank(message = "Vui lòng nhập mã OTP.")
    @Size(min = 6, max = 6, message = "Mã OTP bao gồm 6 ký tự.")
    private String otp;
}
