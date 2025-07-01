package com.example.edutech.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edutech.Model.Curso;
import com.example.edutech.Repository.ControlStockRepository;


@Service
public class ControlStockService {
    @Autowired
    private ControlStockRepository controlStockRepository;

    public Curso controlStock(int id) {
        return controlStockRepository.controlStock(id);
    }
 
    public List<Curso> mostrarCursos() {
        return controlStockRepository.mostrarCursos();
    }

    public Optional<Curso> buscarCurso(int id) {
        return controlStockRepository.buscarPorId(id);
    }
}
