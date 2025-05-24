package com.example.edutech.Service;

import com.example.edutech.Model.EstadoSolicitud;
import com.example.edutech.Model.ReporteIncidencia;
import com.example.edutech.Repository.ReporteIncidenciaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReporteIncidenciaService {
    
    private ReporteIncidenciaRepository reporteIncidenciaRepository;

    public ReporteIncidenciaService(ReporteIncidenciaRepository reporteIncidenciaRepository){
        this.reporteIncidenciaRepository = reporteIncidenciaRepository;

    }

    //Aqui se crea una solicitud de tipo RI. para almacenar en la BD
    public ReporteIncidencia crearSolicitud(ReporteIncidencia solicitud){
        return reporteIncidenciaRepository.save(solicitud);
    }

    //Para poder buscar una solicitud por ID
    public List<ReporteIncidencia> obtenerPorUsuario (int usuarioId){
        return reporteIncidenciaRepository.findByUsuarioId(usuarioId);
    }
    
    //Aqui busca y compara el id de solicitud, si coincide, cambiar el estado por el nuevo valor
    //también podría probarlo con un try-catch para agarrar en caso de error.
    public ReporteIncidencia cambiarEstado (int solicitudId, EstadoSolicitud nuevoEstado){
        ReporteIncidencia solicitud = reporteIncidenciaRepository.findById(solicitudId)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        
            //guarda solicitud
        solicitud.setEstado(nuevoEstado);
        reporteIncidenciaRepository.save(solicitud);

        //esto genera un mapa para enviar al MS de notificaciones
        Map<String, Object> notificacion = new HashMap<>();
        notificacion.put("usuarioId", solicitud.getUsuarioId());
        notificacion.put("mensaje", "Tu solicitud n° " + solicitudId+" cambió a estado: " + nuevoEstado);
        //aquí lo envía con método REST
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://localhost:8080/notificaciones", notificacion, Void.class);

        return solicitud;

    }

}
