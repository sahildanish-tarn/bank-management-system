package com.jsp.bank_management_system.service;

import com.jsp.bank_management_system.dto.userDtos.*;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface UserService {

   ResponseEntity<UserResponseDto> signup(SignupUserRequestDto requestDto);

    ResponseEntity<UserResponseDto> updateUser(UpdateUserRequestDto requestDto);
    ResponseEntity<UserSigninResponseDto> singin(SigninRequestDto requestDto);

    ResponseEntity<UserResponseDto> updatePassword(UpdatePasswordDto updatePasswordDto);
    ResponseEntity<UserResponseDto> deleteByUsername(String username);

    ResponseEntity<UserResponseDto> findById(long id);
    ResponseEntity<UserResponseDto> findByUsername(String username);
    ResponseEntity<List<UserResponseDto>> findAll();
}
