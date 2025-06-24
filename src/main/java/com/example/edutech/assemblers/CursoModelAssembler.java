package com.example.edutech.assemblers;
//clases necesarias para el modelo y controller.
import com.example.edutech.Model.Curso;
import com.example.edutech.Controller.CursoController;
import com.example.edutech.Controller.CursoControllerV2;
//clase static para crear los enlaces HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.lang.reflect.Method;

//clase EntityModel para usar los HATEOAS
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
//anotacion NoNull para indicar que el método no acepta valores nulos
import org.springframework.lang.NonNull;
//agregar anotación @Component para indicar que la clase CursoModelAssembler
//es un componente spring y puede ser inyectada en otros componentes o controladores
@Component
public class CursoModelAssembler implements RepresentationModelAssembler<Curso, EntityModel<Curso>>{
    @Override
    public @NonNull EntityModel<Curso> toModel(Curso curso){
        //El método linkTo se usa para crear los enlaces HATEOAS para cada API
        //methodOn reconoce el método REST del controller
        return EntityModel.of(curso,
        linkTo(methodOn(CursoControllerV2.class).obtenerCursos(curso.getId())).withSelfRel(),
        linkTo(methodOn(CursoControllerV2.class).obtenerCursoPorId()).withRel("cursos"),
        linkTo(methodOn(CursoControllerV2.class).listarCurso()).withRel("cursos"),
        linkTo(methodOn(CursoControllerV2.class).eliminarCurso(curso.getId())).withRel("eliminar"),
        linkTo(methodOn(CursoControllerV2.class).actualizarCurso(curso.getId(),curso)).withRel("actualizar")
        );
    }
    
}
