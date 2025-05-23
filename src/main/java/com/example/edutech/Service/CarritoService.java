package com.example.edutech.Service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.example.edutech.Model.CarritoItem;
import com.example.edutech.Model.Curso;

@Service
public class CarritoService {
    private Map<Integer, CarritoItem> carrito = new HashMap<>();

    public void agregarCurso(Curso curso) {
    carrito.compute(curso.getId(), (cursoId, item) -> { // cursoID esta dando problemas y le puse ignorar, asi de choro
        if (item == null) {
            return new CarritoItem(curso.getId(), curso.getNombre(), 1, curso.getPrecio());
        } else {
            item.setCantidad(item.getCantidad() + 1);
            return item;
        }
        });
    }

    public void eliminarCurso(int cursoId) {
        carrito.remove(cursoId);
    }

    public Collection<CarritoItem> obtenerItems() {
        return carrito.values();
    }

    public double calcularTotal() {
        return carrito.values().stream().mapToDouble(CarritoItem::getSubtotal).sum();
    }

    public void vaciarCarrito() {
        carrito.clear();
    }
}
