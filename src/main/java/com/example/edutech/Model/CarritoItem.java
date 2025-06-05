package com.example.edutech.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Version;

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
    private int id;
    private int cursoId;
    private String nombre;
    private int cantidad;
    private double precio;
    private int usuarioId; // Lo agregue para poder darle persistencia al carrito y asi enlazarlo con el usuario que hara la compra
    private double subtotal;

    @Version
    private Integer version = 0;

    // Constructor para crear un nuevo item en el carrito
    public CarritoItem(int cursoId, String nombre, int cantidad, double precio, int usuarioId) {
    this.cursoId = cursoId;
    this.nombre = nombre;
    this.cantidad = cantidad;
    this.precio = precio;
    this.usuarioId = usuarioId;
    this.subtotal = cantidad * precio;
    this.version = 0; // Inicializa la versión
    }

    public CarritoItem(int cursoId, String nombre, int cantidad, double precio) {
    this.cursoId = cursoId;
    this.nombre = nombre;
    this.cantidad = cantidad;
    this.precio = precio;
    }

    // Actualiza el subtotal al cambiar la cantidad
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = cantidad * precio;
    }
}

