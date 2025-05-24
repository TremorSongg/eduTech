package com.example.edutech.Service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.example.edutech.Model.CarritoItem;
import com.example.edutech.Model.Curso;

@Service
public class CarritoService {

    private Map<Integer, CarritoItem> carrito = new HashMap<>();

    private final CursoService cursoService;

    public CarritoService(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    public void agregarCurso(Curso curso) {
    int cursoId = curso.getId();
    Curso cursoActual = cursoService.buscarPorId(cursoId)
                          .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

    carrito.compute(cursoId, (id, item) -> {
        int cantidadActualEnCarrito = (item != null) ? item.getCantidad() : 0;

        if (cantidadActualEnCarrito >= cursoActual.getCupos()) {
            throw new IllegalStateException("No hay suficientes cupos disponibles para este curso");
        }

        // Descontar stock en el modelo original
        cursoActual.setCupos(cursoActual.getCupos() - 1);
        cursoService.guardarCurso(cursoActual);

        if (item == null) {
            return new CarritoItem(cursoId, cursoActual.getNombre(), 1, cursoActual.getPrecio());
        } else {
            item.setCantidad(cantidadActualEnCarrito + 1); // recalcula subtotal internamente
            return item;
        }
    });
    }

    public void eliminarCurso(int cursoId) {
        CarritoItem eliminado = carrito.remove(cursoId);

        if (eliminado != null) {
            Curso curso = cursoService.buscarPorId(cursoId).orElse(null);
            if (curso != null) {
                curso.setCupos(curso.getCupos() + eliminado.getCantidad());
                cursoService.guardarCurso(curso);
            }
        }
    }

    public Collection<CarritoItem> obtenerItems() {
        return carrito.values();
    }

    public double calcularTotal() {
        return carrito.values().stream()
            .mapToDouble(CarritoItem::getSubtotal)
            .sum();
    }

    public void vaciarCarrito() {
        for (CarritoItem item : carrito.values()) {
            Curso curso = cursoService.buscarPorId(item.getCursoId()).orElse(null);
            if (curso != null) {
                curso.setCupos(curso.getCupos() + item.getCantidad());
                cursoService.guardarCurso(curso);
            }
        }
        carrito.clear();
    }
}
