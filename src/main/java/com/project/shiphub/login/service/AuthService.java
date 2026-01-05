package com.project.shiphub.login.service;

import com.project.shiphub.login.dto.LoginRequest;
import com.project.shiphub.login.dto.LoginResponse;
import com.project.shiphub.login.model.User;
import com.project.shiphub.login.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private LoginRepository loginRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse autenticar(LoginRequest loginRequest) throws Exception {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            throw new Exception("E-mail é obrigatório");
        }

        if (loginRequest.getSenha() == null || loginRequest.getSenha().isEmpty()) {
            throw new Exception("Senha é obrigatória");
        }

        Optional<User> usuarioOptional = loginRepository.findByEmail(loginRequest.getEmail());

        if(!usuarioOptional.isPresent()) {
            throw new Exception("E-mail ou senha incorretos");
        }

        User user = usuarioOptional.get();

        if (!passwordEncoder.matches(loginRequest.getSenha(), user.getPassword())) {

            throw new Exception("E-mail ou senha incorretos");
        }

        LoginResponse response = new LoginResponse();

        response.setMensagem("Login realizado com sucesso!");
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());

        return response;
    }

}
