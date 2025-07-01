package com.example.edutech.assemblers;

import com.example.edutech.Model.Notificacion;
import com.example.edutech.Controller.NotificacionControllerV2;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class NotificacionModelAssembler implements RepresentationModelAssembler<Notificacion, EntityModel<Notificacion>> {

    @Override
    @org.springframework.lang.NonNull
    public EntityModel<Notificacion> toModel(Notificacion notificacion) {
        return EntityModel.of(notificacion,
            linkTo(methodOn(NotificacionControllerV2.class).crearNotificacion(null)).withRel("crear-notificacion")
        );
    }
}
