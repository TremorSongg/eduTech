package com.example.edutech.Repository;

import com.example.edutech.Model.Curso;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CursoRepository  {
    private List<Curso> listaCursos = new ArrayList<>();

    public Curso actualizar(Curso c) {
        int id = 0;
        int idPosicion = 0;
        for (int i = 0; i < listaCursos.size(); i++) {
            if ( listaCursos.get(i).getId() == c.getId()) {
                id = c.getId();
                idPosicion = i;
            }
        }
        Curso curso1 = new Curso();
        curso1.setId(id);
        curso1.setNombre(c.getNombre());
        curso1.setDescripcion(c.getDescripcion());
        curso1.setCupos(c.getCupos());
        curso1.setPrecio(c.getPrecio());

        listaCursos.set(idPosicion,curso1);
        return curso1;
    }



    public Curso controlStock(Curso c) {
        int id = 0;
        int idPosicion = 0;
        for (int i = 0; i < listaCursos.size(); i++) {
            if ( listaCursos.get(i).getId() == c.getId()) {
                id = c.getId();
                idPosicion = i;
            }
        }
        int cupo = c.getCupos();
        cupo = cupo - 1;
        Curso curso1 = new Curso();
        curso1.setId(id);
        curso1.setNombre(c.getNombre());
        curso1.setDescripcion(c.getDescripcion());
        curso1.setCupos(cupo);
        curso1.setPrecio(c.getPrecio());

        listaCursos.set(idPosicion,curso1);
        return curso1;
    }

}
