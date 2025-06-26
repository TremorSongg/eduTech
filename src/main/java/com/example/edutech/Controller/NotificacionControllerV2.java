package com.example.edutech.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edutech.Service.NotificacionService;
import com.example.edutech.Model.Notificacion;
import com.example.edutech.assemblers.NotificacionModelAssembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/notificaciones")
@Tag(name = "Notificaciones", description = "Operaciones relacionadas con las notificaciones de los usuarios")
public class NotificacionControllerV2 {

    private final NotificacionService notificacionService;
    private final NotificacionModelAssembler notificacionModelAssembler;

    public NotificacionControllerV2(NotificacionService notificacionService,
                                    NotificacionModelAssembler notificacionModelAssembler) {
        this.notificacionService = notificacionService;
        this.notificacionModelAssembler = notificacionModelAssembler;
    }

    @Operation(summary = "Crear Notificación", description = "Permite crear una nueva notificación para un usuario")
    @PostMapping(name = "crearNotificacion", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> crearNotificacion(@RequestBody Map<String, Object> datos) {
        try {
            int usuarioId = (int) datos.get("usuarioId");
            String mensaje = (String) datos.get("mensaje");

            Notificacion notificacion = new Notificacion();
            notificacion.setUsuarioId(usuarioId);
            notificacion.setMensaje(mensaje);

            Notificacion creada = notificacionService.crear(notificacion);

            EntityModel<Notificacion> notificacionModel = notificacionModelAssembler.toModel(creada);

            return ResponseEntity.ok(notificacionModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Datos inválidos" + e.getMessage());
        }
    }
}