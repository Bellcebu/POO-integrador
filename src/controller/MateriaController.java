package controller;

import model.*;
import persistence.ArchivoMaterias;
import java.util.List;
import java.util.ArrayList;

public class MateriaController {

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

    public ResultadoOperacion agregarAccion(String codigo, String nombre, int cuatrimestre, boolean esObligatoria) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return new ResultadoOperacion(false, "Código no puede estar vacío");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return new ResultadoOperacion(false, "Nombre no puede estar vacío");
        }

        if (cuatrimestre <= 0) {
            return new ResultadoOperacion(false, "Cuatrimestre debe ser un número válido mayor a 0");
        }

        if (cuatrimestre > 20) {
            return new ResultadoOperacion(false, "Cuatrimestre no puede ser mayor a 20");
        }

        codigo = codigo.trim().toUpperCase();
        nombre = nombre.trim();

        if (!codigo.matches("[A-Z0-9]+")) {
            return new ResultadoOperacion(false, "El código debe contener solo letras y números");
        }

        for (Materia m : Facultad.getInstance().getMaterias()) {
            if (m.getCodigo().equals(codigo)) {
                return new ResultadoOperacion(false, "Ya existe una materia con ese código");
            }
        }

        for (Materia m : Facultad.getInstance().getMaterias()) {
            if (m.getNombre().equalsIgnoreCase(nombre)) {
                return new ResultadoOperacion(false, "Ya existe una materia con ese nombre");
            }
        }

        Materia nuevaMateria = new Materia(codigo, nombre, cuatrimestre, esObligatoria);
        Facultad.getInstance().getMaterias().add(nuevaMateria);
        persistence.ArchivoMaterias.agregar(nuevaMateria);

        return new ResultadoOperacion(true, "Materia creada exitosamente.");
    }

    public ResultadoOperacion agregarCorrelativasAMateria(String codigoMateria, List<String> codigosCorrelativas) {
        if (codigoMateria == null || codigoMateria.trim().isEmpty()) {
            return new ResultadoOperacion(false, "El código de materia es obligatorio");
        }

        if (codigosCorrelativas == null || codigosCorrelativas.isEmpty()) {
            return new ResultadoOperacion(false, "Debe seleccionar al menos una correlativa");
        }

        Materia materia = null;
        for (Materia m : Facultad.getInstance().getMaterias()) {
            if (m.getCodigo().equals(codigoMateria.trim())) {
                materia = m;
                break;
            }
        }

        if (materia == null) {
            return new ResultadoOperacion(false, "Materia no encontrada");
        }

        int correlativasAgregadas = 0;
        int correlativasNoEncontradas = 0;
        int correlativasYaExistentes = 0;
        int correlativasCirculares = 0;
        int autoCorrelativas = 0;
        List<String> correlativasExitosas = new ArrayList<>();
        List<String> materiasConCiclo = new ArrayList<>();

        for (String codigoCorrelativa : codigosCorrelativas) {
            if (codigoCorrelativa == null || codigoCorrelativa.trim().isEmpty()) {
                continue;
            }

            if (codigoCorrelativa.trim().equals(codigoMateria.trim())) {
                autoCorrelativas++;
                continue;
            }

            Materia correlativa = Materia.buscarPorCodigo(codigoCorrelativa.trim(), Facultad.getInstance().getMaterias());
            if (correlativa != null) {
                if (!materia.getCorrelativas().contains(correlativa)) {
                    if (detectarCicloCompleto(materia, correlativa)) {
                        correlativasCirculares++;
                        materiasConCiclo.add(correlativa.getNombre());
                        continue;
                    }

                    materia.agregarCorrelativa(correlativa);
                    correlativasAgregadas++;
                    correlativasExitosas.add(correlativa.getNombre());
                } else {
                    correlativasYaExistentes++;
                }
            } else {
                correlativasNoEncontradas++;
            }
        }

        if (correlativasAgregadas == 0) {
            List<String> errores = new ArrayList<>();
            if (correlativasYaExistentes > 0) {
                errores.add("Todas las correlativas seleccionadas ya existían");
            }
            if (correlativasNoEncontradas > 0) {
                errores.add("No se encontraron materias válidas");
            }
            if (autoCorrelativas > 0) {
                errores.add("Una materia no puede ser correlativa de sí misma");
            }
            if (correlativasCirculares > 0) {
                errores.add("Se detectaron dependencias circulares con: " + String.join(", ", materiasConCiclo));
            }

            return new ResultadoOperacion(false, "No se agregaron correlativas: " + String.join(", ", errores));
        }

        ArchivoMaterias.actualizar(materia);

        return generarMensajeResultadoCorrelativas(correlativasAgregadas, correlativasExitosas,
                correlativasYaExistentes, correlativasNoEncontradas,
                autoCorrelativas, correlativasCirculares, materiasConCiclo, codigosCorrelativas.size());
    }

    private boolean detectarCicloCompleto(Materia materiaOrigen, Materia nuevaCorrelativa) {
        return buscarCicloRecursivo(nuevaCorrelativa, materiaOrigen, new ArrayList<>());
    }

    private boolean buscarCicloRecursivo(Materia materiaActual, Materia materiaObjetivo, List<Materia> visitadas) {
        if (materiaActual.equals(materiaObjetivo)) {
            return true;
        }

        if (visitadas.contains(materiaActual)) {
            return false;
        }

        visitadas.add(materiaActual);

        for (Materia correlativa : materiaActual.getCorrelativas()) {
            if (buscarCicloRecursivo(correlativa, materiaObjetivo, new ArrayList<>(visitadas))) {
                return true;
            }
        }

        return false;
    }

    private ResultadoOperacion generarMensajeResultadoCorrelativas(int correlativasAgregadas, List<String> correlativasExitosas,
                                                                   int correlativasYaExistentes, int correlativasNoEncontradas, int autoCorrelativas,
                                                                   int correlativasCirculares, List<String> materiasConCiclo, int totalCorrelativas) {

        if (correlativasAgregadas == totalCorrelativas && correlativasYaExistentes == 0 &&
                correlativasNoEncontradas == 0 && autoCorrelativas == 0 && correlativasCirculares == 0) {
            return new ResultadoOperacion(true, "Se agregaron exitosamente todas las correlativas seleccionadas");
        }

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Se agregaron ").append(correlativasAgregadas)
                .append(" de ").append(totalCorrelativas).append(" correlativas seleccionadas");

        if (!correlativasExitosas.isEmpty()) {
            mensaje.append("\n\nCorrelativas agregadas: ")
                    .append(String.join(", ", correlativasExitosas));
        }

        if (correlativasYaExistentes > 0) {
            mensaje.append("\n").append(correlativasYaExistentes).append(" ya eran correlativas");
        }

        if (correlativasNoEncontradas > 0) {
            mensaje.append("\n").append(correlativasNoEncontradas).append(" no fueron encontradas");
        }

        if (autoCorrelativas > 0) {
            mensaje.append("\n").append(autoCorrelativas).append(" auto-correlativas ignoradas");
        }

        if (correlativasCirculares > 0) {
            mensaje.append("\nDependencias circulares detectadas con: ").append(String.join(", ", materiasConCiclo));
        }

        return new ResultadoOperacion(true, mensaje.toString());
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
        Alumno alumno = Alumno.buscarPorLegajo(legajoAlumno, Facultad.getInstance().getAlumnos());
        if (alumno == null) {
            return false;
        }

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

        inscripcion = new InscripcionMateria(alumno, inscripcion.getMateria());

        if (parcialAprobado) inscripcion.aprobarParcial();
        if (finalAprobado) inscripcion.aprobarFinal();
        if (promocionado) inscripcion.otorgarPromocion();

        alumno.getInscripciones().put(inscripcion.getMateria(), inscripcion);
        persistence.ArchivoInscripciones.actualizar();

        return true;
    }
}