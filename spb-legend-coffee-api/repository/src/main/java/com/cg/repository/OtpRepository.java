package com.cg.repository;

import com.cg.domain.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

    Otp getByCode(String code);

    @Query("SELECT NEW com.cg.domain.entity.Otp (" +
                "otp.id, " +
                "otp.code, " +
                "otp.user " +
            ") " +
            "FROM Otp AS otp " +
            "WHERE otp.deleted = false " +
            "AND otp.id = :userId "
    )
    Optional<Otp> getOtpByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Otp AS otp SET otp.deleted = true WHERE otp.id = :otpId")
    void softDelete(@Param("otpId") long otpId);
}
