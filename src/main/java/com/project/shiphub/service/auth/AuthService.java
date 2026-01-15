package com.project.shiphub.service.auth;

import com.project.shiphub.dto.auth.LoginRequest;
import com.project.shiphub.dto.auth.LoginResponse;
import com.project.shiphub.dto.auth.RegisterRequest;
import com.project.shiphub.dto.auth.RegisterResponse;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.repository.auth.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            throw new Exception("Senha é obrigatória");
        }

        Optional<User> usuarioOptional = loginRepository.findByEmail(loginRequest.getEmail());

        if(!usuarioOptional.isPresent()) {
            throw new Exception("E-mail ou senha incorretos");
        }

        User user = usuarioOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            throw new Exception("E-mail ou senha incorretos");
        }

        LoginResponse response = new LoginResponse();

        response.setMensagem("Login realizado com sucesso!");
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());

        return response;
    }

    public RegisterResponse cadastrar(RegisterRequest request) throws Exception {

        if (request.getName() == null || request.getName().isEmpty()) {
            throw new Exception("Nome é obrigatório");
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new Exception("E-mail é obrigatório");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new Exception("Senha é obrigatório");
        }

        if (loginRepository.existsByEmail(request.getEmail())) {
            throw new Exception("E-mail já cadastrado");
        }

        User user = new User ();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword((passwordEncoder.encode(request.getPassword())));
        user.setDataCriacao(LocalDateTime.now());

        User savedUser = loginRepository.save(user);

        return new RegisterResponse(
                "Usuário cadastrado com sucesso!", savedUser.getName(), savedUser.getId(), savedUser.getEmail()
        );
    }

}
