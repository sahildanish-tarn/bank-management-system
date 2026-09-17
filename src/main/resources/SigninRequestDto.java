package com.jsp.bank_management_system.dto.userDtos;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SigninRequestDto {

    @NotEmpty
   private String username;

    @NotEmpty
   private String password;
}
