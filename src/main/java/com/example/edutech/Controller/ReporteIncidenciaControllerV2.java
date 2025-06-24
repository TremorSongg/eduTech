package com.example.edutech.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edutech.Model.EstadoSolicitud;
import com.example.edutech.Model.ReporteIncidencia;
import com.example.edutech.Service.ReporteIncidenciaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/reportes")
@Tag(name = "Reportes de Incidencia", description = "Operaciones relacionadas con los reportes de incidencia de los usuarios")
public class ReporteIncidenciaControllerV2 {
    private ReporteIncidenciaService reporteIncidenciaService;

    public ReporteIncidenciaControllerV2(ReporteIncidenciaService reporteIncidenciaService){
        this.reporteIncidenciaService = reporteIncidenciaService;
    }

    //Crear nueva solicitud
    @Operation(summary = "Crear Reporte de Incidencia", description = "Permite a un usuario crear un reporte de incidencia")
    @PostMapping("/crear")
    public ResponseEntity<ReporteIncidencia> crearSolicitud(@RequestBody Map<String, Object> datos ) {
        try {
            int usuarioId = (int)(datos.get("usuarioId"));
            String mensaje = (String)datos.get("mensaje");

            ReporteIncidencia solicitud = new ReporteIncidencia();
            solicitud.setUsuarioId(usuarioId);
            solicitud.setMensaje(mensaje);
            solicitud.setEstado(EstadoSolicitud.PENDIENTE);

            ReporteIncidencia nuevaSolicitud = reporteIncidenciaService.crearSolicitud(solicitud);
            return ResponseEntity.ok(nuevaSolicitud);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    //Cambiar estado de solicitud y enviar notificacion
    @Operation(summary = "Cambiar Estado de Reporte de Incidencia", description = "Permite cambiar el estado de un reporte de incidencia")
    @PutMapping("/estado/{id}")
    public ResponseEntity<ReporteIncidencia> cambiarEstado(
            @PathVariable("id") int id,
            @RequestBody Map<String, String> datos){
        try{
            EstadoSolicitud nuevoEstado = EstadoSolicitud.valueOf(datos.get("estado"));
            ReporteIncidencia actualizada = reporteIncidenciaService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(actualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

        }
        //muestra mediante GET, los reportes de usuarios con el mismo id
    @Operation(summary = "Obtener Reportes de Incidencia por Usuario", description = "Devuelve una lista de reportes de incidencia para un usuario específico")
    @GetMapping("/usuario/{usuarioId}")
    //ResponseEntity es el esqueleto de los datos con lista de reportes de incidencia
    //pathvariable consulta la base de datos mediante JPA
    public ResponseEntity<List<ReporteIncidencia>> getByUsuario(@PathVariable int usuarioId) {
        try {
            // aquí obtiene la lista de datos de reporte y los une al esqueleto
            List<ReporteIncidencia> reportes = reporteIncidenciaService.obtenerPorUsuario(usuarioId);
            return ResponseEntity.ok(reportes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
