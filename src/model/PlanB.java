package model;

public class PlanB implements PlanEstudio {
    @Override
    public boolean puedeCursar(Materia materia, Alumno alumno) {
        for (Materia correlativa : materia.getCorrelativas()) {
            if (!alumno.aproboFinal(correlativa)) {
                return false;
            }
        }
        return true;
    }
}