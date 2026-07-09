package com.ibm.auth.common.util;

import com.ibm.auth.entity.User;
import com.ibm.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeIdGenerator {

    private  final UserRepository userRepository;

    public String generateEmployeeId() {

        return userRepository.findTopByOrderByEmployeeIdDesc()
                .map(User::getEmployeeId)
                .map(lastId -> {
                    int number = Integer.parseInt(lastId.substring(1));
                    return String.format("A%06d", number + 1);
                })
                .orElse("A000001");
    }
}
