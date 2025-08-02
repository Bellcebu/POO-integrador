package controller;

import model.Alumno;
import model.Facultad;

import java.util.ArrayList;
import java.util.List;

public class AlumnoController {

    public List<Alumno> obtenerTodos() {
        return Facultad.getInstance().getAlumnos();
    }

    public boolean agregarAccion(String nombre, String legajo) {
        Alumno nuevoAlumno = new Alumno(nombre, legajo);

        for (Alumno a : Facultad.getInstance().getAlumnos()) {
            if (a.equals(nuevoAlumno)) {
                return false;
            }
        }

        Facultad.getInstance().getAlumnos().add(nuevoAlumno);
        persistence.ArchivoAlumnos.agregar(nuevoAlumno);

        return true;
    }

    public List<Alumno> buscarAlumnos(String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return new ArrayList<>(Facultad.getInstance().getAlumnos());
        }

        String busqueda = textoBusqueda.trim().toLowerCase();
        List<Alumno> resultados = new ArrayList<>();

        for (Alumno alumno : Facultad.getInstance().getAlumnos()) {
            if (alumno.getNombre().toLowerCase().contains(busqueda) ||
                    alumno.getLegajo().toLowerCase().contains(busqueda)) {
                resultados.add(alumno);
            }
        }
        return resultados;
    }

    public List<Alumno> ordenarAlumnos(boolean ordenAZ) {
        List<Alumno> alumnosOrdenados = new ArrayList<>(Facultad.getInstance().getAlumnos());
        if (ordenAZ) {
            alumnosOrdenados.sort((a1, a2) -> a1.getNombre().compareToIgnoreCase(a2.getNombre()));
        } else {
            alumnosOrdenados.sort((a1, a2) -> a2.getNombre().compareToIgnoreCase(a1.getNombre()));
        }
        return alumnosOrdenados;
    }
}