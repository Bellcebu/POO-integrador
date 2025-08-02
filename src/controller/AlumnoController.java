package controller;

import model.Alumno;
import model.Facultad;
import java.util.List;

public class AlumnoController {

    public List<Alumno> obtenerTodos() {
        return Facultad.getInstance().getAlumnos();
    }
}