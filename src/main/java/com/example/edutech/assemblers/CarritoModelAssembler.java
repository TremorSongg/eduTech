package com.example.edutech.assemblers;
//clases necesarias para el modelo y controller.
import com.example.edutech.Model.Curso;
import com.example.edutech.Model.CarritoItem;
import com.example.edutech.Controller.CarritoControllerV2;
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
public class CarritoModelAssembler implements 
RepresentationModelAssembler<Curso,EntityModel<Curso>>{

    @Override
    public @NonNull EntityModel<Curso> toModel(Curso curso){
        return EntityModel.of(curso);
    }
        //Método extra para poder asignar el id de usuario
    public EntityModel<Curso> toModelWithUser(Curso curso, int usuarioId){
        return EntityModel.of(curso,
        //metodo get no usamos withselfrel, solo with rel
        linkTo(methodOn(CarritoControllerV2.class).verCarrito(curso.getId())).withRel("Carrito"),
        linkTo(methodOn(CarritoControllerV2.class).eliminarItem(curso.getId(), usuarioId)).withRel("Eliminar")
        );
    }
    
}
