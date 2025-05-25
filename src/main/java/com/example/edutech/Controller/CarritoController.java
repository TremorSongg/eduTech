package com.example.edutech.Controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.edutech.Model.CarritoItem;
import com.example.edutech.Model.Curso;
import com.example.edutech.Service.CarritoService;
import com.example.edutech.Service.CursoService;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CursoService cursoService;

    @PostMapping("/comprar")
public ResponseEntity<?> finalizarCompra() {
    try {
        // Obtener items del carrito
        Collection<CarritoItem> items = carritoService.obtenerItems();
        
        if (items.isEmpty()) {
            return ResponseEntity.badRequest().body("El carrito está vacío");
        }
        
        // Procesar compra
        carritoService.finalizarCompra();
        
        // Respuesta simple
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Compra realizada con éxito. ¡Gracias por su compra!");
        response.put("total", carritoService.calcularTotal());
        
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Error al procesar la compra: " + e.getMessage());
        }
    }

    @PostMapping("/agregar/{id}")
    public ResponseEntity<?> agregarCurso(@PathVariable Integer id) {
        Curso curso = cursoService.buscarPorId(id).orElse(null);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            carritoService.agregarCurso(curso);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Curso agregado al carrito");
            response.put("carrito", carritoService.obtenerItems());
            response.put("total", carritoService.calcularTotal());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body("No hay cupos disponibles para este curso");
        }
    }

    @GetMapping
    public ResponseEntity<?> verCarrito() {
    Collection<CarritoItem> items = carritoService.obtenerItems();
    double total = carritoService.calcularTotal();
    
    // Crear lista de items con la estructura esperada por el frontend
    List<Map<String, Object>> itemsResponse = items.stream().map(item -> {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("cursoId", item.getCursoId());
        itemMap.put("nombre", item.getNombre());
        itemMap.put("cantidad", item.getCantidad());
        itemMap.put("precio", item.getPrecio()); // Asegurar que el precio unitario se envía
        itemMap.put("precioUnitario", item.getPrecio()); // Alternativa para compatibilidad
        itemMap.put("subtotal", item.getSubtotal());
        return itemMap;
    }).collect(Collectors.toList());
    
    Map<String, Object> response = new HashMap<>();
    response.put("items", itemsResponse);
    response.put("total", total);
    return ResponseEntity.ok(response);
}

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarItem(@PathVariable Integer id) {
        carritoService.eliminarCurso(id);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Curso eliminado del carrito");
        response.put("carrito", carritoService.obtenerItems());
        response.put("total", carritoService.calcularTotal());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total")
    public ResponseEntity<Double> obtenerTotal() {
        return ResponseEntity.ok(carritoService.calcularTotal());
    }

    @DeleteMapping("/vaciar")
    public ResponseEntity<?> vaciarCarrito() {
        carritoService.vaciarCarrito();
        return ResponseEntity.ok("Carrito vaciado correctamente");
    }
}
