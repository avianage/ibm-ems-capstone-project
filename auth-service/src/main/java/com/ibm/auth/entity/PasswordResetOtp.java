package com.ibm.auth.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "password_reset_otp")
public class PasswordResetOtp {

   @Id
   private String id;

   @Indexed
   private String email;

   private String otpHash;

   private LocalDateTime expiresAt;

   private boolean verified = false;

   private int attemptCount = 0;

   @Indexed
   private String resetToken;

   private LocalDateTime resetTokenExpiresAt;

   public PasswordResetOtp() {
   }

   public String getId() {
       return id;
   }

   public void setId(String id) {
       this.id = id;
   }

   public String getEmail() {
       return email;
   }

   public void setEmail(String email) {
       this.email = email;
   }

   public String getOtpHash() {
       return otpHash;
   }

   public void setOtpHash(String otpHash) {
       this.otpHash = otpHash;
   }

   public LocalDateTime getExpiresAt() {
       return expiresAt;
   }

   public void setExpiresAt(LocalDateTime expiresAt) {
       this.expiresAt = expiresAt;
   }

   public boolean isVerified() {
       return verified;
   }

   public void setVerified(boolean verified) {
       this.verified = verified;
   }

   public int getAttemptCount() {
       return attemptCount;
   }

   public void setAttemptCount(int attemptCount) {
       this.attemptCount = attemptCount;
   }

   public String getResetToken() {
       return resetToken;
   }

   public void setResetToken(String resetToken) {
       this.resetToken = resetToken;
   }

   public LocalDateTime getResetTokenExpiresAt() {
       return resetTokenExpiresAt;
   }

   public void setResetTokenExpiresAt(LocalDateTime resetTokenExpiresAt) {
       this.resetTokenExpiresAt = resetTokenExpiresAt;
   }
}

