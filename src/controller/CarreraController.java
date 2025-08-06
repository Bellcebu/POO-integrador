package controller;

import model.*;
import persistence.ArchivoCarreras;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class CarreraController {

    public static class ResultadoOperacion {
        private final boolean exito;
        private final String mensaje;

        public ResultadoOperacion(boolean exito, String mensaje) {
            this.exito = exito;
            this.mensaje = mensaje;
        }

        public boolean isExito() {
            return exito;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    public ResultadoOperacion agregarAccion(String codigo, String nombre, int cantidadOptativas, String tipoPlan) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return new ResultadoOperacion(false, "Código no puede estar vacío");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return new ResultadoOperacion(false, "Nombre no puede estar vacío");
        }

        if (cantidadOptativas < 0) {
            return new ResultadoOperacion(false, "Cantidad de optativas debe ser un número válido (0 o mayor)");
        }

        if (tipoPlan == null || tipoPlan.trim().isEmpty()) {
            return new ResultadoOperacion(false, "Debe seleccionar un tipo de plan");
        }

        codigo = codigo.trim().toUpperCase();
        nombre = nombre.trim();
        tipoPlan = tipoPlan.trim().toUpperCase();

        if (!codigo.matches("[A-Z0-9]+")) {
            return new ResultadoOperacion(false, "El código debe contener solo letras y números");
        }

        for (Carrera c : Facultad.getInstance().getCarreras()) {
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                return new ResultadoOperacion(false, "Ya existe una carrera con ese código");
            }
        }

        for (Carrera c : Facultad.getInstance().getCarreras()) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return new ResultadoOperacion(false, "Ya existe una carrera con ese nombre");
            }
        }

        PlanEstudio plan = crearPlan(tipoPlan);
        if (plan == null) {
            return new ResultadoOperacion(false, "Tipo de plan inválido: " + tipoPlan);
        }

        Carrera nuevaCarrera = new Carrera(codigo, nombre, cantidadOptativas, plan);
        Facultad.getInstance().getCarreras().add(nuevaCarrera);
        ArchivoCarreras.agregar(nuevaCarrera);

        return new ResultadoOperacion(true, "Carrera creada exitosamente");
    }

    public ResultadoOperacion inscribirAlumnosACarrera(String codigoCarrera, List<String> legajosAlumnos) {
        if (codigoCarrera == null || codigoCarrera.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El código de carrera es obligatorio");
        }

        if (legajosAlumnos == null || legajosAlumnos.isEmpty()) {
            return new ResultadoOperacion(false, "Debe seleccionar al menos un alumno");
        }

        Carrera carrera = null;
        for (Carrera c : Facultad.getInstance().getCarreras()) {
            if (c.getCodigo().equals(codigoCarrera.trim())) {
                carrera = c;
                break;
            }
        }

        if (carrera == null) {
            return new ResultadoOperacion(false, "Carrera no encontrada");
        }

        int alumnosInscriptos = 0;
        int alumnosNoEncontrados = 0;
        int alumnosYaInscriptos = 0;
        List<String> alumnosExitosos = new ArrayList<>();

        for (String legajo : legajosAlumnos) {
            if (legajo == null || legajo.trim().isEmpty()) {
                continue;
            }

            Alumno alumno = Alumno.buscarPorLegajo(legajo.trim(), Facultad.getInstance().getAlumnos());
            if (alumno != null) {
                if (!carrera.getAlumnos().contains(alumno)) {
                    carrera.inscribirAlumno(alumno);
                    alumnosInscriptos++;
                    alumnosExitosos.add(alumno.getNombre());
                } else {
                    alumnosYaInscriptos++;
                }
            } else {
                alumnosNoEncontrados++;
            }
        }

        if (alumnosInscriptos == 0) {
            if (alumnosYaInscriptos > 0) {
                return new ResultadoOperacion(false, "Todos los alumnos seleccionados ya estaban inscriptos en la carrera");
            } else if (alumnosNoEncontrados > 0) {
                return new ResultadoOperacion(false, "No se encontraron alumnos válidos para inscribir");
            } else {
                return new ResultadoOperacion(false, "No se pudo inscribir a ningún alumno");
            }
        }

        ArchivoCarreras.actualizar(carrera);

        return generarMensajeResultadoAlumnos(alumnosInscriptos, alumnosExitosos,
                alumnosYaInscriptos, alumnosNoEncontrados, legajosAlumnos.size());
    }

    public ResultadoOperacion agregarMateriasACarrera(String codigoCarrera, List<String> codigosMaterias) {
        if (codigoCarrera == null || codigoCarrera.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El código de carrera es obligatorio");
        }

        if (codigosMaterias == null || codigosMaterias.isEmpty()) {
            return new ResultadoOperacion(false, "Debe seleccionar al menos una materia");
        }

        Carrera carrera = null;
        for (Carrera c : Facultad.getInstance().getCarreras()) {
            if (c.getCodigo().equals(codigoCarrera.trim())) {
                carrera = c;
                break;
            }
        }

        if (carrera == null) {
            return new ResultadoOperacion(false, "Carrera no encontrada");
        }

        List<Materia> materiasAAgregar = new ArrayList<>();
        List<String> materiasNoEncontradas = new ArrayList<>();

        for (String codigoMateria : codigosMaterias) {
            if (codigoMateria != null && !codigoMateria.trim().isEmpty()) {
                Materia materia = Materia.buscarPorCodigo(codigoMateria.trim(), Facultad.getInstance().getMaterias());
                if (materia != null && !carrera.getMaterias().contains(materia)) {
                    materiasAAgregar.add(materia);
                } else if (materia == null) {
                    materiasNoEncontradas.add(codigoMateria.trim());
                }
            }
        }

        if (!materiasNoEncontradas.isEmpty()) {
            return new ResultadoOperacion(false, "Materias no encontradas: " + String.join(", ", materiasNoEncontradas));
        }

        Set<Materia> todasLasMateriasNecesarias = new HashSet<>(materiasAAgregar);
        Set<Materia> materiasYaEnCarrera = new HashSet<>(carrera.getMaterias());
        List<String> correlativasFaltantes = new ArrayList<>();

        for (Materia materia : materiasAAgregar) {
            for (Materia correlativa : materia.getCorrelativas()) {
                if (!materiasYaEnCarrera.contains(correlativa) && !todasLasMateriasNecesarias.contains(correlativa)) {
                    correlativasFaltantes.add(correlativa.getNombre() + " (" + correlativa.getCodigo() + ") - requerida por " + materia.getNombre());
                }
            }
        }

        if (!correlativasFaltantes.isEmpty()) {
            String mensaje = "No se pueden agregar las materias porque faltan las siguientes correlativas:\n\n" +
                    String.join("\n", correlativasFaltantes) +
                    "\n\nDebe agregar primero las materias correlativas o incluirlas en la selección actual.";
            return new ResultadoOperacion(false, mensaje);
        }

        int materiasAgregadas = 0;
        int materiasYaExistentes = 0;
        List<String> materiasExitosas = new ArrayList<>();

        for (Materia materia : materiasAAgregar) {
            if (!carrera.getMaterias().contains(materia)) {
                carrera.agregarMateria(materia);
                materiasAgregadas++;
                materiasExitosas.add(materia.getNombre());
            } else {
                materiasYaExistentes++;
            }
        }

        if (materiasAgregadas == 0) {
            if (materiasYaExistentes > 0) {
                return new ResultadoOperacion(false, "Todas las materias seleccionadas ya pertenecían a la carrera");
            } else {
                return new ResultadoOperacion(false, "No se pudo agregar ninguna materia");
            }
        }

        ArchivoCarreras.actualizar(carrera);

        return generarMensajeResultadoMaterias(materiasAgregadas, materiasExitosas,
                materiasYaExistentes, 0, codigosMaterias.size());
    }

    private ResultadoOperacion generarMensajeResultadoAlumnos(int alumnosInscriptos, List<String> alumnosExitosos,
                                                              int alumnosYaInscriptos, int alumnosNoEncontrados, int totalAlumnos) {

        if (alumnosInscriptos == totalAlumnos && alumnosYaInscriptos == 0 && alumnosNoEncontrados == 0) {
            return new ResultadoOperacion(true, "Se inscribieron exitosamente todos los alumnos seleccionados");
        }

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Se inscribieron ").append(alumnosInscriptos)
                .append(" de ").append(totalAlumnos).append(" alumnos seleccionados");

        if (!alumnosExitosos.isEmpty()) {
            mensaje.append("\n\nAlumnos inscriptos: ")
                    .append(String.join(", ", alumnosExitosos));
        }

        if (alumnosYaInscriptos > 0) {
            mensaje.append("\n").append(alumnosYaInscriptos).append(" ya estaban inscriptos");
        }

        if (alumnosNoEncontrados > 0) {
            mensaje.append("\n").append(alumnosNoEncontrados).append(" no fueron encontrados");
        }

        return new ResultadoOperacion(true, mensaje.toString());
    }

    private ResultadoOperacion generarMensajeResultadoMaterias(int materiasAgregadas, List<String> materiasExitosas,
                                                               int materiasYaExistentes, int materiasNoEncontradas, int totalMaterias) {

        if (materiasAgregadas == totalMaterias && materiasYaExistentes == 0 && materiasNoEncontradas == 0) {
            return new ResultadoOperacion(true, "Se agregaron exitosamente todas las materias seleccionadas");
        }

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Se agregaron ").append(materiasAgregadas)
                .append(" de ").append(totalMaterias).append(" materias seleccionadas");

        if (!materiasExitosas.isEmpty()) {
            mensaje.append("\n\nMaterias agregadas: ")
                    .append(String.join(", ", materiasExitosas));
        }

        if (materiasYaExistentes > 0) {
            mensaje.append("\n").append(materiasYaExistentes).append(" ya pertenecían a la carrera");
        }

        if (materiasNoEncontradas > 0) {
            mensaje.append("\n").append(materiasNoEncontradas).append(" no fueron encontradas");
        }

        return new ResultadoOperacion(true, mensaje.toString());
    }

    public Carrera buscarCarreraPorCodigo (String codigo){
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }

        for (Carrera carrera : Facultad.getInstance().getCarreras()) {
            if (carrera.getCodigo().equals(codigo.trim())) {
                return carrera;
            }
        }
        return null;
    }

    public List<Carrera> buscarCarreras (String textoBusqueda){
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return new ArrayList<>(Facultad.getInstance().getCarreras());
        }

        String busqueda = textoBusqueda.trim().toLowerCase();
        List<Carrera> resultados = new ArrayList<>();

        for (Carrera carrera : Facultad.getInstance().getCarreras()) {
            if (carrera.getNombre().toLowerCase().contains(busqueda) ||
                    carrera.getCodigo().toLowerCase().contains(busqueda)) {
                resultados.add(carrera);
            }
        }

        return resultados;
    }

    private PlanEstudio crearPlan (String tipoPlan){
        if (tipoPlan == null) {
            return null;
        }

        switch (tipoPlan.toUpperCase()) {
            case "PLANA":
                return new PlanA();
            case "PLANB":
                return new PlanB();
            case "PLANC":
                return new PlanC();
            case "PLAND":
                return new PlanD();
            case "PLANE":
                return new PlanE();
            default:
                return null;
        }
    }
}