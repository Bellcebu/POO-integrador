package controller;

import model.*;
import java.util.*;

public class SistemaController {

    private static SistemaController instance;
    private AlumnoController alumnoController;
    private CarreraController carreraController;
    private MateriaController materiaController;
    private InscripcionController inscripcionController;

    private SistemaController() {
        this.alumnoController = new AlumnoController();
        this.carreraController = new CarreraController();
        this.materiaController = new MateriaController();
        this.inscripcionController = new InscripcionController();
    }

    public static SistemaController getInstance() {
        if (instance == null) {
            instance = new SistemaController();
        }
        return instance;
    }

    /**
     * Cargar todos los datos del sistema en el orden correcto
     */
    public void cargarSistemaCompleto() {
        System.out.println("Iniciando carga del sistema...");

        // 1. Cargar alumnos (sin dependencias)
        List<Alumno> alumnos = alumnoController.cargarAlumnos();
        System.out.println("Alumnos cargados: " + alumnos.size());

        // 2. Cargar carreras básicas (sin relaciones)
        // Esto requiere que cargarCarreras no intente cargar materias aún
        List<Carrera> carreras = carreraController.cargarCarreras();
        System.out.println("Carreras cargadas: " + carreras.size());

        // 3. Cargar materias (con sus correlativas y carrera)
        List<Materia> materias = materiaController.cargarMaterias();
        System.out.println("Materias cargadas: " + materias.size());

        // 4. Re-cargar carreras con todas las relaciones
        carreras = carreraController.cargarCarreras();

        // 5. Cargar inscripciones y actualizar alumnos
        List<InscripcionMateria> inscripciones = inscripcionController.cargarInscripciones();
        System.out.println("Inscripciones cargadas: " + inscripciones.size());

        // 6. Actualizar Facultad
        Facultad facultad = Facultad.getInstance();
        for (Alumno alumno : alumnos) {
            facultad.agregarAlumno(alumno);
        }
        for (Carrera carrera : carreras) {
            facultad.agregarCarrera(carrera);
        }

        System.out.println("Sistema cargado completamente.");
    }

    /**
     * Guardar todos los datos del sistema
     */
    public boolean guardarSistemaCompleto() {
        boolean exito = true;

        System.out.println("Guardando sistema...");

        // Guardar en orden para mantener integridad
        if (!alumnoController.guardarAlumnos(alumnoController.cargarAlumnos())) {
            System.out.println("Error al guardar alumnos");
            exito = false;
        }

        if (!materiaController.guardarMaterias(materiaController.cargarMaterias())) {
            System.out.println("Error al guardar materias");
            exito = false;
        }

        if (!carreraController.guardarCarreras(carreraController.cargarCarreras())) {
            System.out.println("Error al guardar carreras");
            exito = false;
        }

        if (!inscripcionController.guardarInscripciones(inscripcionController.cargarInscripciones())) {
            System.out.println("Error al guardar inscripciones");
            exito = false;
        }

        if (exito) {
            System.out.println("Sistema guardado correctamente.");
        }

        return exito;
    }

