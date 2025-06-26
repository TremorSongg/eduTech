package com.example.edutech.assemblers;

import com.example.edutech.Controller.CarritoControllerV2;
import com.example.edutech.Model.CarritoItem;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CarritoItemModelAssembler implements RepresentationModelAssembler<CarritoItem, EntityModel<CarritoItem>> {

    @Override
    @org.springframework.lang.NonNull
    public EntityModel<CarritoItem> toModel(CarritoItem item) {
        return EntityModel.of(item,
                linkTo(methodOn(CarritoControllerV2.class).verCarrito(item.getUsuarioId())).withRel("verCarrito"),
                linkTo(methodOn(CarritoControllerV2.class).eliminarItem(item.getCursoId(), item.getUsuarioId())).withRel("eliminar"),
                linkTo(methodOn(CarritoControllerV2.class).actualizarCantidad(
                        item.getCursoId(), item.getUsuarioId(), 
                        // solo usamos un dummy payload para evitar nulls (Spring no lo ejecuta realmente)
                        java.util.Map.of("cantidad", item.getCantidad())
                )).withRel("actualizarCantidad"),
                linkTo(methodOn(CarritoControllerV2.class).vaciarCarrito(item.getUsuarioId())).withRel("vaciarCarrito"),
                linkTo(methodOn(CarritoControllerV2.class).finalizarCompra(item.getUsuarioId())).withRel("finalizarCompra"),
                linkTo(methodOn(CarritoControllerV2.class).obtenerTotal(item.getUsuarioId())).withRel("obtenerTotal")
        );
    }
}
