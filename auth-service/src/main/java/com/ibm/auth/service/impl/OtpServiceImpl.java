package com.ibm.auth.service.impl;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ibm.auth.common.exception.PasswordRecoveryException;
import com.ibm.auth.entity.PasswordResetOtp;
import com.ibm.auth.repository.PasswordResetOtpRepository;
import com.ibm.auth.service.EmailService;
import com.ibm.auth.service.OtpService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OtpServiceImpl implements OtpService {

    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 10;
    private static final int RESET_TOKEN_VALIDITY_MINUTES = 10;
    private static final int MAX_ATTEMPTS = 5;

    private final PasswordResetOtpRepository otpRepository;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();
    private final BCryptPasswordEncoder otpEncoder = new BCryptPasswordEncoder();

    public OtpServiceImpl(PasswordResetOtpRepository otpRepository, EmailService emailService) {
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

//    @Override
//    public void generateAndSendOtp(String email) {
//        String otp = generateNumericOtp(OTP_LENGTH);
//
//        PasswordResetOtp record = new PasswordResetOtp();
//        record.setEmail(email);
//        record.setOtpHash(otpEncoder.encode(otp));
//        record.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));
//        record.setVerified(false);
//        record.setAttemptCount(0);
//
//        otpRepository.save(record);
//        emailService.sendOtpEmail(email, otp);
//    }

    @Override
    public String verifyOtp(String email, String otp) {
        PasswordResetOtp record = otpRepository.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new PasswordRecoveryException("No OTP request found for this email"));

        if (record.isVerified()) {
            throw new PasswordRecoveryException("OTP already used. Please request a new one");
        }

        if (record.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new PasswordRecoveryException("OTP has expired. Please request a new one");
        }

        if (record.getAttemptCount() >= MAX_ATTEMPTS) {
            throw new PasswordRecoveryException("Too many incorrect attempts. Please request a new OTP");
        }

        if (!otpEncoder.matches(otp, record.getOtpHash())) {
            record.setAttemptCount(record.getAttemptCount() + 1);
            otpRepository.save(record);
            throw new PasswordRecoveryException("Invalid OTP");
        }

        String resetToken = UUID.randomUUID().toString();
        record.setVerified(true);
        record.setResetToken(resetToken);
        record.setResetTokenExpiresAt(LocalDateTime.now().plusMinutes(RESET_TOKEN_VALIDITY_MINUTES));
        otpRepository.save(record);

        return resetToken;
    }

    @Override
    public void validateResetToken(String email, String resetToken) {
        PasswordResetOtp record = otpRepository.findByResetToken(resetToken)
                .orElseThrow(() -> new PasswordRecoveryException("Invalid or expired reset token"));

        if (!record.getEmail().equalsIgnoreCase(email)) {
            throw new PasswordRecoveryException("Invalid or expired reset token");
        }

        if (!record.isVerified()) {
            throw new PasswordRecoveryException("OTP was not verified for this request");
        }

        if (record.getResetTokenExpiresAt() == null || record.getResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new PasswordRecoveryException("Reset token has expired. Please restart the password reset process");
        }
    }

    @Override
    public void invalidateResetToken(String email) {
        otpRepository.findTopByEmailOrderByIdDesc(email).ifPresent(record -> {
            record.setResetToken(null);
            record.setResetTokenExpiresAt(null);
            otpRepository.save(record);
        });
    }

    private String generateNumericOtp(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(secureRandom.nextInt(10));
        }
        return sb.toString();
    }
    @Override
    public void generateAndSendOtp(String email) {
        String otp = generateNumericOtp(OTP_LENGTH);

        System.out.println("========= OTP for " + email + " : " + otp + " =========");  // ADD THIS

        PasswordResetOtp record = new PasswordResetOtp();
        record.setEmail(email);
        record.setOtpHash(otpEncoder.encode(otp));
        record.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));
        record.setVerified(false);
        record.setAttemptCount(0);

        otpRepository.save(record);
        emailService.sendOtpEmail(email, otp);
    }
}
