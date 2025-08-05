package persistence;

import model.Alumno;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ArchivoAlumnos {
    private static final String ARCHIVO = "data/alumnos.txt";

    private static List<Alumno> cargarTodos() {
        List<Alumno> alumnos = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO));
            for (String linea : lineas) {
                if (!linea.trim().isEmpty()) {
                    Alumno alumno = Alumno.fromString(linea);
                    if (alumno != null) {
                        alumnos.add(alumno);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de alumnos: " + e.getMessage());
        }
        return alumnos;
    }

    private static void guardarTodos(List<Alumno> alumnos) {
        try {
            File archivo = new File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
            for (Alumno alumno : alumnos) {
                writer.write(alumno.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error al guardar los alumnos: " + e.getMessage());
        }
    }

    public static void agregar(Alumno alumno) {
        List<Alumno> alumnos = cargarTodos();
        alumnos.add(alumno);
        guardarTodos(alumnos);
    }
}