package com.example.edutech.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Map<String, Object> response = new HashMap<>();
        response.put("items", carritoService.obtenerItems());
        response.put("total", carritoService.calcularTotal());
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
