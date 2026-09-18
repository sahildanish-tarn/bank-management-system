package com.jsp.bank_management_system.controller;

import com.jsp.bank_management_system.dto.userDtos.*;
import com.jsp.bank_management_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

  private final   UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody @Valid SignupUserRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody @Valid UpdateUserRequestDto requestDto) {
        return userService.updateUser(requestDto);
    }

   @PostMapping("/signin")
    public ResponseEntity<UserSigninResponseDto> singin(@RequestBody @Valid SigninRequestDto requestDto) {
        return userService.singin(requestDto);
    }

  @PatchMapping
    public ResponseEntity<UserResponseDto> updatePassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto) {
        return userService.updatePassword(updatePasswordDto);
    }

   @DeleteMapping
    public ResponseEntity<UserResponseDto> deleteByUsername(@RequestHeader String username) {
        return userService.deleteByUsername(username);
    }

@GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable long id) {
        return userService.findById(id);
    }

 @GetMapping
    public ResponseEntity<UserResponseDto> findByUsername( @RequestHeader String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return userService.findAll();
    }
}
