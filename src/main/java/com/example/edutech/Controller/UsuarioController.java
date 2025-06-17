package com.example.edutech.Controller;

import com.example.edutech.Model.Usuario;
import com.example.edutech.Service.UsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService serv;

    @Operation(summary = "Registrar nuevo usuario", description = "Permite registrar un nuevo usuario en la plataforma")
    @PostMapping("/registrar")
    public Usuario registrar(@RequestBody Usuario u) {
        return serv.guardar(u);
    }

    @Operation(summary = "Actualizar usuario", description = "Permite actualizar los datos de un usuario existente")
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Usuario u) {
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
        return response;
    }

    @Operation(summary = "Obtener usuario por ID", description = "Permite obtener los datos de un usuario por su ID")
    @GetMapping("/listar")
    public List<Usuario> listarUsuarios() {
        return serv.obtenerUsuarios();
    }
}
