package controller;

import model.*;
import java.io.*;
import java.util.*;

public class MateriaController {
    private static final String ARCHIVO = "data/materias.txt";

    /**
     * CREATE - Crear materia con carrera
     */
    public String crearMateria(String codigo, String nombre, int cuatrimestre,
                               boolean esObligatoria, String codigoCarrera) {
        // Validaciones
        if (codigo == null || codigo.trim().isEmpty()) {
            return "Error: El código es obligatorio";
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }
        if (codigoCarrera == null || codigoCarrera.trim().isEmpty()) {
            return "Error: El código de carrera es obligatorio";
        }
        if (cuatrimestre < 1 || cuatrimestre > 10) {
            return "Error: El cuatrimestre debe estar entre 1 y 10";
        }

        // Limpiar datos
        codigo = codigo.trim().toUpperCase();
        nombre = nombre.trim();
        codigoCarrera = codigoCarrera.trim().toUpperCase();

        // Verificar que la carrera existe
        CarreraController carreraController = new CarreraController();
        if (carreraController.buscarPorCodigo(codigoCarrera) == null) {
            return "Error: No existe la carrera con código " + codigoCarrera;
        }

        List<Materia> materias = cargarMaterias();

        // Validar duplicado
        for (Materia m : materias) {
            if (m.getCodigo().equals(codigo)) {
                return "Error: Ya existe una materia con código " + codigo;
            }
        }

        // Crear y guardar
        materias.add(new Materia(codigo, nombre, cuatrimestre, esObligatoria, codigoCarrera));
        if (guardarMaterias(materias)) {
            return null; // Éxito
        } else {
            return "Error: No se pudo guardar la materia";
        }
    }

    /**
     * READ - Cargar materias (primera pasada, sin correlativas)
     */
    public List<Materia> cargarMaterias() {
        List<Materia> materias = new ArrayList<>();

        try {
            File archivo = new File(ARCHIVO);
            if (!archivo.exists()) {
                archivo.getParentFile().mkdirs();
                return materias;
            }

            BufferedReader reader = new BufferedReader(new FileReader(archivo));
            String linea;

            // Primera pasada: crear materias sin correlativas
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Materia materia = Materia.fromString(linea);
                    if (materia != null) {
                        materias.add(materia);
                    }
                }
            }
            reader.close();

