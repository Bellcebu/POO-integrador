package controller;

import model.*;
import java.io.*;
import java.util.*;

public class InscripcionController {
    private static final String ARCHIVO = "data/inscripciones.txt";
    private static final String SEPARADOR = "☆";

    /**
     * Inscribir alumno en una materia con todas las validaciones
     */
    public String inscribirAlumno(String legajoAlumno, String codigoMateria) {
        // Validaciones básicas
        if (legajoAlumno == null || legajoAlumno.trim().isEmpty()) {
            return "Error: El legajo del alumno es obligatorio";
        }
        if (codigoMateria == null || codigoMateria.trim().isEmpty()) {
            return "Error: El código de materia es obligatorio";
        }

        // Cargar datos necesarios
        AlumnoController alumnoController = new AlumnoController();
        MateriaController materiaController = new MateriaController();
        CarreraController carreraController = new CarreraController();

        // Buscar alumno
        Alumno alumno = alumnoController.buscarPorLegajo(legajoAlumno.trim());
        if (alumno == null) {
            return "Error: No se encontró el alumno con legajo " + legajoAlumno;
        }

        // Buscar materia
        Materia materia = materiaController.buscarPorCodigo(codigoMateria.trim());
        if (materia == null) {
            return "Error: No se encontró la materia con código " + codigoMateria;
        }

        // Verificar si ya está inscripto
        List<InscripcionMateria> inscripciones = cargarInscripciones();
        for (InscripcionMateria insc : inscripciones) {
            if (insc.getAlumno().getLegajo().equals(legajoAlumno.trim()) &&
                    insc.getMateria().getCodigo().equals(codigoMateria.trim())) {
                return "Error: El alumno ya está inscripto en esta materia";
            }
        }

        // Buscar la carrera de la materia
        Carrera carrera = carreraController.buscarPorCodigo(materia.getCodigoCarrera());
        if (carrera == null) {
            return "Error: No se encontró la carrera de la materia";
        }

        // Verificar que el alumno esté inscripto en la carrera
        boolean estaInscriptoEnCarrera = false;
        for (Alumno a : carrera.getAlumnos()) {
            if (a.getLegajo().equals(legajoAlumno.trim())) {
                estaInscriptoEnCarrera = true;
                break;
            }
        }

        if (!estaInscriptoEnCarrera) {
            return "Error: El alumno no está inscripto en la carrera " + carrera.getNombre();
        }

        // Cargar las inscripciones del alumno para verificar correlativas
        cargarInscripcionesAlumno(alumno);

        // Verificar si puede cursar según el plan de estudios
        if (!carrera.puedeInscribirseA(materia, alumno)) {
            return "Error: El alumno no cumple los requisitos del " +
                    carrera.getPlanEstudio().getClass().getSimpleName() + " para cursar esta materia";
        }

        // Crear inscripción
        InscripcionMateria nuevaInscripcion = new InscripcionMateria(alumno, materia);
        inscripciones.add(nuevaInscripcion);

        // Guardar
        if (guardarInscripciones(inscripciones)) {
            return null; // Éxito
        } else {
            return "Error: No se pudo guardar la inscripción";
        }
    }

    /**
     * Actualizar estado de una inscripción
     */
    public String actualizarInscripcion(String legajoAlumno, String codigoMateria,
                                        boolean parcialAprobado, boolean finalAprobado, boolean promocionado) {
        // Validaciones lógicas
        if (promocionado && !parcialAprobado) {
            return "Error: No se puede promocionar sin aprobar el parcial";
        }
        if (finalAprobado && !parcialAprobado) {
            return "Error: No se puede aprobar el final sin aprobar el parcial";
        }
        if (promocionado && finalAprobado) {
            return "Error: Una materia no puede estar promocionada y con final aprobado a la vez";
        }

        List<InscripcionMateria> inscripciones = cargarInscripciones();

        for (InscripcionMateria insc : inscripciones) {
            if (insc.getAlumno().getLegajo().equals(legajoAlumno) &&
                    insc.getMateria().getCodigo().equals(codigoMateria)) {

                // Actualizar estados
                if (parcialAprobado) insc.aprobarParcial();
                if (finalAprobado) insc.aprobarFinal();
                if (promocionado) {
                    insc.aprobarParcial(); // Asegurar que tenga parcial
                    insc.otorgarPromocion();
                }

                if (guardarInscripciones(inscripciones)) {
                    return null; // Éxito
                } else {
                    return "Error: No se pudo guardar los cambios";
                }
            }
        }

        return "Error: No se encontró la inscripción";
    }

