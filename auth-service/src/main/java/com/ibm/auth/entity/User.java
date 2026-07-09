package com.ibm.auth.entity;

import com.ibm.auth.payload.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String employeeId;

    private String username;

    private String email;

    private String password;

    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    private boolean accountLocked = false;

    @Builder.Default
    private boolean deleted = false;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}