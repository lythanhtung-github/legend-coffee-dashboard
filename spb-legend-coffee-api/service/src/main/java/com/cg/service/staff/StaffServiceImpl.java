package com.cg.service.staff;

import com.cg.cloudinary.CloudinaryUploadUtil;
import com.cg.domain.dto.staff.StaffCreateDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.entity.*;
import com.cg.domain.enums.EnumFileType;
import com.cg.exception.DataInputException;
import com.cg.repository.StaffRepository;
import com.cg.service.locationRegion.ILocationRegionService;
import com.cg.service.staffAvatar.IStaffAvatarService;
import com.cg.service.upload.IUploadService;
import com.cg.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class StaffServiceImpl implements IStaffService {

    private final String DEFAULT_USER_IMAGE_ID = "default_user_image";
    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private IStaffAvatarService staffAvatarService;

    @Autowired
    private ILocationRegionService locationRegionService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUploadService uploadService;

    @Autowired
    private CloudinaryUploadUtil cloudinaryUploadUtil;

    @Override
    public List<Staff> findAll() {
        return null;
    }

    @Override
    public List<StaffDTO> getAllStaffDTOWhereDeletedIsFalse(){
        return staffRepository.getAllStaffDTOWhereDeletedIsFalse();
    }

    @Override
    public List<StaffDTO> getAllStaffDTOWhereDeletedIsTrue() {
        return staffRepository.getAllStaffDTOWhereDeletedIsTrue();
    }

    @Override
    public List<StaffDTO> getAllStaffDTOWhereDeletedIsFalseAndIdNot(Long staffId){
        return staffRepository.getAllStaffDTOWhereDeletedIsFalseAndIdNot(staffId);
    }

    @Override
    public Optional<StaffDTO> getByUsernameDTO(String username){
        return staffRepository.getByUsernameDTO(username);
    }

    @Override
    public Staff getById(Long id) {
        return staffRepository.getById(id);
    }

    @Override
    public Optional<Staff> findById(Long id) {
        return staffRepository.findById(id);
    }

    @Override
    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public Staff createWithAvatar(StaffCreateDTO staffCreateDTO, LocationRegion locationRegion, User user, MultipartFile file) {

        locationRegion = locationRegionService.save(locationRegion);
        user = userService.save(user);

        String fileType = file.getContentType();
        assert fileType != null;
        fileType = fileType.substring(0, 5);
        StaffAvatar staffAvatar = new StaffAvatar();
        staffAvatar.setFileType(fileType);
        staffAvatar = staffAvatarService.save(staffAvatar);

        if (fileType.equals(EnumFileType.IMAGE.getValue())) {
            staffAvatar = uploadAndSaveStaffAvatar(file, staffAvatar);
        }

        Staff staff  = staffCreateDTO.toStaff(user, locationRegion, staffAvatar);
        staff.setId(null);

        staff = staffRepository.save(staff);

        return staff;
    }

    @Override
    public Staff createNoAvatar(StaffCreateDTO staffCreateDTO, LocationRegion locationRegion, User user) {

        locationRegion = locationRegionService.save(locationRegion);
        user = userService.save(user);

        StaffAvatar staffAvatar = staffAvatarService.findById(DEFAULT_USER_IMAGE_ID).get();
        staffAvatar = staffAvatarService.save(staffAvatar);

        Staff staff  = staffCreateDTO.toStaff(user, locationRegion, staffAvatar);
        staff.setId(null);

        staff = staffRepository.save(staff);

        return staff;
    }

    @Override
    public Staff saveWithLocationRegion(Staff staff){

        locationRegionService.save(staff.getLocationRegion());

        return staffRepository.save(staff);
    }

    @Override
    public Staff saveNoAvatar(Staff staff){

        userService.saveNoPassWord(staff.getUser());
        locationRegionService.save(staff.getLocationRegion());

        return staffRepository.save(staff);
    }
    @Override
    public Staff saveWithAvatar(Staff staff, MultipartFile file) {

            String fileType = file.getContentType();
            assert fileType != null;
            fileType = fileType.substring(0, 5);
            StaffAvatar staffAvatar = new StaffAvatar();
            staffAvatar.setFileType(fileType);

            staffAvatar = staffAvatarService.save(staffAvatar);

            if (fileType.equals(EnumFileType.IMAGE.getValue())) {
                staffAvatar = uploadAndSaveStaffAvatar(file, staffAvatar);
            }

            staff.setAvatar(staffAvatar);
            staff = staffRepository.save(staff);

            return staff;
    }

    @Override
    public StaffAvatar uploadAndSaveStaffAvatar(MultipartFile file, StaffAvatar staffAvatar) {
        try {
            Map uploadResult = uploadService.uploadImage(
                    file,
                    cloudinaryUploadUtil.buildImageUploadParams(
                            staffAvatar.getId(),
                            cloudinaryUploadUtil.STAFF_IMAGE_UPLOAD_FOLDER,
                            cloudinaryUploadUtil.ERROR_IMAGE_UPLOAD
                    )
            );

            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            staffAvatar.setFileName(staffAvatar.getId() + "." + fileFormat);
            staffAvatar.setFileUrl(fileUrl);
            staffAvatar.setFileFolder(cloudinaryUploadUtil.STAFF_IMAGE_UPLOAD_FOLDER);
            staffAvatar.setCloudId(staffAvatar.getFileFolder() + "/" + staffAvatar.getId());

            return staffAvatarService.save(staffAvatar);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại.");
        }
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void softDelete(Long staffId) {
        staffRepository.softDelete(staffId);
    }

    @Override
    public void recoveryAccount(Long staffId) {
        staffRepository.recoveryAccount(staffId);
    }

    @Override
    public Optional<Staff> findByPhone(String phone) {
        return staffRepository.findByPhone(phone);
    }

    @Override
    public Boolean existsByPhoneAndIdNot(String phone, Long id) {
        return staffRepository.existsByPhoneAndIdNot(phone, id);
    }
}
