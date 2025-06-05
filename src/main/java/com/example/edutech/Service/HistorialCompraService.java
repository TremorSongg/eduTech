package com.example.edutech.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edutech.Model.HistorialCompra;
import com.example.edutech.Repository.HistorialCompraRepository;


@Service
public class HistorialCompraService {
    
    private final HistorialCompraRepository historialCompraRepository;

    @Autowired
    public HistorialCompraService(HistorialCompraRepository historialCompraRepository) {
        this.historialCompraRepository = historialCompraRepository;
    }

    public List<HistorialCompra> obtenerHistorialPorUsuarioId(int usuarioId) {
        return historialCompraRepository.findByUsuarioIdOrderByFechaCompraDesc(usuarioId);
    }

    public void guardarCompra(HistorialCompra compra) {
        historialCompraRepository.save(compra);
    }

    public void guardarCompras(List<HistorialCompra> compras) {
        historialCompraRepository.saveAll(compras);
    }
}
