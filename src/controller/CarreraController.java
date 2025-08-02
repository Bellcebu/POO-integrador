package controller;

import model.*;
import java.io.*;
import java.util.*;

public class CarreraController {
    private static final String ARCHIVO = "data/carreras.txt";

    // CREATE - Crear carrera
    public boolean crearCarrera(String codigo, String nombre, int cantidadOptativas, String tipoPlan) {
        List<Carrera> carreras = cargarCarreras();

        // Validar duplicado
        for (Carrera c : carreras) {
            if (c.getCodigo().equals(codigo)) {
                return false;
            }
        }

        // Crear plan
        PlanEstudio plan = crearPlan(tipoPlan);
        if (plan == null) return false;

        // Agregar y guardar
        carreras.add(new Carrera(codigo, nombre, cantidadOptativas, plan));
        return guardarCarreras(carreras);
    }

    // READ - Cargar carreras (sin asociaciones complejas)
    // REEMPLAZAR el método cargarCarreras() en CarreraController.java

    /**
     * READ - Cargar carreras con sus alumnos asociados
     */
    public List<Carrera> cargarCarreras() {
        List<Carrera> carreras = new ArrayList<>();
        AlumnoController alumnoController = new AlumnoController();
        MateriaController materiaController = new MateriaController();

        try {
            File archivo = new File(ARCHIVO);
            if (!archivo.exists()) {
                archivo.getParentFile().mkdirs();
                return carreras;
            }

            BufferedReader reader = new BufferedReader(new FileReader(archivo));
            String linea;

            // Primera pasada: crear carreras básicas
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Carrera carrera = Carrera.fromString(linea);
                    if (carrera != null) {
                        carreras.add(carrera);
                    }
                }
            }
            reader.close();

            // Segunda pasada: cargar relaciones
            reader = new BufferedReader(new FileReader(archivo));
            int index = 0;

            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty() && index < carreras.size()) {
                    Carrera carrera = carreras.get(index);

                    // Cargar materias de la carrera
                    String[] codigosMaterias = Carrera.getCodigosMaterias(linea);
                    for (String codigoMateria : codigosMaterias) {
                        if (!codigoMateria.trim().isEmpty()) {
                            Materia materia = materiaController.buscarPorCodigo(codigoMateria.trim());
                            if (materia != null && materia.getCodigoCarrera().equals(carrera.getCodigo())) {
                                carrera.agregarMateria(materia);
                            }
                        }
                    }

                    // Cargar alumnos inscriptos
                    String[] legajosAlumnos = Carrera.getLegajosAlumnos(linea);
                    for (String legajo : legajosAlumnos) {
                        if (!legajo.trim().isEmpty()) {
                            Alumno alumno = alumnoController.buscarPorLegajo(legajo.trim());
                            if (alumno != null) {
                                carrera.inscribirAlumno(alumno);
                            }
                        }
                    }

                    index++;
                }
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Error al cargar carreras: " + e.getMessage());
        }

        return carreras;
    }

    // SAVE - Guardar carreras
    public boolean guardarCarreras(List<Carrera> carreras) {
        try {
            File archivo = new File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));

            for (Carrera carrera : carreras) {
                writer.write(carrera.toString());
                writer.newLine();
            }

            writer.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error al guardar carreras");
            return false;
        }
    }

    // UTILITY - Crear plan
    private PlanEstudio crearPlan(String tipoPlan) {
        switch (tipoPlan.toUpperCase()) {
            case "A": case "PLANA": return new PlanA();
            case "B": case "PLANB": return new PlanB();
            case "C": case "PLANC": return new PlanC();
            case "D": case "PLAND": return new PlanD();
            case "E": case "PLANE": return new PlanE();
            default: return null;
        }
    }

    // UTILITY - Buscar por código
    public Carrera buscarPorCodigo(String codigo) {
        List<Carrera> carreras = cargarCarreras();
        for (Carrera carrera : carreras) {
            if (carrera.getCodigo().equals(codigo)) {
                return carrera;
            }
        }
        return null;
    }

    // UTILITY - Listar todas
    public void listarCarreras() {
        List<Carrera> carreras = cargarCarreras();
        System.out.println("=== CARRERAS ===");
        for (Carrera carrera : carreras) {
            System.out.println("- " + carrera.mostrarInfo());
        }
    }

    // ACTUALIZAR - Actualizar carrera existente
    public boolean actualizarCarrera(String codigoViejo, String codigoNuevo, String nombre, int cantidadOptativas, String tipoPlan) {
        List<Carrera> carreras = cargarCarreras();

        // Buscar la carrera a actualizar
        Carrera carreraActualizar = null;
        for (Carrera c : carreras) {
            if (c.getCodigo().equals(codigoViejo)) {
                carreraActualizar = c;
                break;
            }
        }

        if (carreraActualizar == null) {
            return false; // No encontrada
        }

        // Si cambió el código, verificar que no exista otro con ese código
        if (!codigoViejo.equals(codigoNuevo)) {
            for (Carrera c : carreras) {
                if (c.getCodigo().equals(codigoNuevo)) {
                    return false; // Código duplicado
                }
            }
        }

        // Crear plan
        PlanEstudio plan = crearPlan(tipoPlan);
        if (plan == null) return false;

        // Remover la vieja y agregar la actualizada
        carreras.remove(carreraActualizar);
        carreras.add(new Carrera(codigoNuevo, nombre, cantidadOptativas, plan));

        return guardarCarreras(carreras);
    }

    // ELIMINAR - Eliminar carrera
    public boolean eliminarCarrera(String codigo) {
        List<Carrera> carreras = cargarCarreras();

        // Buscar y remover
        boolean eliminado = carreras.removeIf(c -> c.getCodigo().equals(codigo));

        if (eliminado) {
            return guardarCarreras(carreras);
        }

        return false;
    }

    // AGREGAR ESTE MÉTODO A CarreraController.java

    /**
     * Inscribir alumno a una carrera y persistir
     */
    public String inscribirAlumnoACarrera(String codigoCarrera, String legajoAlumno) {
        // Validar parámetros
        if (codigoCarrera == null || codigoCarrera.trim().isEmpty()) {
            return "Error: Código de carrera no válido";
        }

        if (legajoAlumno == null || legajoAlumno.trim().isEmpty()) {
            return "Error: Legajo de alumno no válido";
        }

        // Buscar carrera
        Carrera carrera = buscarPorCodigo(codigoCarrera.trim());
        if (carrera == null) {
            return "Error: No se encontró la carrera con código " + codigoCarrera;
        }

        // Buscar alumno
        AlumnoController alumnoController = new AlumnoController();
        Alumno alumno = alumnoController.buscarPorLegajo(legajoAlumno.trim());
        if (alumno == null) {
            return "Error: No se encontró el alumno con legajo " + legajoAlumno;
        }

        // Verificar si ya está inscripto
        for (Alumno a : carrera.getAlumnos()) {
            if (a.getLegajo().equals(legajoAlumno.trim())) {
                return "Error: El alumno ya está inscripto en esta carrera";
            }
        }

        // Cargar todas las carreras
        List<Carrera> carreras = cargarCarreras();

        // Encontrar la carrera en la lista y agregar el alumno
        for (int i = 0; i < carreras.size(); i++) {
            if (carreras.get(i).getCodigo().equals(codigoCarrera.trim())) {
                carreras.get(i).inscribirAlumno(alumno);
                break;
            }
        }

        // Guardar todas las carreras
        if (guardarCarreras(carreras)) {
            return null; // Éxito
        } else {
            return "Error: No se pudo guardar la inscripción";
        }
    }
}