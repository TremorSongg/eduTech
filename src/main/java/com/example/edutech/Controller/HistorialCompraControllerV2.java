package com.example.edutech.Controller;

import com.example.edutech.Model.HistorialCompra;
import com.example.edutech.Service.HistorialCompraService;
import com.example.edutech.assemblers.HistorialModelAssembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/historial")
@Tag(name = "Historial de Compras", description = "Operaciones relacionadas con el historial de compras de los usuarios")
public class HistorialCompraControllerV2 {

    private final HistorialCompraService historialCompraService;
    private final HistorialModelAssembler historialModelAssembler;

    public HistorialCompraControllerV2(HistorialCompraService historialCompraService,
                                       HistorialModelAssembler historialModelAssembler) {
        this.historialCompraService = historialCompraService;
        this.historialModelAssembler = historialModelAssembler;
    }

    @Operation(summary = "Obtener historial de compras", description = "Devuelve una lista de todas las compras realizadas por los usuarios")
    @GetMapping(value = "/usuario/{usuarioId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> obtenerHistorialPorUsuario(@PathVariable int usuarioId) {
        List<HistorialCompra> historial = historialCompraService.obtenerHistorialPorUsuarioId(usuarioId);

        List<EntityModel<HistorialCompra>> historialConLinks = historial.stream()
                .map(historialModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(historialConLinks);
    }
}
