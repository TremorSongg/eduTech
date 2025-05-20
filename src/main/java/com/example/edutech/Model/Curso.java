package com.example.edutech.Model;

import java.util.Optional;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor 
@NoArgsConstructor 
@Entity
@Data

public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String descripcion;
    private int cupos;
    private Double precio;

    public static Optional<Curso> map(Object o) {
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }
    
}
