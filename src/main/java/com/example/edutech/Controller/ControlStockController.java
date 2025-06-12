package com.example.edutech.Controller;

import com.example.edutech.Model.Curso;
import com.example.edutech.Service.ControlStockService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/Stock")
public class ControlStockController {
    @Autowired
    private ControlStockService controlStockService;

    @GetMapping
    public List<Curso> mostrarCursos() {
        return controlStockService.mostrarCursos();
    }
    

    @PostMapping("/controlStock/{id}")
        public ResponseEntity<Curso> controlarStock(@PathVariable int id) {
            try {
                Curso actualizado = controlStockService.controlStock(id);
                return ResponseEntity.ok(actualizado);
            } catch (IllegalStateException e) {
                return null;
        }
    }
    
    @PostMapping("/{id}")
    public Optional<Curso> buscarCurso(@RequestBody int id) {        
        return controlStockService.buscarCurso(id);
    }
    

}
