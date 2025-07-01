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

    
    @Autowired
    private MockMvc mockMvc;

    
    @MockBean
    private HistorialCompraService historialCompraService;

    
    @Autowired
    private ObjectMapper objectMapper;

    
    @Test
    void obtenerHistorialPorUsuario_debeRetornarListaDeCompras() throws Exception {
        
        int usuarioId = 1;

        
        HistorialCompra compra1 = new HistorialCompra(1L, usuarioId, 101, "Curso de Java", 49.99, 1, 49.99, LocalDateTime.now());
        HistorialCompra compra2 = new HistorialCompra(2L, usuarioId, 102, "Curso de Spring Boot", 59.99, 1, 59.99, LocalDateTime.now().minusDays(1));
        List<HistorialCompra> historialSimulado = Arrays.asList(compra1, compra2);

        
        when(historialCompraService.obtenerHistorialPorUsuarioId(usuarioId)).thenReturn(historialSimulado);

        mockMvc.perform(get("/api/v1/historial/usuario/{usuarioId}", usuarioId) 
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$", hasSize(2))) 
                .andExpect(jsonPath("$[0].nombreCurso", is("Curso de Java"))) 
                .andExpect(jsonPath("$[0].usuarioId", is(usuarioId))) 
                .andExpect(jsonPath("$[1].nombreCurso", is("Curso de Spring Boot"))); 
    }

    // Test para simular el caso en que un usuario no tiene historial de compras
    @Test
    void obtenerHistorialPorUsuario_cuandoNoHayCompras_debeRetornarListaVacia() throws Exception {
        
        int usuarioId = 99; 

        // Simular que el servicio devuelve una lista vacía para este usuario
        when(historialCompraService.obtenerHistorialPorUsuarioId(usuarioId)).thenReturn(Collections.emptyList());

        
        mockMvc.perform(get("/api/v1/historial/usuario/{usuarioId}", usuarioId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$", hasSize(0))); 
    }
}