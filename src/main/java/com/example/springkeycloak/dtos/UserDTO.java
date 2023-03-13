package com.example.springkeycloak.dtos;

import com.example.springkeycloak.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserDTO {

    @NotBlank
    private String firstName;
    private String lastName;
    @NotBlank
    private String username;
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private UserRole role;
}
