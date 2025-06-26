package com.example.edutech.Controller;

import com.example.edutech.Model.Curso;
import com.example.edutech.Service.ControlStockService;
import com.example.edutech.assemblers.ControlStockModelAssembler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/Stock")
@Tag(name = "Control de Stock", description = "Operaciones relacionadas con el control de stock de cursos")
public class ControlStockControllerV2 {

    @Autowired
    private ControlStockService controlStockService;

    @Autowired
    private ControlStockModelAssembler controlStockModelAssembler;

    @Operation(summary = "Mostrar cursos", description = "Obtiene una lista de todos los cursos disponibles")
    @GetMapping(value = "/mostrarCursos", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<EntityModel<Curso>>> mostrarCursos() {
        List<EntityModel<Curso>> cursos = controlStockService.mostrarCursos().stream()
            .map(controlStockModelAssembler::toModel)
            .collect(Collectors.toList());

        return ResponseEntity.ok(cursos);
    }

    @Operation(summary = "Controlar stock de curso", description = "Actualiza el stock de un curso específico")
    @PostMapping(value = "/controlStock/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Curso>> controlarStock(@PathVariable int id) {
        try {
            Curso actualizado = controlStockService.controlStock(id);
            return ResponseEntity.ok(controlStockModelAssembler.toModel(actualizado));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Buscar curso por ID", description = "Busca un curso específico por su ID")
    @PostMapping(value = "/buscarCurso", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Curso>> buscarCurso(@RequestBody int id) {
        Optional<Curso> cursoOpt = controlStockService.buscarCurso(id);
        return cursoOpt.map(curso -> ResponseEntity.ok(controlStockModelAssembler.toModel(curso)))
                       .orElse(ResponseEntity.notFound().build());
    }
}