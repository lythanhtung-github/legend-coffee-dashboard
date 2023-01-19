package com.cg.api;

import com.cg.domain.dto.staff.StaffCreateDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.staff.StaffUpdateDTO;
import com.cg.domain.dto.staff.StaffUpdateYourselfDTO;
import com.cg.domain.entity.LocationRegion;
import com.cg.domain.entity.Role;
import com.cg.domain.entity.Staff;
import com.cg.domain.entity.User;
import com.cg.domain.enums.EnumGender;
import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.service.email.EmailSender;
import com.cg.service.role.IRoleService;
import com.cg.service.staff.IStaffService;
import com.cg.service.user.IUserService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staffs")
public class StaffAPI {

    @Autowired
    private IStaffService staffService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllByDeletedIsFalse() {
        List<StaffDTO> staffDTOList = staffService.getAllStaffDTOWhereDeletedIsFalse();

        if (staffDTOList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(staffDTOList, HttpStatus.OK);
    }

    @GetMapping("/recovery")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getAllByDeletedIsTrue() {
        List<StaffDTO> staffDTOList = staffService.getAllStaffDTOWhereDeletedIsTrue();

        if (staffDTOList.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(staffDTOList, HttpStatus.OK);
    }


    @GetMapping("/get-all-staffs-where-id-not/{staffId}")
    public ResponseEntity<?> getAllStaffDTOWhereDeletedIsFalseAndIdNot(@PathVariable String staffId) {

        Long sid = Long.parseLong(staffId);
        List<StaffDTO> staffDTOS = staffService.getAllStaffDTOWhereDeletedIsFalseAndIdNot(sid);

        if (staffDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(staffDTOS, HttpStatus.OK);
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<?> getById(@PathVariable String staffId) {
        long sid;
        try {
            sid = Long.parseLong(staffId);
        } catch (NumberFormatException e) {
            throw new DataInputException("ID nhân viên không hợp lệ.");
        }

        Optional<Staff> staffOptional = staffService.findById(sid);

        if (!staffOptional.isPresent()) {
            throw new DataInputException("ID nhân viên không hợp lệ.");
        }

        return new ResponseEntity<>(staffOptional.get().toStaffDTO(), HttpStatus.OK);
    }

    @GetMapping("/get-by-username/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable String username) {

        Optional<StaffDTO> staffDTOOptional = staffService.getByUsernameDTO(username);

        if (!staffDTOOptional.isPresent()) {
            throw new DataInputException("Nhân viên không hợp lệ.");
        }

        return new ResponseEntity<>(staffDTOOptional.get(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> createStaff(MultipartFile file, @Validated StaffCreateDTO staffCreateDTO, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Optional<User> userOptional = userService.findByUserName(staffCreateDTO.getUsername());

        if (userOptional.isPresent()) {
            throw new EmailExistsException("Email đã tồn tại trong hệ thống.");
        }

        Optional<Staff> staffOptional = staffService.findByPhone(staffCreateDTO.getPhone());

        if (staffOptional.isPresent()) {
            throw new DataInputException("Số điện thoại đã tồn tại trong hệ thống.");
        }

        LocationRegion locationRegion = staffCreateDTO.toLocationRegion();
        locationRegion.setId(null);

        Optional<Role> optRole = roleService.findById(Long.parseLong(staffCreateDTO.getRoleId()));

        if (!optRole.isPresent()) {
            throw new DataInputException("Role không hợp lệ.");
        }

        Role role = optRole.get();

        User user = staffCreateDTO.toUser(role);

        user.setId(null);
        user.setIsFirstLogin(true);

        user.setCodeFirstLogin(appUtils.randomOtp(12));

        user.setPassword(appUtils.randomPassword(6));

        Staff newStaff;

        if (file == null) {

            newStaff = staffService.createNoAvatar(staffCreateDTO, locationRegion, user);

        } else {

            newStaff = staffService.createWithAvatar(staffCreateDTO, locationRegion, user, file);

        }

        emailSender.sendRegisterStaffEmail(newStaff, user.getUsername());

        return new ResponseEntity<>(newStaff.toStaffDTO(), HttpStatus.CREATED);
    }

    @PatchMapping("/{staffId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> updateStaff(@PathVariable Long staffId, MultipartFile file, @Validated StaffUpdateDTO staffUpdateDTO, BindingResult bindingResult) {

        Optional<Staff> staffOptional = staffService.findById(staffId);
        if (!staffOptional.isPresent()) {
            throw new DataInputException("ID nhân viên không tồn tại.");
        }

        Staff staff = staffOptional.get();

        String phone = staffUpdateDTO.getPhone();

        if (staffService.existsByPhoneAndIdNot(phone, staffId)) {
            throw new DataInputException("Số điện thoại đã tồn tại trong hệ thống.");
        }

        long roleId;

        try {
            roleId = Long.parseLong(staffUpdateDTO.getRoleId());
        } catch (Exception e) {
            throw new DataInputException("Role không hợp lệ.");
        }

        Optional<Role> roleOptional = roleService.findById(roleId);

        if (!roleOptional.isPresent()) {
            throw new DataInputException("Role không tồn tại.");
        }

        Role role = roleOptional.get();

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        LocationRegion newLocationRegion = staffUpdateDTO.toLocationRegion();

        staff.setFullName(staffUpdateDTO.getFullName())
                .setDob(AppUtils.stringToLocalDate(staffUpdateDTO.getDob()))
                .setGender(EnumGender.valueOf(staffUpdateDTO.getGender()))
                .setPhone(phone);

        staff.getUser().setRole(role);

        staff.getLocationRegion()
                .setProvinceId(newLocationRegion.getProvinceId())
                .setProvinceName(newLocationRegion.getProvinceName())
                .setDistrictId(newLocationRegion.getDistrictId())
                .setDistrictName(newLocationRegion.getDistrictName())
                .setWardId(newLocationRegion.getWardId())
                .setWardName(newLocationRegion.getWardName())
                .setAddress(newLocationRegion.getAddress());

        staff = staffService.saveNoAvatar(staff);

        if (file != null) {
            staff = staffService.saveWithAvatar(staff, file);
        }

        return new ResponseEntity<>(staff.toStaffDTO(), HttpStatus.OK);
    }

    @PatchMapping("/update-yourself/{staffId}")
    public ResponseEntity<?> update(@PathVariable Long staffId, MultipartFile file, @Validated StaffUpdateYourselfDTO staffUpdateYourselfDTO, BindingResult bindingResult) {

        Optional<Staff> staffOptional = staffService.findById(staffId);
        if (!staffOptional.isPresent()) {
            throw new DataInputException("ID nhân viên không tồn tại.");
        }

        String phone = staffUpdateYourselfDTO.getPhone();

        if (staffService.existsByPhoneAndIdNot(phone, staffId)) {
            throw new DataInputException("Số điện thoại đã tồn tại trong hệ thống.");
        }

        Staff staff = staffOptional.get();

        LocationRegion locationRegion = staffUpdateYourselfDTO.toLocationRegion();
        locationRegion.setId(staff.getLocationRegion().getId());

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        staff.setFullName(staffUpdateYourselfDTO.getFullName());
        staff.setPhone(phone);
        staff.setLocationRegion(locationRegion);
        staff = staffService.saveWithLocationRegion(staff);
        if (file != null) {
            staff = staffService.saveWithAvatar(staff, file);
        }

        return new ResponseEntity<>(staff.toStaffDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{staffId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long staffId) {

        Optional<Staff> staffOptional = staffService.findById(staffId);

        if (!staffOptional.isPresent()) {
            throw new DataInputException("ID nhân viên không hợp lệ.");
        }

        Staff staff = staffOptional.get();

        if (staff.getUser().getRole().getCode().equals("ADMIN")) {
            throw new DataInputException("Không thể xóa nhân viên là ADMIN.");
        }

        try {
            userService.softDelete(staff.getUser().getId());
            staffService.softDelete(staffId);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } catch (Exception e) {

            throw new DataInputException("Vui lòng liên hệ Administrator.");
        }
    }


    @PatchMapping("/recovery/{staffId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> recovery(@PathVariable Long staffId) {

        Optional<Staff> staffOptional = staffService.findById(staffId);

        if (!staffOptional.isPresent()) {
            throw new DataInputException("ID nhân viên không hợp lệ.");
        }

        Staff staff = staffOptional.get();


        try {
            staffService.recoveryAccount(staffId);
            userService.recovery(staff.getUser().getId());

            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } catch (Exception e) {

            throw new DataInputException("Vui lòng liên hệ Administrator.");
        }
    }

}
