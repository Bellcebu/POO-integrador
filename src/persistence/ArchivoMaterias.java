package persistence;

import model.Materia;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ArchivoMaterias {
    private static final String ARCHIVO = "data/materias.txt";

    private static List<Materia> cargarTodos() {
        List<Materia> materias = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO));
            for (String linea : lineas) {
                if (!linea.trim().isEmpty()) {
                    Materia materia = Materia.fromString(linea);
                    if (materia != null) {
                        materias.add(materia);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar materias: " + e.getMessage());
        }
        return materias;
    }

    private static void guardarTodos(List<Materia> materias) {
        try {
            File archivo = new File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
            for (Materia materia : materias) {
                writer.write(materia.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error al guardar materias: " + e.getMessage());
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