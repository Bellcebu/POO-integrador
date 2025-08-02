package controller;

import model.Carrera;
import model.Facultad;
import java.util.List;

public class CarreraController {

    public List<Carrera> obtenerTodas() {
        return Facultad.getInstance().getCarreras();
    }
}