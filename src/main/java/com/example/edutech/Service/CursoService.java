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
        return cursoRepository.findAll();
    }

    public Optional<Curso> buscarPorId(int id) {
        return cursoRepository.findById(id);
    }

    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    public Curso actualizar(int id, Curso cursoActualizado) {
        Optional<Curso> optionalCurso = cursoRepository.findById(id);
        if (optionalCurso.isPresent()) {
            Curso cursoExistente = optionalCurso.get();
            cursoExistente.setNombre(cursoActualizado.getNombre());
            cursoExistente.setDescripcion(cursoActualizado.getDescripcion());
            cursoExistente.setCupos(cursoActualizado.getCupos());
            return cursoRepository.save(cursoExistente);
        } else {
            return null;
        }
    }

    public void eliminar(int id) {
        cursoRepository.deleteById(id);
    }

}
