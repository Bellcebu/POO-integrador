package persistence;

import model.*;
import java.util.List;

public class ArchivoInscripciones {
    private static final String ARCHIVO = "data/inscripciones.txt";

    public static void actualizar() {
        try {
            java.io.File archivo = new java.io.File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(archivo));

            // Recorrer todos los alumnos y sus inscripciones
            for (Alumno alumno : Facultad.getInstance().getAlumnos()) {
                for (InscripcionMateria inscripcion : alumno.getInscripciones().values()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(alumno.getLegajo()).append("☆");
                    sb.append(inscripcion.getMateria().getCodigo()).append("☆");
                    sb.append(inscripcion.aproboParcial()).append("☆");
                    sb.append(inscripcion.aproboFinal()).append("☆");
                    sb.append(inscripcion.promociono());

                    writer.write(sb.toString());
                    writer.newLine();
                }
            }

            writer.close();
        } catch (java.io.IOException e) {
            System.out.println("Error al actualizar inscripciones: " + e.getMessage());
        }
    }
}