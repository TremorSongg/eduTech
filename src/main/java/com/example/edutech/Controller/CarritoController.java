package com.example.edutech.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.edutech.Model.CarritoItem;
import com.example.edutech.Model.Curso;
import com.example.edutech.Service.CarritoService;
import com.example.edutech.Service.CursoService;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CursoService cursoService;

    // Finalizar la compra del carrito
    @PostMapping("/comprar")
    public ResponseEntity<?> finalizarCompra() {
        try {
            // Verificar si el carrito está vacío
            if (carritoService.obtenerItems().isEmpty()) {
                return ResponseEntity.badRequest().body("El carrito está vacío");
            }
            
            // Procesar la compra
            carritoService.finalizarCompra();
            
            // Crear respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Compra realizada con éxito. ¡Gracias por su compra!");
            response.put("total", carritoService.calcularTotal());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Agrega un curso al carrito
    @PostMapping("/agregar/{id}")
    public ResponseEntity<?> agregarCurso(@PathVariable Integer id) {
        // Buscar el curso por ID
        Curso curso = cursoService.buscarPorId(id).orElse(null);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Agregar curso al carrito
            carritoService.agregarCurso(curso);
            
            // Crear respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Curso agregado al carrito");
            response.put("carrito", carritoService.obtenerItems());
            response.put("total", carritoService.calcularTotal());
            
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("No hay cupos disponibles");
        }
    }

    // Muestra los items del carrito
    @GetMapping
     public ResponseEntity<?> verCarrito() {
    Collection<CarritoItem> items = carritoService.obtenerItems();
    double total = carritoService.calcularTotal();
    
    // Crear lista de items para la respuesta
    List<Map<String, Object>> itemsResponse = items.stream().map(item -> {
        // Map es para crear un nuevo objeto con los datos que queremos enviar al cliente, funcionando como DTO, correspondiendo a un objeto JSON, 
        // se utiliza desde el import java.util.Map;
        // Asegurando que el ID del curso, nombre, cantidad, precio y subtotal se envían
        Map<String, Object> itemMap = new HashMap<>(); 
        itemMap.put("cursoId", item.getCursoId()); 
        itemMap.put("nombre", item.getNombre()); 
        itemMap.put("cantidad", item.getCantidad()); 
        itemMap.put("precio", item.getPrecio()); 
        itemMap.put("precioUnitario", item.getPrecio()); 
        itemMap.put("subtotal", item.getSubtotal()); 
        return itemMap;
    }).collect(Collectors.toList());
    
    // Creamos respuesta al cliente 
    Map<String, Object> response = new HashMap<>();
    response.put("items", itemsResponse);
    response.put("total", total);
    return ResponseEntity.ok(response);
}

    // Elimina un item del carrito
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarItem(@PathVariable Integer id) {
        carritoService.eliminarCurso(id);
        
        // Crear respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Curso eliminado del carrito");
        response.put("carrito", carritoService.obtenerItems());
        response.put("total", carritoService.calcularTotal());
        
        return ResponseEntity.ok(response);
    }

    // Obtiene el total del carrito
    @GetMapping("/total")
    public ResponseEntity<Double> obtenerTotal() {
        return ResponseEntity.ok(carritoService.calcularTotal());
    }

    // Vacía todo el carrito
    @DeleteMapping("/vaciar")
    public ResponseEntity<?> vaciarCarrito() {
        carritoService.vaciarCarrito();
        return ResponseEntity.ok("Carrito vaciado correctamente");
    }

    // Actualiza la cantidad de un item
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarCantidad(@PathVariable Integer id, @RequestBody Map<String, Integer> payload) {
        int cantidad = payload.get("cantidad");
        carritoService.actualizarCantidad(id, cantidad);
        return ResponseEntity.ok().build();
    }
}