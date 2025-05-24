package com.example.edutech.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edutech.Model.EstadoSolicitud;
import com.example.edutech.Model.ReporteIncidencia;
import com.example.edutech.Service.ReporteIncidenciaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteIncidenciaController {
    private ReporteIncidenciaService reporteIncidenciaService;

    public ReporteIncidenciaController(ReporteIncidenciaService reporteIncidenciaService){
        this.reporteIncidenciaService = reporteIncidenciaService;
    }

    //Crear nueva solicitud
    @PostMapping("/crear")
    public ResponseEntity<ReporteIncidencia> crearSolicitud(@RequestBody Map<String, String> datos ) {
        try {
            int usuarioId = Integer.parseInt(datos.get("usuarioId"));
            String mensaje = datos.get("mensaje");

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

}
