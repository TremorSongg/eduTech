package com.example.edutech.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor 
@NoArgsConstructor 
@Entity
@Data
public class ReporteIncidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int usuarioId;

    private String mensaje;
//Esto hace que se tome el enum como string en la base de datos
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;
    //Para mostrar la fecha de creación
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}