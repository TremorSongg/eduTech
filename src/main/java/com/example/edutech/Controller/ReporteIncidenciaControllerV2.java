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
import org.springframework.hateoas.Link; // Importar Link
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap; // Importar HashMap
import java.util.Optional; // Importar Optional
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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

    // Nuevo endpoint para la raíz del controlador, exponiendo las operaciones disponibles.
    @Operation(summary = "Punto de entrada de Reportes de Incidencia", description = "Provee enlaces a las operaciones principales de los reportes, indicando los parámetros necesarios.")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getRootReportesInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Bienvenido al servicio de Reportes de Incidencia. Aquí tienes las operaciones disponibles:");

        Map<String, Link> links = new HashMap<>();
        // Enlace para obtener reportes por usuario (GET)
        links.put("obtenerPorUsuario", linkTo(methodOn(ReporteIncidenciaControllerV2.class).getByUsuario(0)).withRel("obtenerPorUsuario").withTitle("Obtener reportes por usuario. Requiere '{usuarioId}'."));
        // Enlace para crear una solicitud (POST) - se incluye con título explicativo sobre el cuerpo.
        links.put("crearReporte", linkTo(methodOn(ReporteIncidenciaControllerV2.class).crearSolicitud(null)).withRel("crearReporte").withTitle("Crear un nuevo reporte. Requiere un cuerpo JSON con 'usuarioId' y 'mensaje'."));
        // Enlace para cambiar el estado (PUT) - se incluye con título explicativo sobre los parámetros y el cuerpo.
        links.put("cambiarEstadoReporte", linkTo(methodOn(ReporteIncidenciaControllerV2.class).cambiarEstado(0, null)).withRel("cambiarEstadoReporte").withTitle("Cambiar el estado de un reporte. Requiere '{id}' y un cuerpo JSON con 'estado'."));
        // Enlace para obtener un reporte individual (GET)
        links.put("obtenerReportePorId", linkTo(methodOn(ReporteIncidenciaControllerV2.class).getReporteById(0)).withRel("obtenerReportePorId").withTitle("Obtener un reporte por ID. Requiere '{id}'."));

        response.put("_links", links);

        return ResponseEntity.ok(response);
    }

    // Nuevo endpoint para obtener un solo ReporteIncidencia por su ID
    @Operation(summary = "Obtener Reporte de Incidencia por ID", description = "Devuelve un reporte de incidencia específico por su ID")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ReporteIncidencia>> getReporteById(@PathVariable int id) {
        Optional<ReporteIncidencia> reporteOpt = reporteIncidenciaService.obtenerPorId(id); // Asumo que tu servicio tiene este método
        return reporteOpt.map(assembler::toModel)
                         .map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear Reporte de Incidencia", description = "Permite a un usuario crear un reporte de incidencia")
    @PostMapping(value = "/crear", produces = MediaTypes.HAL_JSON_VALUE)
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
            // Un manejo de error más detallado aquí sería beneficioso para el cliente.
            // Por ahora, se mantiene la firma original que devuelve null.
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Cambiar Estado de Reporte de Incidencia", description = "Permite cambiar el estado de un reporte de incidencia")
    @PutMapping(value = "/estado/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ReporteIncidencia>> cambiarEstado(
                @PathVariable("id") int id,
                @RequestBody Map<String, String> datos){
        try {
            EstadoSolicitud nuevoEstado = EstadoSolicitud.valueOf(datos.get("estado"));
            ReporteIncidencia actualizada = reporteIncidenciaService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(assembler.toModel(actualizada));
        } catch (IllegalArgumentException e) {
            // Un manejo de error más detallado aquí sería beneficioso para el cliente.
            // Por ahora, se mantiene la firma original que devuelve null.
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Obtener Reportes de Incidencia por Usuario", description = "Devuelve una lista de reportes de incidencia para un usuario específico")
    @GetMapping(value = "/usuario/{usuarioId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ReporteIncidencia>>> getByUsuario(@PathVariable int usuarioId) {
        try {
            List<ReporteIncidencia> reportes = reporteIncidenciaService.obtenerPorUsuario(usuarioId);
            List<EntityModel<ReporteIncidencia>> models = reportes.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            // ✅ Añadir el self link a la CollectionModel
            return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(ReporteIncidenciaControllerV2.class).getByUsuario(usuarioId)).withSelfRel()));
        } catch (IllegalArgumentException e) {
            // Un manejo de error más detallado aquí sería beneficioso para el cliente.
            // Por ahora, se mantiene la firma original que devuelve null.
            return ResponseEntity.badRequest().body(null);
        }
    }
}
