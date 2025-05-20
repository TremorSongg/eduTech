package com.example.edutech.Model;

import java.util.Optional;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String password;

    public static Optional<Usuario> map(Object o){
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }
}
