package com.example.edutech.Controller;

import com.example.edutech.Model.Curso;
import com.example.edutech.Service.ControlStockService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/Stock")
@Tag(name = "Control de Stock", description = "Operaciones relacionadas con el control de stock de cursos")
public class ControlStockControllerV2 {
    @Autowired
    private ControlStockService controlStockService;

    @Operation(summary = "Mostrar cursos", description = "Obtiene una lista de todos los cursos disponibles")
    @GetMapping(value = "/mostrarCursos", produces = MediaTypes.HAL_JSON_VALUE)
    public List<Curso> mostrarCursos() {
        return controlStockService.mostrarCursos();
    }
    
    @Operation(summary = "Controlar stock de curso", description = "Actualiza el stock de un curso específico")
    @PostMapping(value = "/controlStock/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<Curso> controlarStock(@PathVariable int id) {
            try {
                Curso actualizado = controlStockService.controlStock(id);
                return ResponseEntity.ok(actualizado);
            } catch (IllegalStateException e) {
                return null;
        }
    }
    
    @Operation(summary = "Buscar curso por ID", description = "Busca un curso específico por su ID")
    @PostMapping(value = "/buscarCurso", produces = MediaTypes.HAL_JSON_VALUE) // Se reemplazo ("/{id}") por el /buscarCurso
    public Optional<Curso> buscarCurso(@RequestBody int id) {        
        return controlStockService.buscarCurso(id);
    }
    

}
