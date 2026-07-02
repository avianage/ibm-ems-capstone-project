package com.ibm.auth.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.auth.entity.PasswordResetOtp;

import java.util.Optional;

public interface PasswordResetOtpRepository extends MongoRepository<PasswordResetOtp, String> {

   Optional<PasswordResetOtp> findTopByEmailOrderByIdDesc(String email);

   Optional<PasswordResetOtp> findByResetToken(String resetToken);
}
