package com.jsp.bank_management_system.service.imp;

import com.jsp.bank_management_system.dto.userDtos.*;
import com.jsp.bank_management_system.entity.User;
import com.jsp.bank_management_system.exception.InvalidEmailException;
import com.jsp.bank_management_system.exception.InvalidPasswordException;
import com.jsp.bank_management_system.exception.UserNotfound;
import com.jsp.bank_management_system.exception.UserWithUsernameAlreadyExists;
import com.jsp.bank_management_system.repository.UserRepository;
import com.jsp.bank_management_system.security.JwtService;
import com.jsp.bank_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<UserResponseDto> signup(SignupUserRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new UserWithUsernameAlreadyExists(requestDto.getUsername());
        }
        System.out.println(requestDto +"requestDto");
        User user=objectMapper.convertValue(requestDto, User.class);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user= userRepository.save( user);
        System.out.println(user +"user");
        return ResponseEntity.status(HttpStatus.CREATED).body(objectMapper.convertValue(user,UserResponseDto.class));
    }

    @Override
    public ResponseEntity<UserResponseDto> updateUser(UpdateUserRequestDto requestDto) {
        if(userRepository.existsById(requestDto.getId())) {
            User user= userRepository.save( objectMapper.convertValue(requestDto, User.class));

            return ResponseEntity.ok(objectMapper.convertValue(user,UserResponseDto.class));
        }
        throw new UserNotfound("user not found");
    }

    @Override
    public ResponseEntity<UserSigninResponseDto> singin(SigninRequestDto requestDto) {
        User user = userRepository
                .findByUsername(requestDto.getUsername())
                .orElseThrow(
                        () -> new InvalidEmailException("Invalid username")
                );
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                requestDto.getUsername(),
                                requestDto.getPassword()
                        )
                );
        String token = jwtService.generateToken(
                authentication.getName()
        );

//        if ( ! passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
//            throw new InvalidPasswordException("Invalid password");
//        }
        UserSigninResponseDto response =
                objectMapper.convertValue(
                        user,
                        UserSigninResponseDto.class
                );

        response.setToken(token);

        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<UserResponseDto> updatePassword(UpdatePasswordDto updatePasswordDto) {
      User user=  userRepository.findByUsername(updatePasswordDto.getUsername()).orElseThrow(
                () -> new UserNotfound("Username not found")
        );
      if(!updatePasswordDto.getOldPassword().equals(user.getPassword())) {
          throw new InvalidPasswordException("Invalid old password");
      }
      if(updatePasswordDto.getNewPassword().equals(user.getPassword())) {
          throw new InvalidPasswordException("New password should not be the same as old password");
      }
      user.setPassword(updatePasswordDto.getNewPassword());
      userRepository.save(user);
        return ResponseEntity.ok(objectMapper.convertValue(user,UserResponseDto.class));
    }

    @Override
    public ResponseEntity<UserResponseDto> deleteByUsername(String username) {

           User user = userRepository.findByUsername(username).orElseThrow(
                    () -> new UserNotfound("Username not found")
            );
           userRepository.delete(user);

        return ResponseEntity.ok(objectMapper.convertValue(user,UserResponseDto.class));
    }

    @Override
    public ResponseEntity<UserResponseDto> findById(long id) {
        User user =userRepository.findById(id).orElseThrow(
                ()-> new UserNotfound("User not found")
        );
        return ResponseEntity.status(HttpStatus.FOUND).body(objectMapper.convertValue(user,UserResponseDto.class));
    }

    @Override
    public ResponseEntity<UserResponseDto> findByUsername(String username) {
            User user=userRepository.findByUsername(username).orElseThrow(
                    ()->new UserNotfound("Username not found")
            );
            return ResponseEntity.status(HttpStatus.FOUND).body(objectMapper.convertValue(user,UserResponseDto.class));
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> findAll() {

        List<UserResponseDto> users=new ArrayList<>();

        for(User user:userRepository.findAll()) {
            users.add(objectMapper.convertValue(user,UserResponseDto.class));
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(users);
    }
}
