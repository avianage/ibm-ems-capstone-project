package com.ibm.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "otp")
public class Otp {

    @Id
    private String id;

    private String email;

    private String otp;

    private LocalDateTime expiryTime;

    @Builder.Default
    private boolean verified = false;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}