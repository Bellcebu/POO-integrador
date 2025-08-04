package controller;

import model.*;
import persistence.ArchivoMaterias;
import java.util.List;
import java.util.ArrayList;


public class MateriaController {

    public List<Materia> obtenerTodas() {
        return Facultad.getInstance().getMaterias();
    }

    public List<Materia> buscarMaterias(String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return new ArrayList<>(Facultad.getInstance().getMaterias());
        }

        String busqueda = textoBusqueda.trim().toLowerCase();
        List<Materia> resultados = new ArrayList<>();

        for (Materia materia : Facultad.getInstance().getMaterias()) {
            if (materia.getNombre().toLowerCase().contains(busqueda) ||
                    materia.getCodigo().toLowerCase().contains(busqueda)) {
                resultados.add(materia);
            }
        }

        return resultados;
    }

    public boolean agregarAccion(String codigo, String nombre, int cuatrimestre,
                                 boolean esObligatoria, String codigoCarrera) {
        // Validar que no exista el código
        for (Materia m : Facultad.getInstance().getMaterias()) {
            if (m.getCodigo().equals(codigo)) {
                return false; // Ya existe
            }
        }

        // Validar que la carrera exista
        boolean carreraExiste = false;
        for (Carrera c : Facultad.getInstance().getCarreras()) {
            if (c.getCodigo().equals(codigoCarrera)) {
                carreraExiste = true;
                break;
            }
        }

        if (!carreraExiste) {
            return false; // Carrera no existe
        }

        // Crear la materia
        Materia nuevaMateria = new Materia(codigo, nombre, cuatrimestre, esObligatoria, codigoCarrera);

        // Agregar a memoria
        Facultad.getInstance().getMaterias().add(nuevaMateria);

        // Guardar en archivo
        persistence.ArchivoMaterias.agregar(nuevaMateria);

        return true;
    }

    public boolean agregarCorrelativasAMateria(String codigoMateria, List<String> codigosCorrelativas) {
        // Buscar la materia
        Materia materia = null;
        for (Materia m : Facultad.getInstance().getMaterias()) {
            if (m.getCodigo().equals(codigoMateria)) {
                materia = m;
                break;
            }
        }

        if (materia == null) {
            return false;
        }

        // Buscar y agregar las correlativas
        for (String codigoCorrelativa : codigosCorrelativas) {
            Materia correlativa = Materia.buscarPorCodigo(codigoCorrelativa, Facultad.getInstance().getMaterias());
            if (correlativa != null) {
                // Verificar que no esté ya como correlativa
                if (!materia.getCorrelativas().contains(correlativa)) {
                    materia.agregarCorrelativa(correlativa);
                }
            }
        }

        // Actualizar archivo
        ArchivoMaterias.actualizar(materia);

        return true;
    }

    public Materia buscarMateriaPorCodigo(String codigo) {
        for (Materia materia : Facultad.getInstance().getMaterias()) {
            if (materia.getCodigo().equals(codigo)) {
                return materia;
            }
        }
        return null;
    }

    public List<InscripcionMateria> obtenerInscripcionesPorMateria(String codigoMateria) {
        List<InscripcionMateria> inscripciones = new ArrayList<>();

        for (Alumno alumno : Facultad.getInstance().getAlumnos()) {
            for (InscripcionMateria inscripcion : alumno.getInscripciones().values()) {
                if (inscripcion.getMateria().getCodigo().equals(codigoMateria)) {
                    inscripciones.add(inscripcion);
                }
            }
        }

        return inscripciones;
    }

    public boolean actualizarInscripcion(String legajoAlumno, String codigoMateria,
                                         boolean parcialAprobado, boolean finalAprobado, boolean promocionado) {
        // Buscar el alumno
        Alumno alumno = Alumno.buscarPorLegajo(legajoAlumno, Facultad.getInstance().getAlumnos());
        if (alumno == null) {
            return false;
        }

        // Buscar la inscripción
        InscripcionMateria inscripcion = null;
        for (InscripcionMateria insc : alumno.getInscripciones().values()) {
            if (insc.getMateria().getCodigo().equals(codigoMateria)) {
                inscripcion = insc;
                break;
            }
        }

        if (inscripcion == null) {
            return false;
        }

        // Actualizar estados (resetear primero)
        inscripcion = new InscripcionMateria(alumno, inscripcion.getMateria());

        if (parcialAprobado) inscripcion.aprobarParcial();
        if (finalAprobado) inscripcion.aprobarFinal();
        if (promocionado) inscripcion.otorgarPromocion();

        // Reemplazar en el alumno
        alumno.getInscripciones().put(inscripcion.getMateria(), inscripcion);

        // Actualizar archivo de inscripciones (necesitaremos crear este método)
        persistence.ArchivoInscripciones.actualizar();

        return true;
    }
}