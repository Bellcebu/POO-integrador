package model;

import java.util.*;

public class Facultad {
    private static Facultad instance;
    private List<Carrera> carreras;
    private List<Alumno> alumnos;
    private List<Materia> materias;
    private List<InscripcionMateria> inscripciones;

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

    // Métodos de búsqueda
    public List<Alumno> buscarAlumnos(String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return new ArrayList<>(alumnos);
        }

        String busqueda = textoBusqueda.trim().toLowerCase();
        List<Alumno> resultados = new ArrayList<>();

        for (Alumno alumno : alumnos) {
            if (alumno.getNombre().toLowerCase().contains(busqueda) ||
                    alumno.getLegajo().toLowerCase().contains(busqueda)) {
                resultados.add(alumno);
            }
        }

        return resultados;
    }

    // Métodos de búsqueda y ordenamiento para CARRERAS
    public List<Carrera> buscarCarreras(String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return new ArrayList<>(carreras);
        }

        String busqueda = textoBusqueda.trim().toLowerCase();
        List<Carrera> resultados = new ArrayList<>();

        for (Carrera carrera : carreras) {
            if (carrera.getNombre().toLowerCase().contains(busqueda) ||
                    carrera.getCodigo().toLowerCase().contains(busqueda)) {
                resultados.add(carrera);
            }
        }

        return resultados;
    }

    public List<Carrera> ordenarCarrerasAZ() {
        List<Carrera> carrerasOrdenadas = new ArrayList<>(carreras);
        carrerasOrdenadas.sort((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
        return carrerasOrdenadas;
    }

    public List<Carrera> ordenarCarrerasZA() {
        List<Carrera> carrerasOrdenadas = new ArrayList<>(carreras);
        carrerasOrdenadas.sort((c1, c2) -> c2.getNombre().compareToIgnoreCase(c1.getNombre()));
        return carrerasOrdenadas;
    }

    // Métodos de búsqueda y ordenamiento para MATERIAS
    public List<Materia> buscarMaterias(String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return new ArrayList<>(materias);
        }

        String busqueda = textoBusqueda.trim().toLowerCase();
        List<Materia> resultados = new ArrayList<>();

        for (Materia materia : materias) {
            if (materia.getNombre().toLowerCase().contains(busqueda) ||
                    materia.getCodigo().toLowerCase().contains(busqueda)) {
                resultados.add(materia);
            }
        }

        return resultados;
    }

    public List<Materia> ordenarMateriasAZ() {
        List<Materia> materiasOrdenadas = new ArrayList<>(materias);
        materiasOrdenadas.sort((m1, m2) -> m1.getNombre().compareToIgnoreCase(m2.getNombre()));
        return materiasOrdenadas;
    }

    public List<Materia> ordenarMateriasZA() {
        List<Materia> materiasOrdenadas = new ArrayList<>(materias);
        materiasOrdenadas.sort((m1, m2) -> m2.getNombre().compareToIgnoreCase(m1.getNombre()));
        return materiasOrdenadas;
    }

    // Métodos de ordenamiento
    public List<Alumno> ordenarAlumnosAZ() {
        List<Alumno> alumnosOrdenados = new ArrayList<>(alumnos);
        alumnosOrdenados.sort((a1, a2) -> a1.getNombre().compareToIgnoreCase(a2.getNombre()));
        return alumnosOrdenados;
    }

    public List<Alumno> ordenarAlumnosZA() {
        List<Alumno> alumnosOrdenados = new ArrayList<>(alumnos);
        alumnosOrdenados.sort((a1, a2) -> a2.getNombre().compareToIgnoreCase(a1.getNombre()));
        return alumnosOrdenados;
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

                        Alumno alumno = buscarAlumnoPorLegajo(legajoAlumno);
                        Materia materia = buscarMateriaPorCodigo(codigoMateria);

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

    private Alumno buscarAlumnoPorLegajo(String legajo) {
        for (Alumno alumno : alumnos) {
            if (alumno.getLegajo().equals(legajo)) {
                return alumno;
            }
        }
        return null;
    }

    private Materia buscarMateriaPorCodigo(String codigo) {
        for (Materia materia : materias) {
            if (materia.getCodigo().equals(codigo)) {
                return materia;
            }
        }
        return null;
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

    // Métodos para eliminar
    public boolean eliminarAlumno(String legajo) {
        return alumnos.removeIf(a -> a.getLegajo().equals(legajo));
    }

    public boolean eliminarCarrera(String codigo) {
        return carreras.removeIf(c -> c.getCodigo().equals(codigo));
    }

    public boolean eliminarMateria(String codigo) {
        return materias.removeIf(m -> m.getCodigo().equals(codigo));
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

    // Métodos add básicos
    public void agregarCarrera(Carrera carrera) {
        carreras.add(carrera);
    }

    public boolean agregarAlumno(Alumno alumno) {
        for (Alumno a : alumnos) {
            if (a.equals(alumno)) {
                return false;
            }
        }
        alumnos.add(alumno);
        return true;
    }

    @Override
    public String toString() {
        return "Facultad - Carreras: " + carreras.size() + ", Alumnos: " + alumnos.size();
    }
}