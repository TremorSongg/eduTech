package com.example.edutech.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.edutech.Model.CarritoItem;
import com.example.edutech.Model.Curso;
import com.example.edutech.Service.CarritoService;
import com.example.edutech.Service.CursoService;
import com.example.edutech.assemblers.CarritoItemModelAssembler;

import java.util.*;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/carrito")
@CrossOrigin
@Tag(name = "Carrito", description = "Operaciones relacionadas con el carrito de compras")
public class CarritoControllerV2 {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CarritoItemModelAssembler carritoItemModelAssembler;

    @Operation(summary = "Finalizar compra", description = "Permite finalizar la compra de los cursos en el carrito del usuario")
    @PostMapping(value = "/finalizar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> finalizarCompra(@RequestParam int usuarioId) {
        try {
            List<CarritoItem> items = carritoService.obtenerItems(usuarioId);
            if (items.isEmpty()) {
                return ResponseEntity.badRequest().body("El carrito está vacío");
            }

            double total = carritoService.calcularTotal(usuarioId);
            carritoService.finalizarCompra(usuarioId);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Compra realizada con éxito. ¡Gracias por su compra!");
            response.put("total", total);
            response.put("carrito", items.stream()
                    .map(carritoItemModelAssembler::toModel)
                    .collect(Collectors.toList()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Agregar curso al carrito", description = "Permite agregar un curso al carrito de compras del usuario")
    @PostMapping(value = "/agregar/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> agregarCurso(@PathVariable Integer id, @RequestParam int usuarioId) {
        Curso curso = cursoService.buscarPorId(id).orElse(null);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            carritoService.agregarCurso(curso, usuarioId);

            List<CarritoItem> items = carritoService.obtenerItems(usuarioId);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Curso agregado al carrito");
            response.put("carrito", items.stream()
                    .map(carritoItemModelAssembler::toModel)
                    .collect(Collectors.toList()));
            response.put("total", carritoService.calcularTotal(usuarioId));

            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("No hay cupos disponibles");
        }
    }

    @Operation(summary = "Ver carrito", description = "Permite ver los cursos agregados al carrito de compras del usuario")
    @GetMapping(value = "/ver", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> verCarrito(@RequestParam int usuarioId) {
        Collection<CarritoItem> items = carritoService.obtenerItems(usuarioId);
        double total = carritoService.calcularTotal(usuarioId);

        List<EntityModel<CarritoItem>> itemsResponse = items.stream()
                .map(carritoItemModelAssembler::toModel)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("items", itemsResponse);
        response.put("total", total);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar curso del carrito", description = "Permite eliminar un curso del carrito de compras del usuario")
    @DeleteMapping(value = "/eliminar/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarItem(@PathVariable Integer id, @RequestParam int usuarioId) {
        carritoService.eliminarCurso(id, usuarioId);

        List<CarritoItem> items = carritoService.obtenerItems(usuarioId);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Curso eliminado del carrito");
        response.put("carrito", items.stream()
                .map(carritoItemModelAssembler::toModel)
                .collect(Collectors.toList()));
        response.put("total", carritoService.calcularTotal(usuarioId));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener total del carrito", description = "Permite obtener el total de los cursos en el carrito de compras del usuario")
    @GetMapping(value = "/total", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Double> obtenerTotal(@RequestParam int usuarioId) {
        return ResponseEntity.ok(carritoService.calcularTotal(usuarioId));
    }

    @Operation(summary = "Vaciar carrito", description = "Permite vaciar todos los cursos del carrito de compras del usuario")
    @DeleteMapping(value = "/vaciar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> vaciarCarrito(@RequestParam int usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.ok("Carrito vaciado correctamente");
    }

    @Operation(summary = "Actualizar cantidad de curso en el carrito", description = "Permite actualizar la cantidad de un curso en el carrito de compras del usuario")
    @PutMapping(value = "/actualizar/{id}", produces = MediaTypes.HAL_JSON_VALUE)
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
