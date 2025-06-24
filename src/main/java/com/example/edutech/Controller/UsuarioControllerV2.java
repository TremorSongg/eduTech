package com.example.edutech.Controller;

import com.example.edutech.Model.Usuario;
import com.example.edutech.Service.UsuarioService;
import com.example.edutech.assemblers.UsuarioModelAssembler;


import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

//Assembler para HATEOAS
import com.example.edutech.assemblers.UsuarioModelAssembler;
//clases necesarias para HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
//clases HATEOAS EntityModel y CollectionModel (que va dentro del Mediatype) para manejar los modelos de return
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
//respuestas de responsEntitypara manejar las respuestas HTTP
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v2/usuarios")
@CrossOrigin
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService serv;

    //Inyectar el Assembler
    @Autowired
    private UsuarioModelAssembler assembler;


    @Operation(summary = "Registrar nuevo usuario", description = "Permite registrar un nuevo usuario en la plataforma")
    //Agregamos value y produces a cada metodo REST para indicar la ruta de la API y anotacion produces para indicar
    // que el método devuelve un EntityModel del usuario en formato HAL JSON
    @PostMapping(value = "/registrar", produces = MediaTypes.HAL_JSON_VALUE)
    public Usuario registrar(@RequestBody Usuario u) {
        return serv.guardar(u);
    }

    @Operation(summary = "Actualizar usuario", description = "Permite actualizar los datos de un usuario existente")
    @PostMapping(value = "/login", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Map<String, String>>> login(@RequestBody Usuario u) {
        Optional<Usuario> user = serv.autenticar(u.getEmail(), u.getPassword());
        Map<String, String> response = new HashMap<>();
        if (user.isPresent()) {
            response.put("result", "OK");
            response.put("id", String.valueOf(user.get().getId())); // Convertir ID a String
            response.put("nombre", user.get().getNombre());
            response.put("email", user.get().getEmail());
        } else {
            response.put("result", "Error");
        }
        EntityModel<Map<String, String>> model = EntityModel.of(response);
        model.add(linkTo(methodOn(UsuarioControllerV2.class)
        .login(u)).withSelfRel());
        model.add(linkTo(methodOn(UsuarioControllerV2.class).
        registrar(new Usuario())).withRel("registrar"));
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Obtener usuario por ID", description = "Permite obtener los datos de un usuario por su ID")
    @GetMapping(value = "/listar", produces = MediaTypes.HAL_JSON_VALUE)
    public List<Usuario> listarUsuarios() {
        return serv.obtenerUsuarios();
    }
}
