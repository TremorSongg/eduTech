package com.example.edutech.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edutech.Model.EstadoSolicitud;
import com.example.edutech.Model.ReporteIncidencia;
import com.example.edutech.Service.ReporteIncidenciaService;
import com.example.edutech.assemblers.ReporteIncidenciaModelAssembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/reportes")
@Tag(name = "Reportes de Incidencia", description = "Operaciones relacionadas con los reportes de incidencia de los usuarios")
public class ReporteIncidenciaControllerV2 {

    private final ReporteIncidenciaService reporteIncidenciaService;
    private final ReporteIncidenciaModelAssembler assembler;

    public ReporteIncidenciaControllerV2(ReporteIncidenciaService reporteIncidenciaService, ReporteIncidenciaModelAssembler assembler) {
        this.reporteIncidenciaService = reporteIncidenciaService;
        this.assembler = assembler;
    }

    @Operation(summary = "Crear Reporte de Incidencia", description = "Permite a un usuario crear un reporte de incidencia")
    @PostMapping(value = "/crear", produces = MediaTypes.HAL_JSON_VALUE) // ✅ HAL agregado
    public ResponseEntity<EntityModel<ReporteIncidencia>> crearSolicitud(@RequestBody Map<String, Object> datos) {
        try {
            int usuarioId = (int)(datos.get("usuarioId"));
            String mensaje = (String)datos.get("mensaje");

            ReporteIncidencia solicitud = new ReporteIncidencia();
            solicitud.setUsuarioId(usuarioId);
            solicitud.setMensaje(mensaje);
            solicitud.setEstado(EstadoSolicitud.PENDIENTE);

            ReporteIncidencia nuevaSolicitud = reporteIncidenciaService.crearSolicitud(solicitud);
            return ResponseEntity.ok(assembler.toModel(nuevaSolicitud));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Cambiar Estado de Reporte de Incidencia", description = "Permite cambiar el estado de un reporte de incidencia")
    @PutMapping(value = "/estado/{id}", produces = MediaTypes.HAL_JSON_VALUE) // ✅ HAL agregado
    public ResponseEntity<EntityModel<ReporteIncidencia>> cambiarEstado(
            @PathVariable("id") int id,
            @RequestBody Map<String, String> datos){
        try {
            EstadoSolicitud nuevoEstado = EstadoSolicitud.valueOf(datos.get("estado"));
            ReporteIncidencia actualizada = reporteIncidenciaService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(assembler.toModel(actualizada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Obtener Reportes de Incidencia por Usuario", description = "Devuelve una lista de reportes de incidencia para un usuario específico")
    @GetMapping(value = "/usuario/{usuarioId}", produces = MediaTypes.HAL_JSON_VALUE) // ✅ HAL agregado
    public ResponseEntity<CollectionModel<EntityModel<ReporteIncidencia>>> getByUsuario(@PathVariable int usuarioId) {
        try {
            List<ReporteIncidencia> reportes = reporteIncidenciaService.obtenerPorUsuario(usuarioId);
            List<EntityModel<ReporteIncidencia>> models = reportes.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(CollectionModel.of(models));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
