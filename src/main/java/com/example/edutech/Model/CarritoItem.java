package com.example.edutech.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class CarritoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cursoId;
    private String nombre;
    private int cantidad;
    private double precio;
    private double subtotal;

    // Constructor para crear un nuevo item en el carrito
    public CarritoItem(int cursoId, String nombre, int cantidad, double precio) {
        this.cursoId = cursoId;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = cantidad * precio;
    }

    // Actualiza el subtotal al cambiar la cantidad
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = cantidad * precio;
    }

    
}

