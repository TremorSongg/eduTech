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
    public ResponseEntity<?> finalizarCompra(@RequestParam int usuarioId) {
        try {
            if (carritoService.obtenerItems(usuarioId).isEmpty()) {
                return ResponseEntity.badRequest().body("El carrito está vacío");
            }

            double total = carritoService.calcularTotal(usuarioId);
            carritoService.finalizarCompra(usuarioId);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Compra realizada con éxito. ¡Gracias por su compra!");
            response.put("total", total);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Agrega un curso al carrito
    @PostMapping("/agregar/{id}")
    public ResponseEntity<?> agregarCurso(@PathVariable Integer id, @RequestParam int usuarioId) {
        Curso curso = cursoService.buscarPorId(id).orElse(null);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            carritoService.agregarCurso(curso, usuarioId);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Curso agregado al carrito");
            response.put("carrito", carritoService.obtenerItems(usuarioId));
            response.put("total", carritoService.calcularTotal(usuarioId));

            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("No hay cupos disponibles");
        }
    }

    // Muestra los items del carrito
    @GetMapping
    public ResponseEntity<?> verCarrito(@RequestParam int usuarioId) {
        Collection<CarritoItem> items = carritoService.obtenerItems(usuarioId);
        double total = carritoService.calcularTotal(usuarioId);

        List<Map<String, Object>> itemsResponse = items.stream().map(item -> {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("cursoId", item.getCursoId());
            itemMap.put("nombre", item.getNombre());
            itemMap.put("cantidad", item.getCantidad());
            itemMap.put("precio", item.getPrecio());
            itemMap.put("precioUnitario", item.getPrecio());
            itemMap.put("subtotal", item.getSubtotal());
            return itemMap;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("items", itemsResponse);
        response.put("total", total);
        return ResponseEntity.ok(response);
    }

    // Elimina un item del carrito
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarItem(@PathVariable Integer id, @RequestParam int usuarioId) {
        carritoService.eliminarCurso(id, usuarioId);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Curso eliminado del carrito");
        response.put("carrito", carritoService.obtenerItems(usuarioId));
        response.put("total", carritoService.calcularTotal(usuarioId));

        return ResponseEntity.ok(response);
    }

    // Obtiene el total del carrito
    @GetMapping("/total")
    public ResponseEntity<Double> obtenerTotal(@RequestParam int usuarioId) {
        return ResponseEntity.ok(carritoService.calcularTotal(usuarioId));
    }

    // Vacía todo el carrito
    @DeleteMapping("/vaciar")
    public ResponseEntity<?> vaciarCarrito(@RequestParam int usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.ok("Carrito vaciado correctamente");
    }

    // Actualiza la cantidad de un item
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarCantidad(
            @PathVariable Integer id,
            @RequestParam int usuarioId,
            @RequestBody Map<String, Integer> payload
    ) {
        int cantidad = payload.get("cantidad");
        carritoService.actualizarCantidad(id, cantidad, usuarioId);
        return ResponseEntity.ok().build();
    }
}
