package com.example.edutech.Controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/agregar/{id}")
    public ResponseEntity<?> agregarCurso(@PathVariable Integer id) {
        Curso curso = cursoService.buscarPorId(id).orElse(null);
        if (curso != null) {
            carritoService.agregarCurso(curso);
            return ResponseEntity.ok("Curso agregado al carrito");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public Collection<CarritoItem> verCarrito() {
        return carritoService.obtenerItems();
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarItem(@PathVariable Integer id) {
        carritoService.eliminarCurso(id);
    }

    @GetMapping("/total")
    public double obtenerTotal() {
        return carritoService.calcularTotal();
    }

    @DeleteMapping("/vaciar")
    public void vaciarCarrito() {
        carritoService.vaciarCarrito();
    }
}
