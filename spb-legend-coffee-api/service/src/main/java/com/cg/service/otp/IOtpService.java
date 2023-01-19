package com.cg.service.otp;


import com.cg.domain.entity.Otp;
import com.cg.service.IGeneralService;

import java.util.Optional;


public interface IOtpService extends IGeneralService<Otp> {

    Optional<Otp> getOtpByUserId(Long userId);

    Otp getByCode(String code);

    void softDelete(Long otpId);
}
