package com.example.edutech.Controller;

import com.example.edutech.Model.Curso;
import com.example.edutech.Service.ControlStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cursos")
public class ControlStockController {
    @Autowired
    private ControlStockService controlStockService;

    @PostMapping("/controlStock/{id}")
        public ResponseEntity<Curso> controlarStock(@PathVariable int id) {
            try {
                Curso actualizado = controlStockService.controlStock(id);
                return ResponseEntity.ok(actualizado);
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
