package com.example.edutech.Controller;

import com.example.edutech.Model.ReporteIncidencia;
import com.example.edutech.Service.ReporteIncidenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.MockMvc;


import com.example.edutech.Model.EstadoSolicitud;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebMvcTest(ReporteIncidenciaController.class)
public class ReporteIncidenciaControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteIncidenciaService reporteIncidenciaService;

    private ReporteIncidencia reporte;


    @BeforeEach
    void setUp() {
        reporte = new ReporteIncidencia(1,123,"Error en plataforma",EstadoSolicitud.PENDIENTE,LocalDateTime.now());
    }    

    @Test
    void crearSolicitud_debeCrearYRetornarReporte() throws Exception {
        Map<String, Object> payload = Map.of(
            "usuarioId", 123,
            "mensaje", "Error en plataforma"
        );

        when(reporteIncidenciaService.crearSolicitud(any(ReporteIncidencia.class))).thenReturn(reporte);

        mockMvc.perform(post("/api/v1/reportes/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(123))
                .andExpect(jsonPath("$.mensaje").value("Error en plataforma"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }


    @Test
    void cambiarEstado_debeActualizarEstadoCorrectamente() throws Exception {
        reporte.setEstado(EstadoSolicitud.RESUELTO);

        Map<String, String> payload = new HashMap<>();
        payload.put("estado", "RESUELTO");

        when(reporteIncidenciaService.cambiarEstado(1, EstadoSolicitud.RESUELTO)).thenReturn(reporte);

        mockMvc.perform(put("/api/v1/reportes/estado/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("RESUELTO"));
    }

    @Test
    void getByUsuario_debeRetornarListaDeReportes() throws Exception {
        ReporteIncidencia otra = new ReporteIncidencia();
        otra.setId(2);
        otra.setUsuarioId(123);
        otra.setMensaje("Falla en curso");
        otra.setEstado(EstadoSolicitud.PENDIENTE);
        otra.setFechaCreacion(LocalDateTime.now());

        when(reporteIncidenciaService.obtenerPorUsuario(123)).thenReturn(List.of(reporte, otra));

        mockMvc.perform(get("/api/v1/reportes/usuario/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mensaje").value("Error en plataforma"))
                .andExpect(jsonPath("$[1].estado").value("PENDIENTE"));
    }
}
