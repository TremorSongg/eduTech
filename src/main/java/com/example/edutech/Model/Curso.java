package com.example.edutech.Model;

import java.util.Optional;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private int cupos;

    public static Optional<Curso> map(Object o) {
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }
    
}
