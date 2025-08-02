package controller;

import model.Alumno;
import model.Facultad;
import java.util.List;

public class AlumnoController {

    public List<Alumno> obtenerTodos() {
        return Facultad.getInstance().getAlumnos();
    }

    public boolean agregarAccion(String nombre, String legajo) {
        Alumno nuevoAlumno = new Alumno(nombre, legajo);
        boolean agregado = Facultad.getInstance().agregarAlumno(nuevoAlumno);

        if (agregado) {
            persistence.ArchivoAlumnos.agregar(nuevoAlumno);
        }
        return agregado;
    }

    public boolean editarAccion(String legajoViejo, String nombreNuevo, String legajoNuevo) {
        Alumno nuevoAlumno = new Alumno(nombreNuevo, legajoNuevo);
        boolean editado = Facultad.getInstance().editarAlumno(legajoViejo, nuevoAlumno);

        if (editado) {
            persistence.ArchivoAlumnos.editar(legajoViejo, nuevoAlumno);
        }
        return editado;
    }

    public boolean eliminarAccion(String legajo) {
        boolean eliminar = Facultad.getInstance().eliminarAlumno(legajo);

        if (eliminar) {
            persistence.ArchivoAlumnos.eliminar(legajo);
        }
        return eliminar;
    }

    public Alumno buscarPorLegajo(String legajo) {
        return Facultad.getInstance().getAlumnos().stream()
                .filter(a -> a.getLegajo().equals(legajo))
                .findFirst()
                .orElse(null);
    }
}