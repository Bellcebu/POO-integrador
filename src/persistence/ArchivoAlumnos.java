package persistence;

import model.Alumno;
import java.util.List;
import java.util.ArrayList;

public class ArchivoAlumnos {
    private static final String ARCHIVO = "data/alumnos.txt";

    private static List<Alumno> cargarTodos() {
        List<Alumno> alumnos = new ArrayList<>();
        try {
            java.util.List<String> lineas = java.nio.file.Files.readAllLines(
                    java.nio.file.Paths.get(ARCHIVO));

            for (String linea : lineas) {
                if (!linea.trim().isEmpty()) {
                    Alumno alumno = Alumno.fromString(linea);
                    if (alumno != null) {
                        alumnos.add(alumno);
                    }
                }
            }
        } catch (java.io.IOException e) {
        }
        return alumnos;
    }

    private static void guardarTodos(List<Alumno> alumnos) {
        try {
            java.io.File archivo = new java.io.File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(archivo));

            for (Alumno alumno : alumnos) {
                writer.write(alumno.toString());
                writer.newLine();
            }

            writer.close();
        } catch (java.io.IOException e) {
        }
    }

    public static void agregar(Alumno alumno) {
        List<Alumno> alumnos = cargarTodos();
        alumnos.add(alumno);
        guardarTodos(alumnos);
    }

}