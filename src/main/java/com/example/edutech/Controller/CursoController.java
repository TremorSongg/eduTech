package com.example.edutech.Controller;

import com.example.edutech.Model.Curso;
import com.example.edutech.Service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public List<Curso> obtenerCursos() {
        return cursoService.obtenerCursos();
    }

    @GetMapping("/{id}")
    public Curso obtenerCursoPorId(@PathVariable int id) {
        return cursoService.buscarPorId(id).orElse(null);
    }

    @PostMapping
    public Curso crearCurso(@RequestBody Curso curso) {
        return cursoService.guardar(curso);
    }

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


    @DeleteMapping("/{id}")
    public void eliminarCurso(@PathVariable int id) {
        cursoService.eliminar(id);
    }
}
