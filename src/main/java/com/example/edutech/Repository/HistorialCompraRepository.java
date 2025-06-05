package com.example.edutech.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.edutech.Model.HistorialCompra;


public interface HistorialCompraRepository extends JpaRepository<HistorialCompra, Long> {
    List<HistorialCompra> findByUsuarioIdOrderByFechaCompraDesc(int usuarioId);
}
