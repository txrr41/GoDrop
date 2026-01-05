package com.project.shiphub.login.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalDateTime.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long email;

    @Column(nullable = false)
    private Long password;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
}
