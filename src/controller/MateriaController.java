package controller;

import model.Materia;
import model.Facultad;
import java.util.List;

public class MateriaController {

    public List<Materia> obtenerTodas() {
        return Facultad.getInstance().getMaterias();
    }
}