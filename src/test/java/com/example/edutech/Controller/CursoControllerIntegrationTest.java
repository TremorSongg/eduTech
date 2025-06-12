package com.example.edutech.Controller;

import com.example.edutech.Model.Curso;
import com.example.edutech.Service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.doNothing;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(CursoController.class)
public class CursoControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarLibros_debeRetornarListaJson() throws Exception {
        List<Curso> cursos = List.of(
                new Curso(1, "python", "python desde cero", 30,100000.0),
                new Curso(2, "java", "java desde cero", 25,100000.0)
        );

        when(cursoService.obtenerCursos()).thenReturn(cursos);

        mockMvc.perform(get("/api/v1/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("python"));
    }

    @Test
    void agregarLibro_debeGuardarYRetornarLibro() throws Exception {
        Curso curso = new Curso(3, "C++", "C++ desde cero", 10,100000.0);

        when(cursoService.guardar(any(Curso.class))).thenReturn(curso);

        mockMvc.perform(post("/api/v1/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(curso)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("C++"));
    }

    @Test
    void buscarLibro_porId_existente() throws Exception {
        Curso curso = new Curso(4, "Html", "Html desde cero", 1,100000.0);

        when(cursoService.buscarPorId(4)).thenReturn(Optional.of(curso));

        mockMvc.perform(get("/api/v1/cursos/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Html"));
    }

    @Test
    void eliminarLibro_existente() throws Exception {
        doNothing().when(cursoService).eliminar(3);

        mockMvc.perform(delete("/api/v1/cursos/3"))
                .andExpect(status().isOk());
    }

    // no se como hacer este 
    
    // @Test
    // void totalLibrosV2_debeRetornarCantidad() throws Exception {
    //     when(libroService.totalLibrosV2()).thenReturn(10);

    //     mockMvc.perform(get("/api/v1/libros/total"))
    //             .andExpect(status().isOk())
    //             .andExpect(content().string("10"));
    // }

}
