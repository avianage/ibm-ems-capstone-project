package com.ibm.auth.payload.response;

import com.ibm.auth.payload.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class LoginResponse {

    private String token;

    private String username;

    private Set<Role> roles;

}
