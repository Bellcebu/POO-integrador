package model;

public class InscripcionMateria {
    private Alumno alumno;
    private Materia materia;
    private boolean parcialAprobado;
    private boolean finalAprobado;
    private boolean promocionado;

    public InscripcionMateria(Alumno alumno, Materia materia) {
        this.alumno = alumno;
        this.materia = materia;
        this.parcialAprobado = false;
        this.finalAprobado = false;
        this.promocionado = false;
    }

    public Alumno getAlumno() {
        return alumno;
    }
    public Materia getMateria() {
        return materia;
    }
    public boolean aproboParcial() {
        return parcialAprobado;
    }
    public boolean aproboFinal() {
        return finalAprobado;
    }
    public void aprobarParcial() {
        this.parcialAprobado = true;
    }
    public void aprobarFinal() {
        this.finalAprobado = true;
    }
    public boolean promociono() {
        return promocionado;
    }
    public void otorgarPromocion() {
        if (parcialAprobado) {
            this.promocionado = true;
        }
    }
    public boolean aproboMateria() {
        return finalAprobado || promocionado;
    }

    @Override
    public String toString() {
        String estado = "";
        if (promocionado) estado = " [PROMOCIONADO]";
        else if (finalAprobado) estado = " [FINAL APROBADO]";

        return alumno.getNombre() + " - " + materia.getNombre() +
                " (Parcial: " + (parcialAprobado ? "✓" : "✗") +
                ", Final: " + (finalAprobado ? "✓" : "✗") + ")" + estado;
    }
}