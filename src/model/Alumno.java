package model;
import java.util.*;

public class Alumno {
    private static final String SEPARADOR = "☆";

    private String nombre;
    private String legajo;
    private Map<Materia, InscripcionMateria> inscripciones;

    public Alumno(String nombre, String legajo) {
        this.nombre = nombre;
        this.legajo = legajo;
        this.inscripciones = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }
    public String getLegajo() {
        return legajo;
    }
    public Map<Materia, InscripcionMateria> getInscripciones() {
        return inscripciones;
    }
    public InscripcionMateria getInscripcion(Materia materia) {
        for (Map.Entry<Materia, InscripcionMateria> entry : inscripciones.entrySet()) {
            if (entry.getKey().getCodigo().equals(materia.getCodigo())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void agregarInscripcion(Materia materia, InscripcionMateria inscripcion) {
        inscripciones.put(materia, inscripcion);
    }

    public boolean estaInscriptoEn(Materia materia) {
        for (Materia m : inscripciones.keySet()) {
            if (m.getCodigo().equals(materia.getCodigo())) {
                return true;
            }
        }
        return false;
    }

    public static Alumno buscarPorLegajo(String legajo, List<Alumno> alumnos) {
        for (Alumno alumno : alumnos) {
            if (alumno.getLegajo().equals(legajo)) {
                return alumno;
            }
        }
        return null;
    }

    public List<Materia> getMateriasAprobadas() {
        List<Materia> aprobadas = new ArrayList<>();
        for (Map.Entry<Materia, InscripcionMateria> entry : inscripciones.entrySet()) {
            if (entry.getValue().aproboMateria()) {
                aprobadas.add(entry.getKey());
            }
        }
        return aprobadas;
    }

    public List<Materia> getMateriasCursadasAprobadas() {
        List<Materia> cursadas = new ArrayList<>();
        for (Map.Entry<Materia, InscripcionMateria> entry : inscripciones.entrySet()) {
            if (entry.getValue().aproboParcial()) {
                cursadas.add(entry.getKey());
            }
        }
        return cursadas;
    }

    public boolean aproboCursada(Materia materia) {
        for (Map.Entry<Materia, InscripcionMateria> entry : inscripciones.entrySet()) {
            if (entry.getKey().getCodigo().equals(materia.getCodigo())) {
                return entry.getValue().aproboParcial();
            }
        }
        return false;
    }

    public boolean aproboFinal(Materia materia) {
        for (Map.Entry<Materia, InscripcionMateria> entry : inscripciones.entrySet()) {
            if (entry.getKey().getCodigo().equals(materia.getCodigo())) {
                InscripcionMateria insc = entry.getValue();
                return insc.aproboFinal() || insc.promociono();
            }
        }
        return false;
    }

    public boolean aproboMateria(Materia materia) {
        for (Map.Entry<Materia, InscripcionMateria> entry : inscripciones.entrySet()) {
            if (entry.getKey().getCodigo().equals(materia.getCodigo())) {
                return entry.getValue().aproboMateria();
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Alumno alumno = (Alumno) obj;
        return Objects.equals(legajo, alumno.legajo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(legajo);
    }

    @Override
    public String toString() {
        return nombre + SEPARADOR + legajo;
    }

    public static Alumno fromString(String linea) {
        try {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 2) {
                String nombre = partes[0].trim();
                String legajo = partes[1].trim();
                return new Alumno(nombre, legajo);
            }
        } catch (Exception e) {
            System.out.println("Error al parsear alumno: " + linea);
        }
        return null;
    }

    public String mostrarInfo() {
        return nombre + " (" + legajo + ")";
    }

    public String mostrarInfoDetallada() {
        StringBuilder sb = new StringBuilder();
        sb.append("Alumno: ").append(mostrarInfo()).append("\n");
        sb.append("Materias inscriptas: ").append(inscripciones.size()).append("\n");
        sb.append("Materias aprobadas: ").append(getMateriasAprobadas().size());
        return sb.toString();
    }
}