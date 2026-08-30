package com.igor.EcoPathAPI.controller;

import com.igor.EcoPathAPI.dto.user.LoginInput;
import com.igor.EcoPathAPI.dto.user.LoginResponse;
import com.igor.EcoPathAPI.dto.user.RegisterInput;
import com.igor.EcoPathAPI.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterInput input) {
            userService.register(input);
        }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody @Valid LoginInput input){
        return userService.login(input);
    }


}
