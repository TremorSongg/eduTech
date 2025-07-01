package com.example.edutech.Controller;

import com.example.edutech.Service.CarritoService;
import com.example.edutech.Service.CursoService;
import com.example.edutech.Model.Curso;

////
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(CarritoController.class)
public class CarritoControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @MockBean
    private CarritoService carritoService;

    private Curso cursoEjemplo;

    @BeforeEach
    void setUp() {
        cursoEjemplo = new Curso(1,"Curso de Python", "Curso de Python desde cero",25,1000.0);
    }

    @Test
    void agregarCurso_alCarrito_debeResponderConfirmacion() throws Exception {
        when(cursoService.buscarPorId(1)).thenReturn(Optional.of(cursoEjemplo));

        mockMvc.perform(post("/api/v1/carrito/agregar/1")
                .param("usuarioId", "123"))  
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.mensaje").value("Curso agregado al carrito"));
    }

    @Test
    void verCarrito_debeMostrarCursosAgregados() throws Exception {
        when(cursoService.buscarPorId(1)).thenReturn(Optional.of(cursoEjemplo));
        mockMvc.perform(post("/api/v1/carrito/agregar/1")
        .param("usuarioId", "123"));

        
        mockMvc.perform(get("/api/v1/carrito")
        .param("usuarioId", "123"))
                .andExpect(status().isOk());
        jsonPath("$.mensaje").value("Curso agregado al carrito");
        jsonPath("$.carrito[0].nombre").value("Curso de Python");
        jsonPath("$.total").value(1000.0);
    }

    @Test
    void eliminarCurso_delCarrito_debeEliminarCorrectamente() throws Exception {
        when(cursoService.buscarPorId(1)).thenReturn(Optional.of(cursoEjemplo));
        mockMvc.perform(post("/api/v1/carrito/agregar/1")
                .param("usuarioId", "123"));

        mockMvc.perform(delete("/api/v1/carrito/eliminar/1")
                .param("usuarioId", "123"))
                .andExpect(status().isOk());
                jsonPath("$.mensaje").value("Curso eliminado del carrito");
                jsonPath("$.carrito[0].nombre").value("Curso de Python");
                jsonPath("$.total").value(1000.0);
    }

    @Test
    void vaciarCarrito_debeResponderCorrectamente() throws Exception {
        when(cursoService.buscarPorId(1)).thenReturn(Optional.of(cursoEjemplo));
        mockMvc.perform(post("/api/v1/carrito/agregar/1")
                .param("usuarioId", "123"));

        mockMvc.perform(delete("/api/v1/carrito/vaciar")
            .param("usuarioId", "123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Carrito vaciado correctamente"));
    }

  
}
