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
    public @NonNull EntityModel<Usuario> toModel(@NonNull Usuario u) {
        // Enlace 'self' para el usuario individual, asumiendo un endpoint GET /api/v2/usuarios/{id}
        // Este es crucial para la navegación HATEOAS.
        EntityModel<Usuario> usuarioModel = EntityModel.of(u,
            linkTo(methodOn(UsuarioControllerV2.class).getUsuarioById(u.getId())).withSelfRel(),
            linkTo(methodOn(UsuarioControllerV2.class).listarUsuarios()).withRel("listar-usuarios"), // Enlace a la colección de usuarios
            linkTo(methodOn(UsuarioControllerV2.class).actualizarUsuario(u.getId(), u)).withRel("actualizar-usuario"), // Enlace para actualizar este usuario
            linkTo(methodOn(UsuarioControllerV2.class).eliminarUsuario(u.getId())).withRel("eliminar-usuario") // Enlace para eliminar este usuario
        );
        return usuarioModel;
    }
}
