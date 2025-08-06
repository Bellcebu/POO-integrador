package controller;

import model.*;
import model.Facultad;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class AlumnoController {

    public static class ResultadoOperacion {
        private final boolean exito;
        private final String mensaje;

        public ResultadoOperacion(boolean exito, String mensaje) {
            this.exito = exito;
            this.mensaje = mensaje;
        }

        public boolean isExito() { return exito; }
        public String getMensaje() { return mensaje; }
    }

    public List<Alumno> obtenerTodos() {
        return Facultad.getInstance().getAlumnos();
    }

    public ResultadoOperacion agregarAccion(String nombre, String legajo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return new ResultadoOperacion(false, "Nombre no puede estar vacío");
        }

        if (legajo == null || legajo.trim().isEmpty()) {
            return new ResultadoOperacion(false, "Legajo no puede estar vacío");
        }

        nombre = nombre.trim();
        legajo = legajo.trim();

        if (!legajo.matches("\\d+")) {
            return new ResultadoOperacion(false, "Legajo debe contener solo números");
        }

        for (Alumno a : Facultad.getInstance().getAlumnos()) {
            if (a.getLegajo().equals(legajo)) {
                return new ResultadoOperacion(false, "Ya existe un alumno con ese legajo");
            }
        }

        Alumno nuevoAlumno = new Alumno(nombre, legajo);
        Facultad.getInstance().getAlumnos().add(nuevoAlumno);
        persistence.ArchivoAlumnos.agregar(nuevoAlumno);

        return new ResultadoOperacion(true, "Alumno creado exitosamente");
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

        Alumno alumno = Alumno.buscarPorLegajo(legajoAlumno, Facultad.getInstance().getAlumnos());
        if (alumno == null) {
            return materiasDisponibles;
        }

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

        for (Materia materia : carrera.getMaterias()) {
            boolean yaInscripto = alumno.estaInscriptoEn(materia);

            if (!yaInscripto) {
                boolean puedeInscribirse = carrera.puedeInscribirseA(materia, alumno);

                if (puedeInscribirse) {
                    materiasDisponibles.add(materia);
                }
            }
        }

        return materiasDisponibles;
    }

    public ResultadoOperacion inscribirAlumnoAMaterias(String legajoAlumno, List<String> codigosMaterias) {
        if (legajoAlumno == null || legajoAlumno.trim().isEmpty()) {
            return new ResultadoOperacion(false, "Legajo del alumno no puede estar vacío");
        }

        if (codigosMaterias == null || codigosMaterias.isEmpty()) {
            return new ResultadoOperacion(false, "Debe seleccionar al menos una materia");
        }

        Alumno alumno = Alumno.buscarPorLegajo(legajoAlumno.trim(), Facultad.getInstance().getAlumnos());
        if (alumno == null) {
            return new ResultadoOperacion(false, "Alumno no encontrado");
        }

        List<Carrera> carrerasAlumno = obtenerCarrerasDelAlumno(legajoAlumno);
        if (carrerasAlumno.isEmpty()) {
            return new ResultadoOperacion(false, "El alumno no está inscripto en ninguna carrera");
        }

        int materiasInscriptas = 0;
        List<String> erroresPorMateria = new ArrayList<>();
        List<String> materiasExitosas = new ArrayList<>();

        for (String codigoMateria : codigosMaterias) {
            if (codigoMateria == null || codigoMateria.trim().isEmpty()) {
                continue;
            }

            String codigo = codigoMateria.trim();
            Materia materia = Materia.buscarPorCodigo(codigo, Facultad.getInstance().getMaterias());

            if (materia == null) {
                erroresPorMateria.add("Materia " + codigo + " no encontrada");
                continue;
            }

            if (alumno.estaInscriptoEn(materia)) {
                erroresPorMateria.add("Ya está inscripto en " + materia.getNombre());
                continue;
            }

            boolean materiaEnCarreraAlumno = false;
            Carrera carreraConMateria = null;

            for (Carrera carrera : carrerasAlumno) {
                if (carrera.getMaterias().contains(materia)) {
                    materiaEnCarreraAlumno = true;
                    carreraConMateria = carrera;
                    break;
                }
            }

            if (!materiaEnCarreraAlumno) {
                erroresPorMateria.add(materia.getNombre() + " no pertenece a ninguna de sus carreras");
                continue;
            }

            if (!carreraConMateria.puedeInscribirseA(materia, alumno)) {
                String razonRechazo = obtenerRazonRechazoInscripcion(materia, alumno, carreraConMateria);
                erroresPorMateria.add("No puede inscribirse a " + materia.getNombre() + ". " + razonRechazo);
                continue;
            }

            InscripcionMateria inscripcion = new InscripcionMateria(alumno, materia);
            alumno.agregarInscripcion(materia, inscripcion);
            materiasInscriptas++;
            materiasExitosas.add(materia.getNombre());
        }

        if (materiasInscriptas > 0) {
            persistence.ArchivoInscripciones.actualizar();
        }

        return generarMensajeResultadoInscripcion(materiasInscriptas, materiasExitosas, erroresPorMateria, codigosMaterias.size());
    }

    private String obtenerRazonRechazoInscripcion(Materia materia, Alumno alumno, Carrera carrera) {
        List<String> correlativasFaltantes = new ArrayList<>();

        for (Materia correlativa : materia.getCorrelativas()) {
            String planTipo = carrera.getPlanEstudio().getClass().getSimpleName();

            boolean cumpleCorrelativa = false;
            switch (planTipo) {
                case "PlanA":
                case "PlanC":
                case "PlanD":
                    cumpleCorrelativa = alumno.aproboCursada(correlativa);
                    break;
                case "PlanB":
                case "PlanE":
                    cumpleCorrelativa = alumno.aproboFinal(correlativa);
                    break;
            }

            if (!cumpleCorrelativa) {
                String requisito = (planTipo.equals("PlanB") || planTipo.equals("PlanE")) ?
                        " (final aprobado)" : " (cursada aprobada)";
                correlativasFaltantes.add(correlativa.getNombre() + requisito);
            }
        }

        if (!correlativasFaltantes.isEmpty()) {
            return "Falta aprobar: " + String.join(", ", correlativasFaltantes);
        }

        String planTipo = carrera.getPlanEstudio().getClass().getSimpleName();
        if (planTipo.equals("PlanC") || planTipo.equals("PlanD") || planTipo.equals("PlanE")) {
            int limiteCuatrimestre = planTipo.equals("PlanC") ? 5 : 3;
            int cuatrimestreMateria = materia.getCuatrimestre();

            for (InscripcionMateria inscripcion : alumno.getInscripciones().values()) {
                Materia m = inscripcion.getMateria();
                if (m.getCuatrimestre() <= cuatrimestreMateria - limiteCuatrimestre) {
                    if (!alumno.aproboFinal(m)) {
                        return "El plan requiere tener aprobado el final de " + m.getNombre() +
                                " (cuatrimestre " + m.getCuatrimestre() + ")";
                    }
                }
            }
        }

        return "No cumple con los requisitos del plan de estudios";
    }

    private ResultadoOperacion generarMensajeResultadoInscripcion(int materiasInscriptas, List<String> materiasExitosas, List<String> erroresPorMateria, int totalMaterias) {

        if (materiasInscriptas == 0) {
            String mensaje = "No se pudo inscribir a ninguna materia:\n" +
                    String.join("\n", erroresPorMateria);
            return new ResultadoOperacion(false, mensaje);
        }

        if (materiasInscriptas == totalMaterias && erroresPorMateria.isEmpty()) {
            return new ResultadoOperacion(true, "Se inscribió exitosamente a todas las materias seleccionadas");
        }

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Se inscribió a ").append(materiasInscriptas)
                .append(" de ").append(totalMaterias).append(" materias seleccionadas");

        if (!materiasExitosas.isEmpty()) {
            mensaje.append("\n\nMaterias inscriptas: ")
                    .append(String.join(", ", materiasExitosas));
        }

        if (!erroresPorMateria.isEmpty()) {
            mensaje.append("\n\nProblemas encontrados:\n")
                    .append(String.join("\n", erroresPorMateria));
        }

        return new ResultadoOperacion(true, mensaje.toString());
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