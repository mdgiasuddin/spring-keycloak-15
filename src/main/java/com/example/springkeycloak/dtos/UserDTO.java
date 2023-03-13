package com.example.springkeycloak.dtos;

import com.example.springkeycloak.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserDTO {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private UserRole role;
}
