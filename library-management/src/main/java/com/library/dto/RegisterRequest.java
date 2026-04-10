package com.library.dto;

import com.library.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private Role role; // Optional: In a real app we might secure this so not anyone can register as ADMIN
}
