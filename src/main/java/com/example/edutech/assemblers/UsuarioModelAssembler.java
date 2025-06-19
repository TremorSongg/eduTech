package com.example.edutech.assemblers;

//clases necesarias para el modelo y controller.
import com.example.edutech.Model.Usuario;
import com.example.edutech.Controller.UsuarioControllerV2;
//clase static para crear los enlaces HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
//clase EntityModel para usar los HATEOAS
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
//anotacion NoNull para indicar que el método no acepta valores nulos
import org.springframework.lang.NonNull;


//agregar anotación @Component para indicar que la clase UsuarioModelAssembler
//es un componente spring y puede ser inyectada en otros componentes o controladores
@Component
public class UsuarioModelAssembler implements 
RepresentationModelAssembler<Usuario, EntityModel <Usuario>>{ //es una clase que implementa la representación
    //del modelo y convierte un objeto usuario en un EntityModel con le que trabajaremos

    
}