    /**
     * Obtener alumnos inscriptos en una materia
     */
    public List<InscripcionMateria> obtenerInscriptosPorMateria(String codigoMateria) {
        List<InscripcionMateria> inscripciones = cargarInscripciones();
        List<InscripcionMateria> resultado = new ArrayList<>();

        for (InscripcionMateria insc : inscripciones) {
            if (insc.getMateria().getCodigo().equals(codigoMateria)) {
                resultado.add(insc);
            }
        }

        return resultado;
    }

    /**
     * Obtener inscripciones de un alumno
     */
    public List<InscripcionMateria> obtenerInscripcionesPorAlumno(String legajoAlumno) {
        List<InscripcionMateria> inscripciones = cargarInscripciones();
        List<InscripcionMateria> resultado = new ArrayList<>();

        for (InscripcionMateria insc : inscripciones) {
            if (insc.getAlumno().getLegajo().equals(legajoAlumno)) {
                resultado.add(insc);
            }
        }

        return resultado;
    }

    /**
     * Cargar inscripciones de un alumno específico en su objeto
     */
    private void cargarInscripcionesAlumno(Alumno alumno) {
        List<InscripcionMateria> todasLasInscripciones = cargarInscripciones();

        for (InscripcionMateria insc : todasLasInscripciones) {
            if (insc.getAlumno().getLegajo().equals(alumno.getLegajo())) {
                alumno.agregarInscripcion(insc.getMateria(), insc);
            }
        }
    }

    /**
     * Obtener materias que puede cursar un alumno en una carrera
     */
    public List<Materia> obtenerMateriasDisponibles(String legajoAlumno, String codigoCarrera) {
        List<Materia> disponibles = new ArrayList<>();

        AlumnoController alumnoController = new AlumnoController();
        CarreraController carreraController = new CarreraController();

        Alumno alumno = alumnoController.buscarPorLegajo(legajoAlumno);
        Carrera carrera = carreraController.buscarPorCodigo(codigoCarrera);

        if (alumno == null || carrera == null) {
            return disponibles;
        }

        // Cargar inscripciones del alumno
        cargarInscripcionesAlumno(alumno);

        // Verificar cada materia de la carrera
        for (Materia materia : carrera.getMaterias()) {
            // Si no está inscripto y puede cursarla
            if (!alumno.estaInscriptoEn(materia) && carrera.puedeInscribirseA(materia, alumno)) {
                disponibles.add(materia);
            }
        }

        return disponibles;
    }

    /**
     * Cargar todas las inscripciones
     */
    public List<InscripcionMateria> cargarInscripciones() {
        List<InscripcionMateria> inscripciones = new ArrayList<>();

        try {
            File archivo = new File(ARCHIVO);
            if (!archivo.exists()) {
                archivo.getParentFile().mkdirs();
                return inscripciones;
            }

            BufferedReader reader = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    InscripcionMateria inscripcion = fromString(linea);
                    if (inscripcion != null) {
                        inscripciones.add(inscripcion);
                    }
                }
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Error al cargar inscripciones: " + e.getMessage());
        }

