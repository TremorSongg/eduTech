package com.example.edutech.Model;


import jakarta.persistence.Entity;
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
public class Notificacion {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;
    private int usuarioId;
    private String mensaje; 


}