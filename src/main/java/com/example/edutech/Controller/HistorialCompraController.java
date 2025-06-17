package com.example.edutech.Controller;

import com.example.edutech.Model.HistorialCompra;
import com.example.edutech.Service.HistorialCompraService;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/historial")
@Tag(name = "Historial de Compras", description = "Operaciones relacionadas con el historial de compras de los usuarios")
public class HistorialCompraController {
    
    private final HistorialCompraService historialCompraService;

    // Constructor para inyectar el servicio de historial de compras
    // Aqui iba un autowired, pero al ser final no es necesario
    public HistorialCompraController(HistorialCompraService historialCompraService) {
        this.historialCompraService = historialCompraService;
    }

    @Operation(summary = "Obtener historial de compras", description = "Devuelve una lista de todas las compras realizadas por los usuarios")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<HistorialCompra>> obtenerHistorialPorUsuario(
            @PathVariable int usuarioId) {
        List<HistorialCompra> historial = historialCompraService.obtenerHistorialPorUsuarioId(usuarioId);
        return ResponseEntity.ok(historial);
    }
}
