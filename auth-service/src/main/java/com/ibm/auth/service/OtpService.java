package com.ibm.auth.service;

public interface OtpService {
	
    void generateAndSendOtp(String email);
    String verifyOtp(String email, String otp);
    void validateResetToken(String email, String resetToken);
    void invalidateResetToken(String email);
}