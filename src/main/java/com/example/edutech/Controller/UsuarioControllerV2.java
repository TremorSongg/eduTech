package com.example.edutech.Controller;

import com.example.edutech.Model.Usuario;
import com.example.edutech.Service.UsuarioService;
import com.example.edutech.assemblers.UsuarioModelAssembler;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v2/usuarios")
@CrossOrigin
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService serv;

    @Autowired
    private UsuarioModelAssembler assembler;

    @Operation(summary = "Registrar nuevo usuario", description = "Permite registrar un nuevo usuario en la plataforma")
    @PostMapping(value = "/registrar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> registrar(@RequestBody Usuario u) {
        Usuario saved = serv.guardar(u);
        return ResponseEntity.ok(assembler.toModel(saved));
    }

    @Operation(summary = "Actualizar usuario", description = "Permite actualizar los datos de un usuario existente")
    @PostMapping(value = "/login", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Map<String, String>>> login(@RequestBody Usuario u) {
        Optional<Usuario> user = serv.autenticar(u.getEmail(), u.getPassword());
        Map<String, String> response = new HashMap<>();
        if (user.isPresent()) {
            response.put("result", "OK");
            response.put("id", String.valueOf(user.get().getId()));
            response.put("nombre", user.get().getNombre());
            response.put("email", user.get().getEmail());
        } else {
            response.put("result", "Error");
        }
        EntityModel<Map<String, String>> model = EntityModel.of(response);
        model.add(linkTo(methodOn(UsuarioControllerV2.class).login(new Usuario())).withSelfRel());
        model.add(linkTo(methodOn(UsuarioControllerV2.class).registrar(new Usuario())).withRel("registrar"));
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Obtener usuario por ID", description = "Permite obtener los datos de un usuario por su ID")
    @GetMapping(value = "/listar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listarUsuarios() {
        List<EntityModel<Usuario>> usuarios = serv.obtenerUsuarios().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(usuarios,
            linkTo(methodOn(UsuarioControllerV2.class).listarUsuarios()).withSelfRel()));
    }
}
