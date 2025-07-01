package com.example.edutech.Controller;

import com.example.edutech.Model.Curso;
import com.example.edutech.Service.ControlStockService;
import com.example.edutech.assemblers.ControlStockModelAssembler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/Stock")
@Tag(name = "Control de Stock", description = "Operaciones relacionadas con el control de stock de cursos")
public class ControlStockControllerV2 {

    @Autowired
    private ControlStockService controlStockService;

    @Autowired
    private ControlStockModelAssembler controlStockModelAssembler;

    @Operation(summary = "Punto de entrada del Control de Stock", description = "Provee enlaces a las operaciones principales del control de stock, indicando los parámetros necesarios.")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getRootStockInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Bienvenido al servicio de Control de Stock. Aquí tienes las operaciones disponibles:");
        
        Map<String, Link> links = new HashMap<>();
        links.put("mostrarCursos", linkTo(methodOn(ControlStockControllerV2.class).mostrarCursos()).withRel("mostrarCursos").withTitle("Obtener la lista de todos los cursos disponibles."));
        links.put("controlarStock", linkTo(methodOn(ControlStockControllerV2.class).controlarStock(0)).withRel("controlarStock").withTitle("Actualizar el stock de un curso específico. Requiere '{id}' (cursoId)."));
        
        response.put("_links", links);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mostrar cursos", description = "Obtiene una lista de todos los cursos disponibles")
    @GetMapping(value = "/mostrarCursos", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> mostrarCursos() {
        try {
            List<Curso> cursosFromService = controlStockService.mostrarCursos();
            
            if (cursosFromService == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "El servicio de control de stock devolvió una lista de cursos nula.");
                errorResponse.put("detalles", "Asegúrate de que 'controlStockService.mostrarCursos()' siempre devuelva una lista no nula (incluso si está vacía).");
                return ResponseEntity.status(500).body(errorResponse);
            }

            List<EntityModel<Curso>> cursos = cursosFromService.stream()
                .map(curso -> {
                    if (curso == null) {
                        System.err.println("Advertencia: Se encontró un curso nulo en la lista del servicio.");
                        return null;
                    }
                    try {
                        return controlStockModelAssembler.toModel(curso);
                    } catch (Exception e) {
                        System.err.println("Error al ensamblar el curso con ID: " + curso.getId() + " - " + e.getMessage());
                        throw new RuntimeException("Falló el ensamblaje del modelo para el curso ID: " + curso.getId(), e);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Ocurrió un error interno al intentar mostrar los cursos.");
            errorResponse.put("detalles", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(errorResponse);
        }
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
