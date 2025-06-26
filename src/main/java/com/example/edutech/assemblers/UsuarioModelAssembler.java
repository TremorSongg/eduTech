package com.example.edutech.assemblers;

import com.example.edutech.Model.Usuario;
import com.example.edutech.Controller.UsuarioControllerV2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public @NonNull EntityModel<Usuario> toModel(Usuario u) {
        return EntityModel.of(u,
            linkTo(methodOn(UsuarioControllerV2.class).listarUsuarios()).withSelfRel(),
            linkTo(methodOn(UsuarioControllerV2.class).login(new Usuario())).withRel("login"),
            linkTo(methodOn(UsuarioControllerV2.class).registrar(new Usuario())).withRel("registrar")
        );
    }
}
