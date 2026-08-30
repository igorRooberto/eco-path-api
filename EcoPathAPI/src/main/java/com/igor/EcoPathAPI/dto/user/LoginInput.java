package com.igor.EcoPathAPI.dto.user;

import jakarta.validation.constraints.NotBlank;

public record LoginInput(@NotBlank String userName, @NotBlank String password) {
}
