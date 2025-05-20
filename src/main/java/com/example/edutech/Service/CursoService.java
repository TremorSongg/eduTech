package com.example.edutech.Service;

import com.example.edutech.Model.Curso;
import com.example.edutech.Repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> obtenerCursos() {
        // return cursoRepository.findAll();
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }

    public Optional<Curso> buscarPorId(Long id) {
        // return cursoRepository.findById(id);
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }

    public Curso guardar(Curso curso) {
        // return cursoRepository.save(curso);
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }

    public Curso actualizar(Curso cursoActualizado) {
        return cursoRepository.actualizar(cursoActualizado);
    }

    public void eliminar(Long id) {
        // cursoRepository.deleteById(id);
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }

    public Curso actualizarStock(Curso c) {
        return cursoRepository.controlStock(c);
    }

}
