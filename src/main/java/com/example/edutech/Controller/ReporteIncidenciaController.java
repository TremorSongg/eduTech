package com.example.edutech.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edutech.Model.ReporteIncidencia;
import com.example.edutech.Service.ReporteIncidenciaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

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

            ReporteIncidencia nuevaSolicitud = reporteIncidenciaService.crearSolicitud(usuarioId, mensaje);
            return ResponseEntity.ok(nuevaSolicitud);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    
}
