package com.example.edutech.Repository;

import com.example.edutech.Model.Curso;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CursoRepository  {
    private List<Curso> listaCursos = new ArrayList<>();

    //Metodo que retonrna los cursos
    public List<Curso>obtenerCursos(){
        return listaCursos;
    }

    public Curso buscarPorId(Long id){
        for (Curso curso : listaCursos) {
            if(curso.getId() == id){
                return curso;
            }
        }
        return null;
    }

    public Curso buscarporNombre(String nombre){
        for (Curso curso : listaCursos) {
            if(curso.getNombre() == nombre){
                return curso;
            }
        }
        return null;
    }

    public Curso guardar (Curso cso){
        //Generar ID secuencial
        long nuevoId  = 1;
        for (Curso c : listaCursos) {
            if (c.getId() >= nuevoId){
                nuevoId = c.getId()+1;
            }
        }
        //crear nueva instancia con los datos del curso recibido
        Curso curso = new Curso ();
        curso.setId((int) nuevoId); // ID generado de manera automática
        curso.setNombre(cso.getNombre());
        curso.setCupos(cso.getCupos());
        curso.setDescripcion(cso.getDescripcion());
        curso.setPrecio(cso.getPrecio());
        
        //Agrega nuevo curso
        listaCursos.add(curso);

        return curso;
    }




    public void notificaciones(Curso cso){
        //apartado de notificaciones debería mostrar los cursos
        // que se inscribieron
        //Además si hay problemas con inscripcion debería capturar el curso(?
        Curso curso = new Curso();
        //curso.setId((int) nuevoId()); 
        curso.setNombre(cso.getNombre());
        curso.setDescripcion(cso.getDescripcion());
        curso.setCupos(cso.getCupos());
        curso.setPrecio(cso.getPrecio());
        
        listaCursos.add(curso);
        //mostrar los cursos con problemas
        //con un equivalente a sout, quizás con un try catch
        //añadir-mostrar el estado de una solicitud(?

    }

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
