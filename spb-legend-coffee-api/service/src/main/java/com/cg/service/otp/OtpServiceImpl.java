package com.cg.service.otp;

import com.cg.domain.entity.Otp;
import com.cg.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class OtpServiceImpl implements IOtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public List<Otp> findAll() {
        return null;
    }

    @Override
    public Otp getById(Long id) {
        return null;
    }

    @Override
    public Optional<Otp> getOtpByUserId(Long userId) {
        return otpRepository.getOtpByUserId(userId);
    }

    @Override
    public Optional<Otp> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Otp getByCode(String code) {
        return otpRepository.getByCode(code);
    }

    @Override
    public Otp save(Otp otp) {
        return otpRepository.save(otp);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void softDelete(Long otpId) {
        otpRepository.softDelete(otpId);
    }
}
