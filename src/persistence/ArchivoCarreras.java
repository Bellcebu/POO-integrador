package persistence;

import model.Carrera;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ArchivoCarreras {
    private static final String ARCHIVO = "data/carreras.txt";

    private static List<Carrera> cargarTodos() {
        List<Carrera> carreras = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO));
            for (String linea : lineas) {
                if (!linea.trim().isEmpty()) {
                    Carrera carrera = Carrera.fromString(linea);
                    if (carrera != null) {
                        carreras.add(carrera);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar carreras: " + e.getMessage());
        }
        return carreras;
    }

    private static void guardarTodos(List<Carrera> carreras) {
        try {
            File archivo = new File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
            for (Carrera carrera : carreras) {
                writer.write(carrera.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error al guardar carreras: " + e.getMessage());
        }
    }

    public static void agregar(Carrera carrera) {
        List<Carrera> carreras = cargarTodos();
        carreras.add(carrera);
        guardarTodos(carreras);
    }

    public static void actualizar(Carrera carreraActualizada) {
        List<Carrera> carreras = cargarTodos();

        for (int i = 0; i < carreras.size(); i++) {
            if (carreras.get(i).getCodigo().equals(carreraActualizada.getCodigo())) {
                carreras.set(i, carreraActualizada);
                break;
            }
        }

        guardarTodos(carreras);
    }
}
