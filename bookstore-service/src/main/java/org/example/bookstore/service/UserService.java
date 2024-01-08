package org.example.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.JwtTokenDTO;
import org.example.bookstore.dto.UserDTO;
import org.example.bookstore.entity.UserEntity;
import org.example.bookstore.exceptions.AuthenticationException;
import org.example.bookstore.exceptions.EntityAlreadyExistsException;
import org.example.bookstore.repo.UserRepo;
import org.example.bookstore.security.model.UserSecurityDetails;
import org.example.bookstore.security.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Transactional
    public void signupUser(UserDTO.SignupUser signupUserRequest) {
        if (userRepo.existsByUserName(signupUserRequest.userName())) {
            throw new EntityAlreadyExistsException("User already exists");
        }

        var passwordHash = passwordEncoder.encode(signupUserRequest.password());
        var user = UserEntity.builder()
            .userName(signupUserRequest.userName())
            .passwordHash(passwordHash)
            .build();
        userRepo.save(user);
    }

    @Transactional(readOnly = true)
    public JwtTokenDTO loginUser(UserDTO.LoginUser loginUserRequest) {
        var user = userRepo.findByUserName(loginUserRequest.userName())
            .orElseThrow(() -> new AuthenticationException("User not found"));
        if (!passwordEncoder.matches(loginUserRequest.password(), user.getPasswordHash())) {
            throw new AuthenticationException("Invalid password");
        }

        var token = jwtService.generateToken(new UserSecurityDetails(user.getId(), user.getUserName()));

        return new JwtTokenDTO(token);
    }

}
