package com.example.edutech.Controller;

import com.example.edutech.Model.HistorialCompra;
import com.example.edutech.Service.HistorialCompraService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


@WebMvcTest(HistorialCompraController.class) // Anotación para crear pruebas específicas al controlador web
public class HistorialCompraControllerIntegrationTest {

    // Usar la anotacion autowired para inyectar MockMvc y realizar peticiones http simuladas
    @Autowired
    private MockMvc mockMvc;

    // Simular el servicio HistorialCompraService
    @MockBean
    private HistorialCompraService historialCompraService;

    // Anotacion Autowired para inyectar la clase ObjectMapper, para convertir objetos a formato JSON
    @Autowired
    private ObjectMapper objectMapper;

    // Anotación Test para simular la obtención del historial de compras de un usuario
    @Test
    void obtenerHistorialPorUsuario_debeRetornarListaDeCompras() throws Exception {
        // 1. Arrange (Preparar los datos de prueba y el comportamiento simulado)
        int usuarioId = 1;

        // Crear una lista de compras de ejemplo que el servicio debería devolver
        HistorialCompra compra1 = new HistorialCompra(1L, usuarioId, 101, "Curso de Java", 49.99, 1, 49.99, LocalDateTime.now());
        HistorialCompra compra2 = new HistorialCompra(2L, usuarioId, 102, "Curso de Spring Boot", 59.99, 1, 59.99, LocalDateTime.now().minusDays(1));
        List<HistorialCompra> historialSimulado = Arrays.asList(compra1, compra2);

        // Simular el comportamiento del servicio: cuando se llame a obtenerHistorialPorUsuarioId con el ID 1, devolver la lista creada
        when(historialCompraService.obtenerHistorialPorUsuarioId(usuarioId)).thenReturn(historialSimulado);

        // 2. Act & Assert (Realizar la petición y verificar el resultado)
        // Simular una petición GET al endpoint del controlador
        mockMvc.perform(get("/api/v1/historial/usuario/{usuarioId}", usuarioId) // La URL debe coincidir con la del @GetMapping
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verificar que el estado de la respuesta sea 200 OK
                .andExpect(jsonPath("$", hasSize(2))) // Verificar que el JSON de respuesta es un array con 2 elementos
                .andExpect(jsonPath("$[0].nombreCurso", is("Curso de Java"))) // Verificar el nombre del curso en el primer elemento
                .andExpect(jsonPath("$[0].usuarioId", is(usuarioId))) // Verificar el ID de usuario
                .andExpect(jsonPath("$[1].nombreCurso", is("Curso de Spring Boot"))); // Verificar el nombre del curso en el segundo elemento
    }

    // Test para simular el caso en que un usuario no tiene historial de compras
    @Test
    void obtenerHistorialPorUsuario_cuandoNoHayCompras_debeRetornarListaVacia() throws Exception {
        // 1. Arrange
        int usuarioId = 99; // Un usuario que no tiene compras

        // Simular que el servicio devuelve una lista vacía para este usuario
        when(historialCompraService.obtenerHistorialPorUsuarioId(usuarioId)).thenReturn(Collections.emptyList());

        // 2. Act & Assert
        // Realizar la petición GET
        mockMvc.perform(get("/api/v1/historial/usuario/{usuarioId}", usuarioId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // El estado sigue siendo OK
                .andExpect(jsonPath("$", hasSize(0))); // Verificar que el array JSON de respuesta está vacío
    }
}