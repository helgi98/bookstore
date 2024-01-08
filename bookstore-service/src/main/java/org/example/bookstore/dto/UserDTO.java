package org.example.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(@NotNull String userName) {
    public record SignupUser(@NotBlank String userName, @NotBlank String password) {
    }

    public record LoginUser(@NotBlank String userName, @NotBlank String password) {
    }
}
