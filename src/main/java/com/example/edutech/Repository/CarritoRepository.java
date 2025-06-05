package com.example.edutech.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.edutech.Model.CarritoItem;

public interface CarritoRepository extends JpaRepository<CarritoItem, Integer> {
    List<CarritoItem> findByUsuarioId(int usuarioId);
    void deleteByUsuarioId(int usuarioId);
    Optional<CarritoItem> findByUsuarioIdAndCursoId(int usuarioId, int cursoId);
  

}

