package com.ibm.auth.repository;

import com.ibm.auth.entity.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends MongoRepository<Otp, String> {

    Optional<Otp> findByEmail(String email);

    void deleteByEmail(String email);

}