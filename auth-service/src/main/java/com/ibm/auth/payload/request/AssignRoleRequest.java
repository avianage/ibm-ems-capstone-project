package com.ibm.auth.payload.request;


import com.ibm.auth.payload.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class AssignRoleRequest {

    private Set<Role> roles;

}
