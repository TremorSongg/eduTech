package com.example.edutech.assemblers;

import com.example.edutech.Controller.CarritoControllerV2;
import com.example.edutech.Model.CarritoItem;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CarritoItemModelAssembler implements RepresentationModelAssembler<CarritoItem, EntityModel<CarritoItem>> {

    @Override
    @NonNull
    public EntityModel<CarritoItem> toModel(@NonNull CarritoItem item) {
        // Se asume que existe un endpoint GET /api/v2/carrito/items/{cursoId}?usuarioId={usuarioId}
        // para obtener un item específico del carrito. Si no existe, este self link será conceptual.
        return EntityModel.of(item,
                linkTo(methodOn(CarritoControllerV2.class).getSingleCarritoItem(item.getCursoId(), item.getUsuarioId())).withSelfRel(),
                linkTo(methodOn(CarritoControllerV2.class).verCarrito(item.getUsuarioId())).withRel("verCarrito"),
                linkTo(methodOn(CarritoControllerV2.class).eliminarItem(item.getCursoId(), item.getUsuarioId())).withRel("eliminarItem"),
                linkTo(methodOn(CarritoControllerV2.class).actualizarCantidad(
                        item.getCursoId(), item.getUsuarioId(),
                        // dummy payload para Spring HATEOAS, no se ejecuta realmente
                        java.util.Map.of("cantidad", item.getCantidad())
                )).withRel("actualizarCantidad"),
                linkTo(methodOn(CarritoControllerV2.class).vaciarCarrito(item.getUsuarioId())).withRel("vaciarCarrito"),
                linkTo(methodOn(CarritoControllerV2.class).finalizarCompra(item.getUsuarioId())).withRel("finalizarCompra"),
                linkTo(methodOn(CarritoControllerV2.class).obtenerTotal(item.getUsuarioId())).withRel("obtenerTotal")
        );
    }
}
