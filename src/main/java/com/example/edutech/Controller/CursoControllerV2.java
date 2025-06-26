package com.example.edutech.Controller;

import com.example.edutech.Model.Curso;
import com.example.edutech.Service.CursoService;
import com.example.edutech.assemblers.CursoModelAssembler;
import com.example.edutech.assemblers.CarritoItemModelAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

//Assembler para HATEOAS
import com.example.edutech.assemblers.CursoModelAssembler;
//clases necesarias para HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
//clases HATEOAS EntityModel y CollectionModel (que va dentro del Mediatype) para manejar los modelos de return
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
//respuestas de responsEntitypara manejar las respuestas HTTP
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v2/cursos")
@Tag(name = "Cursos", description = "Operaciones relacionadas con la gestión de cursos")
public class CursoControllerV2 {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CursoModelAssembler assembler;

    @Operation(summary = "Obtener todos los cursos", description = "Devuelve una lista de todos los cursos disponibles")
    @GetMapping(name = "obtenerCursos", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Curso>> obtenerCursos() {
        List<EntityModel<Curso>> cursos = cursoService.obtenerCursos().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(cursos,
            linkTo(methodOn(CursoControllerV2.class).obtenerCursos()).withSelfRel());
    }

    @Operation(summary = "Obtener curso por ID", description = "Devuelve un curso específico por su ID")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Curso>> obtenerCursoPorId(@PathVariable int id) {
        Curso curso = cursoService.buscarPorId(id).orElseThrow();
        EntityModel<Curso> cursoModel = assembler.toModel(curso);
        return ResponseEntity.ok(cursoModel);
    }

    @Operation(summary = "Crear un nuevo curso", description = "Permite crear un nuevo curso en la plataforma")
    @PostMapping
    public Curso crearCurso(@RequestBody Curso curso) {
        return cursoService.guardar(curso);
    }

    @Operation(summary = "Actualizar curso", description = "Permite actualizar un curso existente por su ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Curso> actualizarCurso(@PathVariable Integer id, @RequestBody Curso cursoActualizado) {
        return cursoService.buscarPorId(id)
            .map(curso -> {
                curso.setNombre(cursoActualizado.getNombre());
                curso.setDescripcion(cursoActualizado.getDescripcion());
                curso.setPrecio(cursoActualizado.getPrecio());
                curso.setCupos(cursoActualizado.getCupos());
                cursoService.guardar(curso);
                return ResponseEntity.ok(curso);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar curso", description = "Permite eliminar un curso por su ID")
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminarCurso(@PathVariable int id) {
        cursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
