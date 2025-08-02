package controller;

import model.Alumno;
import java.io.*;
import java.util.*;

public class AlumnoController {
    private static final String ARCHIVO = "data/alumnos.txt";

    // CREATE - Crear alumno con validaciones completas
    public String crearAlumno(String nombre, String legajo) {
        // Validar campos vacíos
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        if (legajo == null || legajo.trim().isEmpty()) {
            return "Error: El legajo es obligatorio";
        }

        // Limpiar datos
        nombre = nombre.trim();
        legajo = legajo.trim();

        // Validar formato de legajo
        String errorLegajo = validarFormatoLegajo(legajo);
        if (errorLegajo != null) {
            return errorLegajo;
        }

        // Validar formato de nombre
        String errorNombre = validarFormatoNombre(nombre);
        if (errorNombre != null) {
            return errorNombre;
        }

        // Cargar alumnos existentes
        List<Alumno> alumnos = cargarAlumnos();

        // Verificar legajo duplicado
        for (Alumno a : alumnos) {
            if (a.getLegajo().equals(legajo)) {
                return "Error: Ya existe un alumno con legajo " + legajo;
            }
        }

        // Crear y guardar
        alumnos.add(new Alumno(nombre, legajo));
        if (guardarAlumnos(alumnos)) {
            return null; // Éxito
        } else {
            return "Error: No se pudo guardar el alumno";
        }
    }

    // UPDATE - Actualizar alumno con validaciones completas
    public String actualizarAlumno(String legajoViejo, String nombreNuevo, String legajoNuevo) {
        // Validar campos vacíos
        if (nombreNuevo == null || nombreNuevo.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        if (legajoNuevo == null || legajoNuevo.trim().isEmpty()) {
            return "Error: El legajo es obligatorio";
        }

        if (legajoViejo == null || legajoViejo.trim().isEmpty()) {
            return "Error: Legajo original no válido";
        }

        // Limpiar datos
        nombreNuevo = nombreNuevo.trim();
        legajoNuevo = legajoNuevo.trim();
        legajoViejo = legajoViejo.trim();

        // Validar formatos
        String errorLegajo = validarFormatoLegajo(legajoNuevo);
        if (errorLegajo != null) {
            return errorLegajo;
        }

        String errorNombre = validarFormatoNombre(nombreNuevo);
        if (errorNombre != null) {
            return errorNombre;
        }

        // Cargar alumnos
        List<Alumno> alumnos = cargarAlumnos();

        // Buscar alumno a actualizar
        Alumno alumnoActualizar = null;
        for (Alumno a : alumnos) {
            if (a.getLegajo().equals(legajoViejo)) {
                alumnoActualizar = a;
                break;
            }
        }

        if (alumnoActualizar == null) {
            return "Error: No se encontró el alumno con legajo " + legajoViejo;
        }

        // Si cambió el legajo, verificar que no exista otro con ese legajo
        if (!legajoViejo.equals(legajoNuevo)) {
            for (Alumno a : alumnos) {
                if (a.getLegajo().equals(legajoNuevo)) {
                    return "Error: Ya existe un alumno con legajo " + legajoNuevo;
                }
            }
        }

        // Actualizar
        alumnos.remove(alumnoActualizar);
        alumnos.add(new Alumno(nombreNuevo, legajoNuevo));

        if (guardarAlumnos(alumnos)) {
            return null; // Éxito
        } else {
            return "Error: No se pudo actualizar el alumno";
        }
    }

    // DELETE - Eliminar alumno (sin validaciones)
    public boolean eliminarAlumno(String legajo) {
        List<Alumno> alumnos = cargarAlumnos();

        // Buscar y remover
        boolean eliminado = alumnos.removeIf(a -> a.getLegajo().equals(legajo));

        if (eliminado) {
            return guardarAlumnos(alumnos);
        }

        return false;
    }

    // VALIDACIONES PRIVADAS

    /**
     * Valida el formato del legajo
     */
    private String validarFormatoLegajo(String legajo) {
        // Verificar longitud
        if (legajo.length() < 4) {
            return "Error: El legajo debe tener al menos 4 caracteres";
        }

        if (legajo.length() > 10) {
            return "Error: El legajo no puede tener más de 10 caracteres";
        }

        // Verificar que no tenga espacios
        if (legajo.contains(" ")) {
            return "Error: El legajo no puede contener espacios";
        }

        // Verificar que solo tenga números y letras
        if (!legajo.matches("^[a-zA-Z0-9]+$")) {
            return "Error: El legajo solo puede contener letras y números";
        }

        return null; // Válido
    }

    /**
     * Valida el formato del nombre
     */
    private String validarFormatoNombre(String nombre) {
        // Verificar longitud
        if (nombre.length() < 2) {
            return "Error: El nombre debe tener al menos 2 caracteres";
        }

        if (nombre.length() > 50) {
            return "Error: El nombre no puede tener más de 50 caracteres";
        }

        // Verificar que solo tenga letras, espacios y algunos caracteres especiales
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            return "Error: El nombre solo puede contener letras y espacios";
        }

        return null; // Válido
    }

    // READ - Cargar alumnos (sin cambios)
    public List<Alumno> cargarAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();

        try {
            File archivo = new File(ARCHIVO);
            if (!archivo.exists()) {
                archivo.getParentFile().mkdirs();
                return alumnos;
            }

            BufferedReader reader = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Alumno alumno = Alumno.fromString(linea);
                    if (alumno != null) {
                        alumnos.add(alumno);
                    }
                }
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Error al cargar alumnos");
        }

        return alumnos;
    }

    // SAVE - Guardar alumnos (sin cambios)
    public boolean guardarAlumnos(List<Alumno> alumnos) {
        try {
            File archivo = new File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));

            for (Alumno alumno : alumnos) {
                writer.write(alumno.toString());
                writer.newLine();
            }

            writer.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error al guardar alumnos");
            return false;
        }
    }

    // UTILITY - Buscar por legajo (sin cambios)
    public Alumno buscarPorLegajo(String legajo) {
        List<Alumno> alumnos = cargarAlumnos();
        for (Alumno alumno : alumnos) {
            if (alumno.getLegajo().equals(legajo)) {
                return alumno;
            }
        }
        return null;
    }

    // UTILITY - Listar todos (sin cambios)
    public void listarAlumnos() {
        List<Alumno> alumnos = cargarAlumnos();
        System.out.println("=== ALUMNOS ===");
        for (Alumno alumno : alumnos) {
            System.out.println("- " + alumno.mostrarInfo());
        }
    }
}