package model;

public class PlanA implements PlanEstudio {
    @Override
    public boolean puedeCursar(Materia materia, Alumno alumno) {
        for (Materia correlativa : materia.getCorrelativas()) {
            if (!alumno.aproboCursada(correlativa)) {
                return false;
            }
        }
        return true;
    }
}