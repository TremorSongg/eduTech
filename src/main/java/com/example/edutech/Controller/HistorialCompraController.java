package com.example.edutech.Controller;

import com.example.edutech.Model.HistorialCompra;
import com.example.edutech.Service.HistorialCompraService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/historial")
public class HistorialCompraController {
    
    private final HistorialCompraService historialCompraService;

    @Autowired
    public HistorialCompraController(HistorialCompraService historialCompraService) {
        this.historialCompraService = historialCompraService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<HistorialCompra>> obtenerHistorialPorUsuario(
            @PathVariable int usuarioId) {
        List<HistorialCompra> historial = historialCompraService.obtenerHistorialPorUsuarioId(usuarioId);
        return ResponseEntity.ok(historial);
    }
}
