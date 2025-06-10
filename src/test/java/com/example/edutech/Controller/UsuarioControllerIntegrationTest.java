package com.example.edutech.Controller;


import com.example.edutech.Model.Usuario;
import com.example.edutech.Service.UsuarioService;

//importar ObjectMapper para convertir objetos en json
import com.fasterxml.jackson.databind.ObjectMapper;

import org.aspectj.apache.bcel.generic.IINC;
//importar anotacionesde pruebas de Junit
import org.junit.jupiter.api.Test;
// importar las anotaciones de Spring para inyectar las dependencias Maven
import org.springframework.beans.factory.annotation.Autowired;
//importar la anotación para prueba de web controller
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//anotaciones para simular los beans de los Spring
import org.springframework.boot.test.mock.mockito.MockBean;

//tipo de contenido mediatype para peticiones http
import org.springframework.http.MediaType;
//mock de modelo MockMvc para realizar peticiones http simuladas
import org.springframework.test.web.servlet.MockMvc;

//clases necesarias para realizar peticiones http simuladas
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//clases necesarias para verificar los resultados de las peticiones http simuladas
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//any para simular metodos de servicios
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
//mockito para simular el comportamiento de los métodos del servicio
import static org.mockito.Mockito.when;

import java.util.Optional;

@WebMvcTest(UsuarioController.class) //Anotación para crear pruebas específicas al controlador web
public class UsuarioControllerIntegrationTest {
    //Usar la anotacion autowired para inyectar MockMvc y realizar peticiones http simuladas
    @Autowired
    private MockMvc mockMvc;
    //Simular el servicio usuario
    @MockBean
    private UsuarioService usuarioService;

    //anotacion Autowired para crear con clase ObjectMapper, formato JSON
    @Autowired
    private ObjectMapper objectMapper;

    // anotacion Test para simular un registro de un nuevo usuario
    @Test
    void registraUsuarios_returnGuardado() throws Exception{
        Usuario newuser = new Usuario(); // Crea una instancia de usuario
        newuser.setNombre("Pepito");
        newuser.setEmail("pepito@gmail.com");
        newuser.setPassword("1234");

        //Simular si objeto Pepito existe
        when(usuarioService.guardar(any(Usuario.class))).thenReturn(newuser);

        //Simular la peticion del Post  para registrar usuarios
        mockMvc.perform(post("/api/v1/usuarios/registrar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newuser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Pepito"))
            .andExpect(jsonPath("$.email").value("pepito@gmail.com"))
            .andExpect(jsonPath("$.password").value("1234"));


    }
        //Test simular inicio sesion con usuario inexistente
    @Test
    void loginUsuario_ReturnError() throws Exception{
        Usuario userError = new Usuario();
        userError.setEmail("noexiste@gmail.com");
        userError.setPassword("1234");

        //Simular el login con un usuario inexistente
    
        when(usuarioService.autenticar("noexiste@gmail.com","1234"))
        .thenReturn(Optional.empty());

        //Realizar la simulacion de petición POST para inicio sesion
        mockMvc.perform(post("/api/v1/usuarios/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userError)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("Error"));

    }
    
}
