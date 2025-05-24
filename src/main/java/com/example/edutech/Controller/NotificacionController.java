package com.example.edutech.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edutech.Service.NotificacionService;
import com.example.edutech.Model.Notificacion;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {
    
    private NotificacionService notificacionService;

    public NotificacionController(NotificacionService NotificacionService){
        this.notificacionService = notificacionService;
    }
    @PostMapping
    public Notificacion crear(@RequestBody Notificacion notificacion){ 
        return notificacionService.crear(notificacion) ;
    }
    
    @PostMapping
    public ResponseEntity<String> crearNotificacion(@RequestBody Map<String, Object> datos) {
        try {
            int usuarioId = (int) datos.get("usuarioId");
            String mensaje = (String) datos.get("mensaje");

            Notificacion notificacion = new Notificacion();
            notificacion.setUsuarioId(usuarioId);
            notificacion.setMensaje(mensaje);

            notificacionService.crear(notificacion);
            return ResponseEntity.ok("Notificación Creada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Datos inválidos" + e.getMessage());
        }

    }
    
}
