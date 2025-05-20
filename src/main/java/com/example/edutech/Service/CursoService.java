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

    public Curso actualizar(Long id, Curso cursoActualizado) {
        // Optional<Curso> optionalCurso = cursoRepository.findById(id);
        // if (optionalCurso.isPresent()) {
        //     Curso cursoExistente = optionalCurso.get();
        //     cursoExistente.setNombre(cursoActualizado.getNombre());
        //     cursoExistente.setDescripcion(cursoActualizado.getDescripcion());
        //     cursoExistente.setCupos(cursoActualizado.getCupos());
        //     return cursoRepository.save(cursoExistente);
        // } else {
        //     return null;
        // }
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }

    public void eliminar(Long id) {
        // cursoRepository.deleteById(id);
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }

    public Curso actualizarStock(Curso c) {
        return cursoRepository.controlStock(c);
    }

}