    /**
     * Inicializar sistema con datos de prueba
     */
    public void inicializarDatosPrueba() {
        System.out.println("Inicializando datos de prueba...");

        // Crear alumnos
        alumnoController.crearAlumno("Juan Carlos Pérez", "2021001");
        alumnoController.crearAlumno("María Elena García", "2021002");
        alumnoController.crearAlumno("Carlos Alberto Rodríguez", "2021003");
        alumnoController.crearAlumno("Ana Laura Martínez", "2021004");
        alumnoController.crearAlumno("Diego Fernando López", "2021005");

        // Crear carreras
        carreraController.crearCarrera("ING001", "Ingeniería en Sistemas", 3, "PLANA");
        carreraController.crearCarrera("LIC001", "Licenciatura en Biología", 2, "PLANB");
        carreraController.crearCarrera("CON001", "Contador Público", 4, "PLANC");

        // Crear materias para Ingeniería
        materiaController.crearMateria("MAT001", "Matemática I", 1, true, "ING001");
        materiaController.crearMateria("MAT002", "Matemática II", 2, true, "ING001");
        materiaController.crearMateria("PRG001", "Programación I", 1, true, "ING001");
        materiaController.crearMateria("PRG002", "Programación II", 2, true, "ING001");
        materiaController.crearMateria("FIS001", "Física I", 2, true, "ING001");
        materiaController.crearMateria("BD001", "Base de Datos", 3, true, "ING001");
        materiaController.crearMateria("IA001", "Inteligencia Artificial", 8, false, "ING001");
        materiaController.crearMateria("ML001", "Machine Learning", 9, false, "ING001");

        // Agregar correlativas
        materiaController.agregarCorrelativa("MAT002", "MAT001");
        materiaController.agregarCorrelativa("PRG002", "PRG001");
        materiaController.agregarCorrelativa("FIS001", "MAT001");
        materiaController.agregarCorrelativa("BD001", "PRG002");
        materiaController.agregarCorrelativa("IA001", "PRG002");
        materiaController.agregarCorrelativa("IA001", "MAT002");
        materiaController.agregarCorrelativa("ML001", "IA001");

        // Crear materias para Biología
        materiaController.crearMateria("BIO001", "Biología General", 1, true, "LIC001");
        materiaController.crearMateria("BIO002", "Biología Celular", 2, true, "LIC001");
        materiaController.crearMateria("QUI001", "Química General", 1, true, "LIC001");
        materiaController.crearMateria("QUI002", "Química Orgánica", 2, true, "LIC001");
        materiaController.crearMateria("ECO001", "Ecología", 5, false, "LIC001");

        // Agregar correlativas Biología
        materiaController.agregarCorrelativa("BIO002", "BIO001");
        materiaController.agregarCorrelativa("QUI002", "QUI001");
        materiaController.agregarCorrelativa("ECO001", "BIO001");

        // Inscribir alumnos a carreras
        carreraController.inscribirAlumnoACarrera("ING001", "2021001");
        carreraController.inscribirAlumnoACarrera("ING001", "2021002");
        carreraController.inscribirAlumnoACarrera("ING001", "2021003");
        carreraController.inscribirAlumnoACarrera("LIC001", "2021002");
        carreraController.inscribirAlumnoACarrera("LIC001", "2021004");
        carreraController.inscribirAlumnoACarrera("CON001", "2021005");

        // Inscribir alumnos a materias
        inscripcionController.inscribirAlumno("2021001", "MAT001");
        inscripcionController.inscribirAlumno("2021001", "PRG001");
        inscripcionController.inscribirAlumno("2021002", "MAT001");
        inscripcionController.inscribirAlumno("2021002", "PRG001");
        inscripcionController.inscribirAlumno("2021002", "BIO001");
        inscripcionController.inscribirAlumno("2021002", "QUI001");

        // Actualizar algunas notas
        inscripcionController.actualizarInscripcion("2021001", "MAT001", true, true, false);
        inscripcionController.actualizarInscripcion("2021001", "PRG001", true, false, true);
        inscripcionController.actualizarInscripcion("2021002", "MAT001", true, false, false);
        inscripcionController.actualizarInscripcion("2021002", "BIO001", true, true, false);

        System.out.println("Datos de prueba inicializados.");
    }

    /**
     * Generar reporte general del sistema
     */
    public String generarReporteGeneral() {
        StringBuilder reporte = new StringBuilder();

        reporte.append("=== REPORTE GENERAL DEL SISTEMA ===\n\n");

        List<Alumno> alumnos = alumnoController.cargarAlumnos();
        List<Carrera> carreras = carreraController.cargarCarreras();
        List<Materia> materias = materiaController.cargarMaterias();
        List<InscripcionMateria> inscripciones = inscripcionController.cargarInscripciones();

        reporte.append("Total de alumnos: ").append(alumnos.size()).append("\n");
        reporte.append("Total de carreras: ").append(carreras.size()).append("\n");
        reporte.append("Total de materias: ").append(materias.size()).append("\n");
        reporte.append("Total de inscripciones: ").append(inscripciones.size()).append("\n\n");

        reporte.append("CARRERAS:\n");
        for (Carrera carrera : carreras) {
            reporte.append("- ").append(carrera.mostrarInfoCompleta()).append("\n");
        }

        return reporte.toString();
    }

    // Getters para los controllers
    public AlumnoController getAlumnoController() { return alumnoController; }
    public CarreraController getCarreraController() { return carreraController; }
    public MateriaController getMateriaController() { return materiaController; }
    public InscripcionController getInscripcionController() { return inscripcionController; }
}