package model;

public class PlanC implements PlanEstudio {
    @Override
    public boolean puedeCursar(Materia materia, Alumno alumno) {
        for (Materia correlativa : materia.getCorrelativas()) {
            if (!alumno.aproboCursada(correlativa)) {
                return false;
            }
        }

        int cuatrimestreMateria = materia.getCuatrimestre();
        for (InscripcionMateria inscripcion : alumno.getInscripciones().values()) {
            Materia m = inscripcion.getMateria();
            if (m.getCuatrimestre() <= cuatrimestreMateria - 5) {
                if (!materia.getCorrelativas().contains(m)) {
                    if (!alumno.aproboFinal(m)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}