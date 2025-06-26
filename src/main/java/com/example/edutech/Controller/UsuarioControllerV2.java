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
import org.springframework.hateoas.Link; 
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

    @Operation(summary = "Punto de entrada de Usuarios", description = "Provee enlaces a las operaciones principales de usuario, indicando los parámetros necesarios.")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getRootUsuarioInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Bienvenido al servicio de Usuarios. Aquí tienes las operaciones disponibles:");

        Map<String, Link> links = new HashMap<>();
        links.put("listarUsuarios", linkTo(methodOn(UsuarioControllerV2.class).listarUsuarios()).withRel("listar-usuarios").withTitle("Obtener la lista de todos los usuarios."));
        links.put("registrarUsuario", linkTo(methodOn(UsuarioControllerV2.class).registrar(new Usuario())).withRel("registrar-usuario").withTitle("Registrar un nuevo usuario. Requiere cuerpo JSON con los datos del usuario."));
        links.put("loginUsuario", linkTo(methodOn(UsuarioControllerV2.class).login(new Usuario())).withRel("login-usuario").withTitle("Autenticar un usuario. Requiere cuerpo JSON con 'email' y 'password'."));
        links.put("obtenerUsuarioPorId", linkTo(methodOn(UsuarioControllerV2.class).getUsuarioById(0)).withRel("obtener-usuario-por-id").withTitle("Obtener un usuario por su ID. Requiere '{id}'."));

        response.put("_links", links);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar nuevo usuario", description = "Permite registrar un nuevo usuario en la plataforma")
    @PostMapping(value = "/registrar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> registrar(@RequestBody Usuario u) {
        Usuario saved = serv.guardar(u);
        return ResponseEntity.ok(assembler.toModel(saved));
    }

    @Operation(summary = "Autenticar usuario", description = "Permite autenticar un usuario con email y contraseña")
    @PostMapping(value = "/login", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Map<String, String>>> login(@RequestBody Usuario u) {
        Optional<Usuario> user = serv.autenticar(u.getEmail(), u.getPassword());
        Map<String, String> response = new HashMap<>();
        if (user.isPresent()) {
            response.put("result", "OK");
            response.put("id", String.valueOf(user.get().getId()));
            response.put("nombre", user.get().getNombre());
            response.put("email", user.get().getEmail());

            EntityModel<Map<String, String>> model = EntityModel.of(response);
            model.add(linkTo(methodOn(UsuarioControllerV2.class).login(new Usuario())).withSelfRel()); 
            model.add(linkTo(methodOn(UsuarioControllerV2.class).registrar(new Usuario())).withRel("registrar-usuario")); 
            return ResponseEntity.ok(model);
        } else {
            response.put("result", "Error");
            response.put("message", "Credenciales inválidas.");
            EntityModel<Map<String, String>> model = EntityModel.of(response);
            model.add(linkTo(methodOn(UsuarioControllerV2.class).login(new Usuario())).withSelfRel());
            model.add(linkTo(methodOn(UsuarioControllerV2.class).registrar(new Usuario())).withRel("registrar-usuario"));
            return ResponseEntity.status(401).body(model);
        }
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Permite obtener una lista de todos los usuarios")
    @GetMapping(value = "/listar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listarUsuarios() {
        List<EntityModel<Usuario>> usuarios = serv.obtenerUsuarios().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(usuarios,
            linkTo(methodOn(UsuarioControllerV2.class).listarUsuarios()).withSelfRel()));
    }

    @Operation(summary = "Obtener usuario por ID", description = "Permite obtener los datos de un usuario por su ID")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> getUsuarioById(@PathVariable int id) {
        Optional<Usuario> usuarioOpt = serv.buscarPorId(id);
        return usuarioOpt.map(assembler::toModel)
                         .map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar usuario", description = "Permite actualizar los datos de un usuario existente por su ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable int id, @RequestBody Usuario usuarioActualizado) {
        Usuario updated = serv.actualizar(id, usuarioActualizado);
        if (updated != null) {
            return ResponseEntity.ok(assembler.toModel(updated));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar usuario", description = "Permite eliminar un usuario por su ID")
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminarUsuario(@PathVariable int id) {
        serv.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
