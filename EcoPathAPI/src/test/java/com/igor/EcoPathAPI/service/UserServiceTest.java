package com.igor.EcoPathAPI.service;

import com.igor.EcoPathAPI.dto.user.LoginInput;
import com.igor.EcoPathAPI.dto.user.LoginResponse;
import com.igor.EcoPathAPI.dto.user.RegisterInput;
import com.igor.EcoPathAPI.entites.User;
import com.igor.EcoPathAPI.exception.base.BadRequestException;
import com.igor.EcoPathAPI.exception.base.ConflictException;
import com.igor.EcoPathAPI.repository.UserRepository;
import com.igor.EcoPathAPI.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        tokenService = Mockito.mock(TokenService.class);

        userService = new UserService(userRepository, passwordEncoder, tokenService);
    }

    @Test
     void shouldSaveUserWhenDataIsValid(){
        RegisterInput input = new RegisterInput("Igor", "123456");

        when(userRepository.existsByUserName(input.userName())).thenReturn(false);
        when(passwordEncoder.encode(input.password())).thenReturn("password-hash");

        userService.register(input);

        verify(passwordEncoder, times(1)).encode(input.password());
        verify(userRepository, times(1)).save(any(User.class));

     }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists(){
        RegisterInput input = new RegisterInput("Igor", "123456");

        when(userRepository.existsByUserName(input.userName())).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.register(input));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    void shouldLoginUserWhenDataIsValid(){
        LoginInput input = new LoginInput("Igor", "123456");
        User mockUser = User.builder()
                .userName("Igor")
                .password("123456hash")
                .build();

        when(userRepository.findByUserName(input.userName())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(input.password(), mockUser.getPassword())).thenReturn(true);
        when(tokenService.generateToken(mockUser)).thenReturn("JWT_TOKEN");

        LoginResponse response = userService.login(input);

        assertEquals("JWT_TOKEN", response.token());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound(){
        LoginInput input = new LoginInput("Igor", "123456");

        when(userRepository.findByUserName(input.userName())).thenReturn(Optional.empty());

       assertThrows(BadRequestException.class, () -> userService.login(input));

       verify(passwordEncoder, never()).matches(anyString(), anyString());
       verify(tokenService, never()).generateToken(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenPasswordInvalid(){
        LoginInput input = new LoginInput("Igor", "123456");
        User mockUser = User.builder()
                .userName("Igor")
                .password("123456hash")
                .build();

        when(userRepository.findByUserName(input.userName())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(input.password(), mockUser.getPassword())).thenReturn(false);

        assertThrows(BadRequestException.class, () -> userService.login(input));

        verify(tokenService, never()).generateToken(any(User.class));
    }



}
