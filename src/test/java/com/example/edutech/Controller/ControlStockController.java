package com.example.edutech.Controller;

// import com.example.edutech.Model.Curso;
// import com.example.edutech.Service.ControlStockService;
// import com.example.edutech.Service.CursoService;


// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.beans.factory.annotation.Autowired;

// import org.springframework.test.web.servlet.MockMvc;

// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// @WebMvcTest(ControlStockController.class)
// public class ControlStockController {
// @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private CursoService cursoService;

//     @MockBean
//     private ControlStockService controlStockService;

//     @Test
//     void agregarLibro_debeGuardarYRestarUnCupo() throws Exception {
//         Curso curso = new Curso(3, "C++", "C++ desde cero", 10, 100000.0);
//         Curso cursoConCupoReducido = new Curso(3, "C++", "C++ desde cero", 9, 100000.0);

//         when(controlStockService.controlStock(3)).thenReturn(cursoConCupoReducido);

//         mockMvc.perform(post("/api/v1/Stock/controlStock/3"))
//             .andDo(print())
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.cupos").value(9)) 
//             .andExpect(jsonPath("$.titulo").value("C++"));
//     }
// }
