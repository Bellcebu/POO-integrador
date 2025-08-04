package controller;

import model.*;
import persistence.ArchivoCarreras;
import java.util.List;
import java.util.ArrayList;

public class CarreraController {

    public boolean agregarAccion(String codigo, String nombre, int cantidadOptativas, String tipoPlan) {
        for (Carrera c : Facultad.getInstance().getCarreras()) {
            if (c.getCodigo().equals(codigo)) {
                return false;
            }
        }

        PlanEstudio plan = crearPlan(tipoPlan);
        if (plan == null) {
            return false;
        }

        Carrera nuevaCarrera = new Carrera(codigo, nombre, cantidadOptativas, plan);
        Facultad.getInstance().getCarreras().add(nuevaCarrera);
        ArchivoCarreras.agregar(nuevaCarrera);

        return true;
    }

    public boolean inscribirAlumnosACarrera(String codigoCarrera, List<String> legajosAlumnos) {
        Carrera carrera = null;
        for (Carrera c : Facultad.getInstance().getCarreras()) {
            if (c.getCodigo().equals(codigoCarrera)) {
                carrera = c;
                break;
            }
        }

        if (carrera == null) {
            return false;
        }

        for (String legajo : legajosAlumnos) {
            Alumno alumno = Alumno.buscarPorLegajo(legajo, Facultad.getInstance().getAlumnos());
            if (alumno != null) {
                if (!carrera.getAlumnos().contains(alumno)) {
                    carrera.inscribirAlumno(alumno);
                }
            }
        }

        ArchivoCarreras.actualizar(carrera);
        return true;
    }

    public boolean agregarMateriasACarrera(String codigoCarrera, List<String> codigosMaterias) {
        Carrera carrera = null;
        for (Carrera c : Facultad.getInstance().getCarreras()) {
            if (c.getCodigo().equals(codigoCarrera)) {
                carrera = c;
                break;
            }
        }

        if (carrera == null) {
            return false;
        }

        for (String codigoMateria : codigosMaterias) {
            Materia materia = Materia.buscarPorCodigo(codigoMateria, Facultad.getInstance().getMaterias());
            if (materia != null) {
                if (!carrera.getMaterias().contains(materia)) {
                    carrera.agregarMateria(materia);
                }
            }
        }

        ArchivoCarreras.actualizar(carrera);
        return true;
    }

    public Carrera buscarCarreraPorCodigo(String codigo) {
        for (Carrera carrera : Facultad.getInstance().getCarreras()) {
            if (carrera.getCodigo().equals(codigo)) {
                return carrera;
            }
        }
        return null;
    }

    public List<Carrera> buscarCarreras(String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return new ArrayList<>(Facultad.getInstance().getCarreras());
        }

        String busqueda = textoBusqueda.trim().toLowerCase();
        List<Carrera> resultados = new ArrayList<>();

        for (Carrera carrera : Facultad.getInstance().getCarreras()) {
            if (carrera.getNombre().toLowerCase().contains(busqueda) ||
                    carrera.getCodigo().toLowerCase().contains(busqueda)) {
                resultados.add(carrera);
            }
        }

        return resultados;
    }

    private PlanEstudio crearPlan(String tipoPlan) {
        switch (tipoPlan.toUpperCase()) {
            case "PLANA": return new PlanA();
            case "PLANB": return new PlanB();
            case "PLANC": return new PlanC();
            case "PLAND": return new PlanD();
            case "PLANE": return new PlanE();
            default: return null;
        }
    }
}