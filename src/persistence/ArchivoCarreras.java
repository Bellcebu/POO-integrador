package persistence;

import model.Carrera;
import java.util.List;
import java.util.ArrayList;

public class ArchivoCarreras {
    private static final String ARCHIVO = "data/carreras.txt";

    private static List<Carrera> cargarTodos() {
        List<Carrera> carreras = new ArrayList<>();
        try {
            java.util.List<String> lineas = java.nio.file.Files.readAllLines(
                    java.nio.file.Paths.get(ARCHIVO));

            for (String linea : lineas) {
                if (!linea.trim().isEmpty()) {
                    Carrera carrera = Carrera.fromString(linea);
                    if (carrera != null) {
                        carreras.add(carrera);
                    }
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("Error al cargar carreras: " + e.getMessage());
        }
        return carreras;
    }

    private static void guardarTodos(List<Carrera> carreras) {
        try {
            java.io.File archivo = new java.io.File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(archivo));

            for (Carrera carrera : carreras) {
                writer.write(carrera.toString());
                writer.newLine();
            }

            writer.close();
        } catch (java.io.IOException e) {
            System.out.println("Error al guardar carreras: " + e.getMessage());
        }
    }

    public static void agregar(Carrera carrera) {
        List<Carrera> carreras = cargarTodos();
        carreras.add(carrera);
        guardarTodos(carreras);
    }

    public static void actualizar(Carrera carreraActualizada) {
        List<Carrera> carreras = cargarTodos();

        // Buscar y reemplazar la carrera por código
        for (int i = 0; i < carreras.size(); i++) {
            if (carreras.get(i).getCodigo().equals(carreraActualizada.getCodigo())) {
                carreras.set(i, carreraActualizada);
                break;
            }
        }

        guardarTodos(carreras);
    }
}