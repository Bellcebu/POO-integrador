package persistence;

import model.*;

import java.io.*;
import java.util.List;

public class ArchivoInscripciones {
    private static final String ARCHIVO = "data/inscripciones.txt";

    public static void actualizar() {
        try {
            File archivo = new File(ARCHIVO);
            archivo.getParentFile().mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));

            for (Alumno alumno : Facultad.getInstance().getAlumnos()) {
                for (InscripcionMateria inscripcion : alumno.getInscripciones().values()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(alumno.getLegajo()).append("☆")
                            .append(inscripcion.getMateria().getCodigo()).append("☆")
                            .append(inscripcion.aproboParcial()).append("☆")
                            .append(inscripcion.aproboFinal()).append("☆")
                            .append(inscripcion.promociono());

                    writer.write(sb.toString());
                    writer.newLine();
                }
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Error al actualizar inscripciones: " + e.getMessage());
        }
    }
}
