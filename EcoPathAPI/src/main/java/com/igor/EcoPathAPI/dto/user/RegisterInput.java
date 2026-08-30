package com.igor.EcoPathAPI.dto.user;

import jakarta.validation.constraints.NotBlank;

public record RegisterInput(@NotBlank String userName , @NotBlank String password) {
}
