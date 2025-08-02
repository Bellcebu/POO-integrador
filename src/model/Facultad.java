package model;

import controller.AlumnoController;
import controller.MateriaController;

import java.util.*;

public class Facultad {
    private static Facultad instance;
    private List<Carrera> carreras;
    private List<Alumno> alumnos;
    private List<Materia> materias;
    private List<InscripcionMateria> inscripciones;
    private AlumnoController alumnoController;
    private MateriaController materiaController;

    // Patrón singleton
    private Facultad() {
        this.carreras = new ArrayList<>();
        this.alumnos = new ArrayList<>();
        this.materias = new ArrayList<>();
        this.inscripciones = new ArrayList<>();
    }

    private void asociarAlumnosACarreras() {
        try {
            java.util.List<String> lineas = java.nio.file.Files.readAllLines(
                    java.nio.file.Paths.get("data/carreras.txt"));

            for (int i = 0; i < lineas.size() && i < carreras.size(); i++) {
                String linea = lineas.get(i);
                if (!linea.trim().isEmpty()) {
                    String[] legajosAlumnos = Carrera.getLegajosAlumnos(linea);
                    Carrera carrera = carreras.get(i);

                    for (String legajo : legajosAlumnos) {
                        if (!legajo.trim().isEmpty()) {
                            for (Alumno alumno : alumnos) {
                                if (alumno.getLegajo().equals(legajo.trim())) {
                                    carrera.inscribirAlumno(alumno);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

        } catch (java.io.IOException e) {
            System.out.println("Error al asociar alumnos a carreras");
        }
    }

    private void cargarInscripciones() {
        try {
            java.util.List<String> lineas = java.nio.file.Files.readAllLines(
                    java.nio.file.Paths.get("data/inscripciones.txt"));

            for (String linea : lineas) {
                if (!linea.trim().isEmpty()) {
                    String[] partes = linea.split("☆");
                    if (partes.length >= 5) {
                        String legajoAlumno = partes[0].trim();
                        String codigoMateria = partes[1].trim();
                        boolean parcialAprobado = Boolean.parseBoolean(partes[2].trim());
                        boolean finalAprobado = Boolean.parseBoolean(partes[3].trim());
                        boolean promocionado = Boolean.parseBoolean(partes[4].trim());

                        Alumno alumno = Alumno.buscarPorLegajo(legajoAlumno, alumnos);
                        Materia materia = Materia.buscarPorCodigo(codigoMateria, materias);

                        if (alumno != null && materia != null) {
                            InscripcionMateria inscripcion = new InscripcionMateria(alumno, materia);

                            if (parcialAprobado) inscripcion.aprobarParcial();
                            if (finalAprobado) inscripcion.aprobarFinal();
                            if (promocionado) inscripcion.otorgarPromocion();

                            inscripciones.add(inscripcion);
                            alumno.agregarInscripcion(materia, inscripcion);
                        }
                    }
                }
            }

        } catch (java.io.IOException e) {
            System.out.println("Error al cargar inscripciones");
        }
    }





    // Métodos para editar
    public boolean editarAlumno(String legajoViejo, Alumno nuevoAlumno) {
        for (int i = 0; i < alumnos.size(); i++) {
            if (alumnos.get(i).getLegajo().equals(legajoViejo)) {
                alumnos.set(i, nuevoAlumno);
                return true;
            }
        }
        return false;
    }



    public static Facultad getInstance() {
        if (instance == null) {
            instance = new Facultad();
        }
        return instance;
    }

    public void cargarTodoDesdeArchivos() {
        cargarAlumnos();
        cargarMaterias();
        cargarCarreras();
        asociarMateriasACarreras();
        asociarAlumnosACarreras();
        cargarInscripciones();
    }

    private void cargarAlumnos() {
        try {
            java.util.List<String> lineas = java.nio.file.Files.readAllLines(
                    java.nio.file.Paths.get("data/alumnos.txt"));

            for (String linea : lineas) {
                if (!linea.trim().isEmpty()) {
                    Alumno alumno = Alumno.fromString(linea);
                    if (alumno != null) {
                        alumnos.add(alumno);
                    }
                }
            }

        } catch (java.io.IOException e) {
            System.out.println("Error al cargar alumnos");
        }
    }

    private void cargarCarreras() {
        try {
            java.util.List<String> lineas = java.nio.file.Files.readAllLines(
                    java.nio.file.Paths.get("data/carreras.txt"));

            for (String linea : lineas) {
                if (!linea.trim().isEmpty()) {
                    Carrera carrera = Carrera.fromString(linea);
                    if (carrera != null) {
                        carreras.add(carrera);
                    }
                }
            }

        } catch (java.io.IOException e) {
            System.out.println("Error al cargar carreras");
        }
    }

    private void cargarMaterias() {
        try {
            java.util.List<String> lineas = java.nio.file.Files.readAllLines(
                    java.nio.file.Paths.get("data/materias.txt"));

            for (String linea : lineas) {
                if (!linea.trim().isEmpty()) {
                    Materia materia = Materia.fromString(linea);
                    if (materia != null) {
                        materias.add(materia);
                    }
                }
            }

        } catch (java.io.IOException e) {
            System.out.println("Error al cargar materias");
        }
    }

    private void asociarMateriasACarreras() {
        for (Carrera carrera : carreras) {
            for (Materia materia : materias) {
                if (materia.getCodigoCarrera().equals(carrera.getCodigo())) {
                    carrera.agregarMateria(materia);
                }
            }
        }
    }

    public List<Carrera> getCarreras() {
        return carreras;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public List<Materia> getMaterias() {
        return materias;
    }


    @Override
    public String toString() {
        return "Facultad - Carreras: " + carreras.size() + ", Alumnos: " + alumnos.size();
    }
}