        return inscripciones;
    }

    /**
     * Guardar inscripciones
     */
    public boolean guardarInscripciones(List<InscripcionMateria> inscripciones) {
        try {
            File archivo = new File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));

            for (InscripcionMateria inscripcion : inscripciones) {
                writer.write(toString(inscripcion));
                writer.newLine();
            }

            writer.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error al guardar inscripciones: " + e.getMessage());
            return false;
        }
    }

    /**
     * Convertir inscripción a string para persistencia
     */
    private String toString(InscripcionMateria inscripcion) {
        StringBuilder sb = new StringBuilder();
        sb.append(inscripcion.getAlumno().getLegajo()).append(SEPARADOR);
        sb.append(inscripcion.getMateria().getCodigo()).append(SEPARADOR);
        sb.append(inscripcion.aproboParcial()).append(SEPARADOR);
        sb.append(inscripcion.aproboFinal()).append(SEPARADOR);
        sb.append(inscripcion.promociono());
        return sb.toString();
    }

    /**
     * Crear inscripción desde string
     */
    private InscripcionMateria fromString(String linea) {
        try {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 5) {
                String legajoAlumno = partes[0].trim();
                String codigoMateria = partes[1].trim();
                boolean parcialAprobado = Boolean.parseBoolean(partes[2].trim());
                boolean finalAprobado = Boolean.parseBoolean(partes[3].trim());
                boolean promocionado = Boolean.parseBoolean(partes[4].trim());

                // Buscar alumno y materia
                AlumnoController alumnoController = new AlumnoController();
                MateriaController materiaController = new MateriaController();

                Alumno alumno = alumnoController.buscarPorLegajo(legajoAlumno);
                Materia materia = materiaController.buscarPorCodigo(codigoMateria);

                if (alumno != null && materia != null) {
                    InscripcionMateria inscripcion = new InscripcionMateria(alumno, materia);

                    // Establecer estados
                    if (parcialAprobado) inscripcion.aprobarParcial();
                    if (finalAprobado) inscripcion.aprobarFinal();
                    if (promocionado) inscripcion.otorgarPromocion();

                    return inscripcion;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al parsear inscripción: " + linea);
        }
        return null;
    }

    /**
     * Eliminar inscripción
     */
    public String eliminarInscripcion(String legajoAlumno, String codigoMateria) {
        List<InscripcionMateria> inscripciones = cargarInscripciones();

        boolean eliminado = inscripciones.removeIf(insc ->
                insc.getAlumno().getLegajo().equals(legajoAlumno) &&
                        insc.getMateria().getCodigo().equals(codigoMateria)
        );

        if (eliminado) {
            if (guardarInscripciones(inscripciones)) {
                return null; // Éxito
            } else {
                return "Error: No se pudo guardar los cambios";
            }
        }

        return "Error: No se encontró la inscripción";
    }

    /**
     * Verificar si un alumno finalizó una carrera
     */
    public boolean verificarFinalizacionCarrera(String legajoAlumno, String codigoCarrera) {
        AlumnoController alumnoController = new AlumnoController();
        CarreraController carreraController = new CarreraController();

        Alumno alumno = alumnoController.buscarPorLegajo(legajoAlumno);
        Carrera carrera = carreraController.buscarPorCodigo(codigoCarrera);

        if (alumno == null || carrera == null) {
            return false;
        }

        // Cargar inscripciones del alumno
        cargarInscripcionesAlumno(alumno);

        // Verificar si finalizó la carrera
        return carrera.finalizoCarrera(alumno);
    }

    /**
     * Obtener reporte de estado académico
     */
    public String obtenerReporteAcademico(String legajoAlumno, String codigoCarrera) {
        StringBuilder reporte = new StringBuilder();

        AlumnoController alumnoController = new AlumnoController();
        CarreraController carreraController = new CarreraController();

        Alumno alumno = alumnoController.buscarPorLegajo(legajoAlumno);
        Carrera carrera = carreraController.buscarPorCodigo(codigoCarrera);

        if (alumno == null || carrera == null) {
            return "Error: Alumno o carrera no encontrados";
        }

        cargarInscripcionesAlumno(alumno);

        reporte.append("=== REPORTE ACADÉMICO ===\n");
        reporte.append("Alumno: ").append(alumno.mostrarInfo()).append("\n");
        reporte.append("Carrera: ").append(carrera.mostrarInfo()).append("\n\n");

        int obligatoriasAprobadas = 0;
        int obligatoriasPendientes = 0;
        int optativasAprobadas = 0;
        int optativasPendientes = 0;

        for (Materia materia : carrera.getMaterias()) {
            InscripcionMateria insc = alumno.getInscripcion(materia);

            if (materia.esObligatoria()) {
                if (insc != null && insc.aproboMateria()) {
                    obligatoriasAprobadas++;
                } else {
                    obligatoriasPendientes++;
                }
            } else {
                if (insc != null && insc.aproboMateria()) {
                    optativasAprobadas++;
                } else {
                    optativasPendientes++;
                }
            }
        }

        reporte.append("Obligatorias aprobadas: ").append(obligatoriasAprobadas).append("\n");
        reporte.append("Obligatorias pendientes: ").append(obligatoriasPendientes).append("\n");
        reporte.append("Optativas aprobadas: ").append(optativasAprobadas).append("\n");
        reporte.append("Optativas necesarias: ").append(carrera.getCantidadOptativasNecesarias()).append("\n\n");

        if (carrera.finalizoCarrera(alumno)) {
            reporte.append("¡CARRERA FINALIZADA!");
        } else {
            reporte.append("Carrera en curso");
        }

        return reporte.toString();
    }
}