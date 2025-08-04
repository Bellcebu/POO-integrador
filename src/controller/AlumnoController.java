package controller;

import model.*;
import model.Facultad;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import static model.Alumno.buscarPorLegajo;

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

    // AGREGAR en AlumnoController.java:

    public List<Carrera> obtenerCarrerasDelAlumno(String legajo) {
        List<Carrera> carrerasAlumno = new ArrayList<>();

        for (Carrera carrera : Facultad.getInstance().getCarreras()) {
            for (Alumno alumno : carrera.getAlumnos()) {
                if (alumno.getLegajo().equals(legajo)) {
                    carrerasAlumno.add(carrera);
                    break;
                }
            }
        }

        return carrerasAlumno;
    }

    public List<Materia> obtenerMateriasDisponibles(String legajoAlumno, String codigoCarrera) {
        List<Materia> materiasDisponibles = new ArrayList<>();

        // Buscar alumno
        Alumno alumno = Alumno.buscarPorLegajo(legajoAlumno, Facultad.getInstance().getAlumnos());
        if (alumno == null) {
            return materiasDisponibles;
        }

        // Buscar carrera
        Carrera carrera = null;
        for (Carrera c : Facultad.getInstance().getCarreras()) {
            if (c.getCodigo().equals(codigoCarrera)) {
                carrera = c;
                break;
            }
        }

        if (carrera == null) {
            return materiasDisponibles;
        }

        // Filtrar materias que puede cursar
        for (Materia materia : carrera.getMaterias()) {
            // No está ya inscripto
            if (!alumno.estaInscriptoEn(materia)) {
                // Verificar si puede cursar según el plan
                if (carrera.puedeInscribirseA(materia, alumno)) {
                    materiasDisponibles.add(materia);
                }
            }
        }

        return materiasDisponibles;
    }

    public boolean inscribirAlumnoAMaterias(String legajoAlumno, List<String> codigosMaterias) {
        // Buscar alumno
        Alumno alumno = Alumno.buscarPorLegajo(legajoAlumno, Facultad.getInstance().getAlumnos());
        if (alumno == null) {
            return false;
        }

        // Inscribir a cada materia
        for (String codigoMateria : codigosMaterias) {
            Materia materia = Materia.buscarPorCodigo(codigoMateria, Facultad.getInstance().getMaterias());
            if (materia != null) {
                // Crear inscripción
                InscripcionMateria inscripcion = new InscripcionMateria(alumno, materia);
                alumno.agregarInscripcion(materia, inscripcion);
            }
        }

        // Actualizar archivo de inscripciones
        persistence.ArchivoInscripciones.actualizar();

        return true;
    }

    public Alumno buscarPorLegajo(String legajo) {
        return Alumno.buscarPorLegajo(legajo, Facultad.getInstance().getAlumnos());
    }

    public List<InscripcionMateria> obtenerTodasLasInscripciones(String legajo) {
        Alumno alumno = buscarPorLegajo(legajo);
        if (alumno == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(alumno.getInscripciones().values());
    }

    public Map<String, Integer> obtenerEstadisticasAlumno(String legajo) {
        List<InscripcionMateria> inscripciones = obtenerTodasLasInscripciones(legajo);
        Map<String, Integer> estadisticas = new HashMap<>();

        int totalMaterias = inscripciones.size();
        int promocionadas = 0;
        int finalesAprobados = 0;
        int parcialesAprobados = 0;
        int enCurso = 0;
        int materiasAprobadas = 0;

        for (InscripcionMateria inscripcion : inscripciones) {
            if (inscripcion.promociono()) {
                promocionadas++;
                materiasAprobadas++;
            } else if (inscripcion.aproboFinal()) {
                finalesAprobados++;
                materiasAprobadas++;
            } else if (inscripcion.aproboParcial()) {
                parcialesAprobados++;
            } else {
                enCurso++;
            }
        }

        estadisticas.put("total", totalMaterias);
        estadisticas.put("promocionadas", promocionadas);
        estadisticas.put("finales", finalesAprobados);
        estadisticas.put("parciales", parcialesAprobados);
        estadisticas.put("curso", enCurso);
        estadisticas.put("aprobadas", materiasAprobadas);

        return estadisticas;
    }
}