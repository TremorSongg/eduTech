package com.example.edutech.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edutech.Model.Curso;
import com.example.edutech.Repository.CursoRepository;

@Service
public class ControlStockService {
    @Autowired
    private CursoRepository cursoRepository;

    public Curso controlStock(int id) {
        Optional<Curso> optionalCurso = cursoRepository.findById(id);
        if (optionalCurso.isPresent()) {
            Curso cursoExistente = optionalCurso.get();
            int cuposActuales = cursoExistente.getCupos();
    
            if (cuposActuales > 0) {
                cursoExistente.setCupos(cuposActuales - 1);
                return cursoRepository.save(cursoExistente);
            } else {
                throw new IllegalStateException("No hay cupos disponibles.");
            }
        } else {
            return null;
        }
    }
}
