package com.example.edutech.assemblers;

import com.example.edutech.Model.HistorialCompra;
import com.example.edutech.Controller.HistorialCompraControllerV2;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class HistorialModelAssembler implements RepresentationModelAssembler<HistorialCompra, EntityModel<HistorialCompra>> {

    @Override
    @org.springframework.lang.NonNull
    public EntityModel<HistorialCompra> toModel(HistorialCompra historial) {
        return EntityModel.of(historial,
            linkTo(methodOn(HistorialCompraControllerV2.class)
                .obtenerHistorialPorUsuario(historial.getUsuarioId()))
                .withRel("historial-del-usuario")
        );
    }
}
