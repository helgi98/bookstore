package org.example.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.JwtTokenDTO;
import org.example.bookstore.dto.UserDTO;
import org.example.bookstore.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("login")
    public JwtTokenDTO login(@RequestBody @Valid UserDTO.LoginUser loginUserRequest) {
        return userService.loginUser(loginUserRequest);
    }

    @PostMapping("signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Valid UserDTO.SignupUser signupUserRequest) {
        userService.signupUser(signupUserRequest);
    }

}
