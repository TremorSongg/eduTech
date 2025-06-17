package com.example.edutech.Controller;

import com.example.edutech.Model.Curso;
import com.example.edutech.Service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
@Tag(name = "Cursos", description = "Operaciones relacionadas con la gestión de cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Operation(summary = "Obtener todos los cursos", description = "Devuelve una lista de todos los cursos disponibles")
    @GetMapping
    public List<Curso> obtenerCursos() {
        return cursoService.obtenerCursos();
    }

    @Operation(summary = "Obtener curso por ID", description = "Devuelve un curso específico por su ID")
    @GetMapping("/{id}")
    public Curso obtenerCursoPorId(@PathVariable int id) {
        return cursoService.buscarPorId(id).orElse(null);
    }

    @Operation(summary = "Crear un nuevo curso", description = "Permite crear un nuevo curso en la plataforma")
    @PostMapping
    public Curso crearCurso(@RequestBody Curso curso) {
        return cursoService.guardar(curso);
    }

    @Operation(summary = "Actualizar curso", description = "Permite actualizar un curso existente por su ID")
    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public void eliminarCurso(@PathVariable int id) {
        cursoService.eliminar(id);
    }
}
