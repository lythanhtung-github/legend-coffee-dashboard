package com.cg.domain.dto.staff;

import com.cg.domain.entity.LocationRegion;
import com.cg.domain.entity.Staff;
import com.cg.domain.entity.StaffAvatar;
import com.cg.domain.entity.User;
import com.cg.domain.enums.EnumGender;
import com.cg.utils.AppUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StaffUpdateYourselfDTO {
    private Long id;

    @NotEmpty(message = "Vui lòng nhập tên nhân viên!")
    @Size(min = 5, max = 100, message = "Họ tên có độ dài nằm trong khoảng 5 - 100 ký tự.")
    private String fullName;

    @NotEmpty(message = "Vui lòng nhập ngày tháng năm sinh!")
    @Pattern(regexp = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", message = "Ngày sinh không đúng định dạng.")
    private String dob;

    @NotEmpty(message = "Vui lòng chọn giới tính!")
    private String gender;

    @NotEmpty(message = "Vui lòng nhập số điện thoại!")
    @Pattern(regexp = "^0[1-9][0-9]{8}$", message = "Số điện thoại không đúng định dạng.")
    private String phone;

    @Pattern(regexp = "^\\d+$", message = "ID Tỉnh/Thành phố phải là số.")
    @NotEmpty(message = "ID Tỉnh/Thành phố xã không được trống.")
    private String provinceId;

    @NotEmpty(message = "Tên Tỉnh/Thành phố không được trống.")
    private String provinceName;

    @Pattern(regexp = "^\\d+$", message = "ID Thành phố/Quận/Huyện phải là số.")
    @NotEmpty(message = "ID Thành phố/Quận/Huyện xã không được trống.")
    private String districtId;

    @NotEmpty(message = "Tên Thành phố/Quận/Huyện xã không được trống.")
    private String districtName;

    @Pattern(regexp = "^\\d+$", message = "ID Phường/Xã/Thị trấn phải là số.")
    @NotEmpty(message = "Phường/Xã/Thị trấn không được trống.")
    private String wardId;

    @NotEmpty(message = "Tên Phường/Xã/Thị trấn không được trống.")
    private String wardName;

    @NotEmpty(message = "Vui lòng nhập địa chỉ")
    @Size(min = 5, max = 100, message = "Địa chỉ có độ dài nằm trong khoảng 5 - 100 ký tự.")
    private String address;

    public LocationRegion toLocationRegion(){
        return new LocationRegion()
                .setId(id)
                .setProvinceId(provinceId)
                .setProvinceName(provinceName)
                .setDistrictId(districtId)
                .setDistrictName(districtName)
                .setWardId(wardId)
                .setWardName(wardName)
                .setAddress(address);
    }

    public Staff toStaff(User user, LocationRegion locationRegion, StaffAvatar staffAvatar){
        return new Staff()
                .setId(id)
                .setFullName(fullName)
                .setDob(AppUtils.stringToLocalDate(dob))
                .setGender(EnumGender.valueOf(gender))
                .setPhone(phone)
                .setLocationRegion(locationRegion)
                .setUser(user)
                .setAvatar(staffAvatar);
    }
}
