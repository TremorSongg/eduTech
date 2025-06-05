package com.example.edutech.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int usuarioId;
    private int cursoId;
    private String nombreCurso;
    private double precio;
    private int cantidad;
    private double subtotal;
    private LocalDateTime fechaCompra;
    
    @PrePersist
    protected void onCreate() {
        fechaCompra = LocalDateTime.now();
    }
}