package persistence;

import model.Materia;
import java.util.List;
import java.util.ArrayList;

public class ArchivoMaterias {
    private static final String ARCHIVO = "data/materias.txt";

    private static List<Materia> cargarTodos() {
        List<Materia> materias = new ArrayList<>();
        try {
            java.util.List<String> lineas = java.nio.file.Files.readAllLines(
                    java.nio.file.Paths.get(ARCHIVO));

            for (String linea : lineas) {
                if (!linea.trim().isEmpty()) {
                    Materia materia = Materia.fromString(linea);
                    if (materia != null) {
                        materias.add(materia);
                    }
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("Error al cargar materias: " + e.getMessage());
        }
        return materias;
    }

    private static void guardarTodos(List<Materia> materias) {
        try {
            java.io.File archivo = new java.io.File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(archivo));

            for (Materia materia : materias) {
                writer.write(materia.toString());
                writer.newLine();
            }

            writer.close();
        } catch (java.io.IOException e) {
            System.out.println("Error al guardar materias: " + e.getMessage());
        }
    }

    public static void agregar(Materia materia) {
        List<Materia> materias = cargarTodos();
        materias.add(materia);
        guardarTodos(materias);
    }

    public static void actualizar(Materia materiaActualizada) {
        List<Materia> materias = cargarTodos();

        for (int i = 0; i < materias.size(); i++) {
            if (materias.get(i).getCodigo().equals(materiaActualizada.getCodigo())) {
                materias.set(i, materiaActualizada);
                break;
            }
        }

        guardarTodos(materias);
    }
}