            // Segunda pasada: cargar correlativas
            reader = new BufferedReader(new FileReader(archivo));
            int index = 0;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty() && index < materias.size()) {
                    String[] codigosCorrelativas = Materia.getCodigosCorrelativas(linea);
                    Materia materiaActual = materias.get(index);

                    for (String codigoCorr : codigosCorrelativas) {
                        if (!codigoCorr.trim().isEmpty()) {
                            // Buscar correlativa en la misma carrera
                            for (Materia m : materias) {
                                if (m.getCodigo().equals(codigoCorr.trim()) &&
                                        m.getCodigoCarrera().equals(materiaActual.getCodigoCarrera())) {
                                    materiaActual.agregarCorrelativa(m);
                                    break;
                                }
                            }
                        }
                    }
                    index++;
                }
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Error al cargar materias: " + e.getMessage());
        }

        return materias;
    }

    /**
     * SAVE - Guardar materias
     */
    public boolean guardarMaterias(List<Materia> materias) {
        try {
            File archivo = new File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));

            for (Materia materia : materias) {
                writer.write(materia.toString());
                writer.newLine();
            }

            writer.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error al guardar materias: " + e.getMessage());
            return false;
        }
    }

    /**
     * UTILITY - Buscar por código
     */
    public Materia buscarPorCodigo(String codigo) {
        List<Materia> materias = cargarMaterias();
        for (Materia materia : materias) {
            if (materia.getCodigo().equals(codigo)) {
                return materia;
            }
        }
        return null;
    }

    /**
     * UTILITY - Buscar materias por carrera
     */
    public List<Materia> buscarPorCarrera(String codigoCarrera) {
        List<Materia> materias = cargarMaterias();
        List<Materia> materiasCarrera = new ArrayList<>();

        for (Materia materia : materias) {
            if (materia.getCodigoCarrera().equals(codigoCarrera)) {
                materiasCarrera.add(materia);
            }
        }

        return materiasCarrera;
    }

    /**
     * UTILITY - Listar todas
     */
    public void listarMaterias() {
        List<Materia> materias = cargarMaterias();
        System.out.println("=== MATERIAS ===");
        for (Materia materia : materias) {
            System.out.println("- " + materia.mostrarInfoCompleta());
        }
    }

    /**
     * UPDATE - Actualizar materia
     */
    public String actualizarMateria(String codigoViejo, String codigoNuevo, String nombre,
                                    int cuatrimestre, boolean esObligatoria, String codigoCarrera) {
        // Validaciones básicas
        if (codigoNuevo == null || codigoNuevo.trim().isEmpty()) {
            return "Error: El código es obligatorio";
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El nombre es obligatorio";
        }

        List<Materia> materias = cargarMaterias();

        // Buscar la materia a actualizar
        Materia materiaActualizar = null;
        Set<Materia> correlativasOriginales = null;

        for (Materia m : materias) {
            if (m.getCodigo().equals(codigoViejo)) {
                materiaActualizar = m;
                correlativasOriginales = new HashSet<>(m.getCorrelativas());
                break;
            }
        }

        if (materiaActualizar == null) {
            return "Error: No se encontró la materia con código " + codigoViejo;
        }

        // Si cambió el código, verificar que no exista
        if (!codigoViejo.equals(codigoNuevo)) {
            for (Materia m : materias) {
                if (m.getCodigo().equals(codigoNuevo)) {
                    return "Error: Ya existe una materia con código " + codigoNuevo;
                }
            }
        }

        // Remover la vieja y agregar la actualizada
        materias.remove(materiaActualizar);
        Materia nuevaMateria = new Materia(codigoNuevo, nombre, cuatrimestre, esObligatoria, codigoCarrera);

        // Restaurar correlativas
        for (Materia correlativa : correlativasOriginales) {
            nuevaMateria.agregarCorrelativa(correlativa);
        }

        materias.add(nuevaMateria);

        if (guardarMaterias(materias)) {
            return null; // Éxito
        } else {
            return "Error: No se pudo actualizar la materia";
        }
    }

    /**
     * DELETE - Eliminar materia
     */
    public String eliminarMateria(String codigo) {
        List<Materia> materias = cargarMaterias();

        // Verificar que no sea correlativa de otra materia
        for (Materia m : materias) {
            for (Materia correlativa : m.getCorrelativas()) {
                if (correlativa.getCodigo().equals(codigo)) {
                    return "Error: La materia es correlativa de " + m.getNombre();
                }
            }
        }

        // Buscar y remover
        boolean eliminado = materias.removeIf(m -> m.getCodigo().equals(codigo));

        if (eliminado) {
            if (guardarMaterias(materias)) {
                return null; // Éxito
            } else {
                return "Error: No se pudo guardar los cambios";
            }
        }

        return "Error: No se encontró la materia";
    }

    /**
     * AGREGAR CORRELATIVA
     */
    public String agregarCorrelativa(String codigoMateria, String codigoCorrelativa) {
        List<Materia> materias = cargarMaterias();
        Materia materia = null;
        Materia correlativa = null;

        for (Materia m : materias) {
            if (m.getCodigo().equals(codigoMateria)) materia = m;
            if (m.getCodigo().equals(codigoCorrelativa)) correlativa = m;
        }

        if (materia == null) {
            return "Error: No se encontró la materia " + codigoMateria;
        }
        if (correlativa == null) {
            return "Error: No se encontró la correlativa " + codigoCorrelativa;
        }

        // Verificar que sean de la misma carrera
        if (!materia.getCodigoCarrera().equals(correlativa.getCodigoCarrera())) {
            return "Error: Las materias deben ser de la misma carrera";
        }

        // Verificar que la correlativa sea de un cuatrimestre anterior
        if (correlativa.getCuatrimestre() >= materia.getCuatrimestre()) {
            return "Error: La correlativa debe ser de un cuatrimestre anterior";
        }

        materia.agregarCorrelativa(correlativa);

        if (guardarMaterias(materias)) {
            return null; // Éxito
        } else {
            return "Error: No se pudo guardar los cambios";
        }
    }
}