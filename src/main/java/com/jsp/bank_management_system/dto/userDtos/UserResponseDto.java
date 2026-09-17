package com.jsp.bank_management_system.dto.userDtos;

import com.jsp.bank_management_system.enums.UserStatus;
import lombok.Data;

@Data
public class UserResponseDto {

    private Long  id;
    private String username;
    private String role;
    private UserStatus status;
}
