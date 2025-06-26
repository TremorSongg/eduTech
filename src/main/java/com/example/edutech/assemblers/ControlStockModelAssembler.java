package com.example.edutech.assemblers;

import com.example.edutech.Model.Curso;
import com.example.edutech.Controller.ControlStockControllerV2;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ControlStockModelAssembler implements RepresentationModelAssembler<Curso, EntityModel<Curso>> {

    @Override
    @org.springframework.lang.NonNull
    public EntityModel<Curso> toModel(Curso curso) {
        return EntityModel.of(curso,
            linkTo(methodOn(ControlStockControllerV2.class).buscarCurso(curso.getId())).withRel("buscar-curso"),
            linkTo(methodOn(ControlStockControllerV2.class).controlarStock(curso.getId())).withRel("controlar-stock"),
            linkTo(methodOn(ControlStockControllerV2.class).mostrarCursos()).withRel("listar-cursos")
        );
    }
}
