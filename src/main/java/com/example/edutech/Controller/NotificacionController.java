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
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {
    
    private NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService){
        this.notificacionService = notificacionService; // Dice que no se usa pero sí se usa
    }
    
    //Método utiliza POST desde la ruta /notificaciones
    @PostMapping
    // Se reciben los datos desde el front y se transforman a formato JSON
    public ResponseEntity<String> crearNotificacion(@RequestBody Map<String, Object> datos) {
        try {
            //se obtienen los "datos" y se formatean a int y string
            int usuarioId = (int) datos.get("usuarioId");
            String mensaje = (String) datos.get("mensaje");
            //se construye notificación como objeto que contiene los datos
            Notificacion notificacion = new Notificacion();
            notificacion.setUsuarioId(usuarioId);
            notificacion.setMensaje(mensaje);

            //Guarda ek objeto en la base de datos con JPA
            notificacionService.crear(notificacion);
            //confirma
            return ResponseEntity.ok("Notificación Creada");
        } catch (Exception e) {
            //deniega en caso de error
            return ResponseEntity.badRequest().body("Datos inválidos" + e.getMessage());
        }

    }
    
}
