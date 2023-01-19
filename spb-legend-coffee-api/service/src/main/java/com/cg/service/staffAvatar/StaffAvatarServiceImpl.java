package com.cg.service.staffAvatar;

import com.cg.domain.entity.StaffAvatar;
import com.cg.repository.StaffAvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class StaffAvatarServiceImpl implements IStaffAvatarService {

    @Autowired
    private StaffAvatarRepository staffAvatarRepository;

    @Override
    public List<StaffAvatar> findAll() {
        return null;
    }

    @Override
    public StaffAvatar getById(Long id) {
        return null;
    }

    @Override
    public Optional<StaffAvatar> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<StaffAvatar> findById(String id) {

        return staffAvatarRepository.findById(id);
    }

    @Override
    public StaffAvatar save(StaffAvatar staffAvatar) {
        return staffAvatarRepository.save(staffAvatar);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void delete(String id) {
        staffAvatarRepository.deleteById(id);
    }
}


