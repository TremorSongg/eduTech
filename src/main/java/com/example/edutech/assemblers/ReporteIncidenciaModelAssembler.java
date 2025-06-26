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
    public @NonNull EntityModel<ReporteIncidencia> toModel(ReporteIncidencia reporte) {
        return EntityModel.of(reporte,
            linkTo(methodOn(ReporteIncidenciaControllerV2.class).getByUsuario(reporte.getUsuarioId())).withRel("reportes-usuario"),
            linkTo(methodOn(ReporteIncidenciaControllerV2.class).cambiarEstado(reporte.getId(), null)).withRel("cambiar-estado"),
            linkTo(methodOn(ReporteIncidenciaControllerV2.class).crearSolicitud(null)).withRel("crear")
        );
    }
}
