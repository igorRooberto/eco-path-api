package com.igor.EcoPathAPI.service;

import com.igor.EcoPathAPI.dto.user.LoginInput;
import com.igor.EcoPathAPI.dto.user.LoginResponse;
import com.igor.EcoPathAPI.dto.user.RegisterInput;
import com.igor.EcoPathAPI.entites.User;
import com.igor.EcoPathAPI.exception.base.BadRequestException;
import com.igor.EcoPathAPI.exception.base.ConflictException;
import com.igor.EcoPathAPI.repository.UserRepository;
import com.igor.EcoPathAPI.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public void register(RegisterInput input){

        if(userRepository.existsByUserName(input.userName())){
            throw new ConflictException("Usuário já cadastrado com esse nome de usuário.");
        }

        String encryptedPassword = passwordEncoder.encode(input.password());

        User newUser = User.builder()
                .userName(input.userName())
                .password(encryptedPassword)
                .active(true)
                .build();

        userRepository.save(newUser);
    }

    public LoginResponse login(LoginInput input){
        User user = userRepository.findByUserName(input.userName())
                .orElseThrow(() ->  new BadRequestException("Usuário ou senha inválidos."));

        if(!passwordEncoder.matches(input.password(), user.getPassword())){
            throw new BadRequestException("Usuário ou senha inválidos.");
        }

        String token = tokenService.generateToken(user);

        return new LoginResponse(token);
    }

}
