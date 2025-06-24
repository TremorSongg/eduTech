package com.example.edutech.assemblers;

import com.example.edutech.Model.Curso;
import com.example.edutech.Controller.ControlStockControllerV2;

// Clase estática para crear los enlaces HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
// Clase EntityModel para usar los HATEOAS
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
// Anotación NoNull para indicar que el método no acepta valores nulos
import org.springframework.lang.NonNull;

// Agregar anotación @Component para indicar que la clase ControlStockModelAssembler
// es un componente Spring y puede ser inyectada en otros componentes o controladores
@Component


public class ControlStockModelAssembler implements RepresentationModelAssembler<Curso, EntityModel<Curso>> {
    
    @Override
    public @NonNull EntityModel<Curso> toModel(Curso curso) {
        // El método linkTo se usa para crear los enlaces HATEOAS para cada API
        // methodOn reconoce el método REST del controller
        return EntityModel.of(curso,
            linkTo(methodOn(ControlStockControllerV2.class).mostrarCursos()).withRel("mostrarCursos"),
            linkTo(methodOn(ControlStockControllerV2.class).controlarStock(curso.getId())).withRel("controlarStock"),
            linkTo(methodOn(ControlStockControllerV2.class).buscarCurso(curso.getId())).withRel("buscarCurso")
                );
            }
        }
