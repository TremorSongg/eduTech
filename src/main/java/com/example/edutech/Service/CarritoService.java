package com.example.edutech.Service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.example.edutech.Model.CarritoItem;
import com.example.edutech.Model.Curso;

import jakarta.transaction.Transactional;

@Service
public class CarritoService {

    private Map<Integer, CarritoItem> carrito = new HashMap<>();

    private final CursoService cursoService;

    public CarritoService(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    // Agregar un curso al carrito
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
    // Asegurarse que cada item tenga el precio unitario
    carrito.values().forEach(item -> {
        Curso curso = cursoService.buscarPorId(item.getCursoId()).orElse(null);
        if (curso != null) {
            item.setPrecio(curso.getPrecio()); // Asegurar que el precio esté actualizado
            item.setSubtotal(item.getCantidad() * item.getPrecio()); // Recalcular subtotal
        }
    });
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

    @Transactional
public void finalizarCompra() {
    // 1. Verificar y actualizar cupos de cada curso
    for (CarritoItem item : carrito.values()) {
        Curso curso = cursoService.buscarPorId(item.getCursoId())
                          .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
        
        // Validar que haya suficiente cupo (aunque ya se validó al agregar)
        if (curso.getCupos() < item.getCantidad()) {
            throw new IllegalStateException("No hay suficientes cupos para: " + curso.getNombre());
        }
        
        // Actualizar cupos (no necesitamos guardar aquí porque ya está en el método agregar)
    }
    
    // 2. Vaciar el carrito (esto ya devuelve los cupos, así que lo modificamos)
    // Primero guardamos el total para la respuesta
    double totalCompra = calcularTotal(); // Dice que no se usa pero si se usa
    
    // Limpiar sin devolver cupos
    carrito.clear();
    }

    public void actualizarCantidad(Integer cursoId, int nuevaCantidad) {
    CarritoItem item = carrito.get(cursoId.intValue());

    if (item == null) {
        throw new NoSuchElementException("El curso no está en el carrito");
    }

    Curso curso = cursoService.buscarPorId(cursoId.intValue())
                    .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

    int diferencia = nuevaCantidad - item.getCantidad();

    if (diferencia > 0 && diferencia > curso.getCupos()) {
        throw new IllegalStateException("No hay suficientes cupos disponibles");
    }

    // Ajustar cupos
    curso.setCupos(curso.getCupos() - diferencia);
    cursoService.guardarCurso(curso);

    // Actualizar item
    item.setCantidad(nuevaCantidad);
    item.setSubtotal(item.getPrecio() * nuevaCantidad);
}

}
