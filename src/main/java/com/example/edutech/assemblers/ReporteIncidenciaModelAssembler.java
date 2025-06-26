package com.example.edutech.assemblers;

import com.example.edutech.Model.ReporteIncidencia;
import com.example.edutech.Controller.ReporteIncidenciaControllerV2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

@Component
public class ReporteIncidenciaModelAssembler implements RepresentationModelAssembler<ReporteIncidencia, EntityModel<ReporteIncidencia>> {

    @Override
    public @NonNull EntityModel<ReporteIncidencia> toModel(@NonNull ReporteIncidencia reporte) {
        return EntityModel.of(reporte,
            // Self link para el reporte de incidencia individual
            linkTo(methodOn(ReporteIncidenciaControllerV2.class).getReporteById(reporte.getId())).withSelfRel(),
            linkTo(methodOn(ReporteIncidenciaControllerV2.class).getByUsuario(reporte.getUsuarioId())).withRel("reportes-usuario"),
            linkTo(methodOn(ReporteIncidenciaControllerV2.class).cambiarEstado(reporte.getId(), null)).withRel("cambiar-estado").withTitle("Cambiar el estado de este reporte. Requiere un cuerpo JSON con 'estado'."),
            // El link 'crear' para un nuevo reporte, aunque un POST, puede ser útil aquí.
            // Se le añade un título explicativo.
            linkTo(methodOn(ReporteIncidenciaControllerV2.class).crearSolicitud(null)).withRel("crear-reporte").withTitle("Crear un nuevo reporte de incidencia. Requiere un cuerpo JSON con 'usuarioId' y 'mensaje'.")
        );
    }
}
