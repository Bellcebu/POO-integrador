package model;

import java.util.*;

public class Carrera {
    private static final String SEPARADOR = "☆";

    private String codigo;
    private String nombre;
    private int cantidadOptativasNecesarias;
    private PlanEstudio planEstudio;
    private List<Materia> materias;
    private List<Alumno> alumnos;

    public Carrera(String codigo, String nombre, int cantidadOptativasNecesarias, PlanEstudio planEstudio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidadOptativasNecesarias = cantidadOptativasNecesarias;
        this.planEstudio = planEstudio;
        this.materias = new ArrayList<>();
        this.alumnos = new ArrayList<>();
    }

    //get y set
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getCantidadOptativasNecesarias() { return cantidadOptativasNecesarias; }
    public PlanEstudio getPlanEstudio() { return planEstudio; }
    public List<Materia> getMaterias() { return materias; }
    public List<Alumno> getAlumnos() { return alumnos; }
    public static String[] getCodigosMaterias(String linea) {
        try {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 5 && !partes[4].trim().isEmpty()) {
                return partes[4].trim().split(",");
            }
        } catch (Exception e) {
            System.out.println("Error al obtener materias: " + linea);
        }
        return new String[0];
    }
    public static String[] getLegajosAlumnos(String linea) {
        try {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 6 && !partes[5].trim().isEmpty()) {
                return partes[5].trim().split(",");
            }
        } catch (Exception e) {
            System.out.println("Error al obtener alumnos: " + linea);
        }
        return new String[0];
    }
    private String getTipoPlan() {
        return planEstudio.getClass().getSimpleName().toUpperCase();
    }

    // funciones add
    public void agregarMateria(Materia materia) {
        materias.add(materia);
    }

    public boolean inscribirAlumno(Alumno alumno) {
        for (Alumno a : alumnos) {
            if (a.equals(alumno)) {
                System.out.println("Error: El alumno ya está inscripto en esta carrera");
                return false;
            }
        }
        alumnos.add(alumno);
        return true;
    }

    public boolean puedeInscribirseA(Materia materia, Alumno alumno) {
        return materias.contains(materia) && planEstudio.puedeCursar(materia, alumno);
    }

    public boolean finalizoCarrera(Alumno alumno) {
        int obligatoriasAprobadas = 0;
        int totalObligatorias = 0;
        int optativasAprobadas = 0;
        for (Materia materia : materias) {
            if (materia.esObligatoria()) {
                totalObligatorias++;
                InscripcionMateria insc = alumno.getInscripcion(materia);
                if (insc != null && insc.aproboMateria()) {
                    obligatoriasAprobadas++;
                }
            } else {
                InscripcionMateria insc = alumno.getInscripcion(materia);
                if (insc != null && insc.aproboMateria()) {
                    optativasAprobadas++;
                }
            }
        }
        return obligatoriasAprobadas == totalObligatorias &&
                optativasAprobadas >= cantidadOptativasNecesarias;
    }

    // toString y fromString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(codigo).append(SEPARADOR);
        sb.append(nombre).append(SEPARADOR);
        sb.append(cantidadOptativasNecesarias).append(SEPARADOR);
        sb.append(getTipoPlan()).append(SEPARADOR);
        if (!materias.isEmpty()) {
            boolean primera = true;
            for (Materia materia : materias) {
                if (!primera) {
                    sb.append(",");
                }
                sb.append(materia.getCodigo());
                primera = false;
            }
        }
        sb.append(SEPARADOR);
        if (!alumnos.isEmpty()) {
            boolean primero = true;
            for (Alumno alumno : alumnos) {
                if (!primero) {
                    sb.append(",");
                }
                sb.append(alumno.getLegajo());
                primero = false;
            }
        }
        return sb.toString();
    }

    public static Carrera fromString(String linea) {
        try {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 4) {
                String codigo = partes[0].trim();
                String nombre = partes[1].trim();
                int cantidadOptativas = Integer.parseInt(partes[2].trim());
                String tipoPlan = partes[3].trim();

                PlanEstudio plan = crearPlan(tipoPlan);
                if (plan != null) {
                    return new Carrera(codigo, nombre, cantidadOptativas, plan);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al parsear carrera: " + linea);
        }
        return null;
    }

    private static PlanEstudio crearPlan(String tipoPlan) {
        switch (tipoPlan.toUpperCase()) {
            case "PLANA": return new PlanA();
            case "PLANB": return new PlanB();
            case "PLANC": return new PlanC();
            case "PLAND": return new PlanD();
            case "PLANE": return new PlanE();
            default:
                System.out.println("Tipo de plan desconocido: " + tipoPlan);
                return null;
        }
    }

    //mostrar en terminal
    public String mostrarInfo() {
        return nombre + " (" + codigo + ")";
    }
    public String mostrarInfoCompleta() {
        return String.format("%s - %s - %d optativas - %s - %d materias - %d alumnos",
                codigo, nombre, cantidadOptativasNecesarias,
                getTipoPlan(), materias.size(), alumnos.size());
    }
}