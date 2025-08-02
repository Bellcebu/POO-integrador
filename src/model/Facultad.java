package  model;
import java.util.*;

public class Facultad {
    private static Facultad instance;
    private List<Carrera> carreras;
    private List<Alumno> alumnos;

    //patron singleton
    private Facultad() {
        this.carreras = new ArrayList<>();
        this.alumnos = new ArrayList<>();
    }
    public static Facultad getInstance() {
        if (instance == null) {
            instance = new Facultad();
        }
        return instance;
    }

    //get y set
    public List<Carrera> getCarreras() { return carreras; }
    public List<Alumno> getAlumnos() { return alumnos; }

    //funciones add
    public void agregarCarrera(Carrera carrera) {
        carreras.add(carrera);
    }
    public boolean agregarAlumno(Alumno alumno) {
        for (Alumno a : alumnos) {
            if (a.equals(alumno)) {
                System.out.println("Error: Ya existe un alumno con legajo " + alumno.getLegajo());
                return false;
            }
        }
        alumnos.add(alumno);
        return true;
    }

    //toString
    @Override
    public String toString() {
        return "Facultad - Carreras: " + carreras.size() + ", Alumnos: " + alumnos.size();
    }
}