package controller;

import model.Carrera;
import model.Facultad;

import java.util.ArrayList;
import java.util.List;

public class CarreraController {

    public List<Carrera> obtenerTodas() {
        return Facultad.getInstance().getCarreras();
    }

    public void agregarCarrera(Carrera carrera) {
        Facultad.getInstance().getCarreras().add(carrera);
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

    public List<Carrera> ordenarCarrerasAZ() {
        List<Carrera> carrerasOrdenadas = new ArrayList<>(Facultad.getInstance().getCarreras());
        carrerasOrdenadas.sort((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
        return carrerasOrdenadas;
    }

    public List<Carrera> ordenarCarrerasZA() {
        List<Carrera> carrerasOrdenadas = new ArrayList<>(Facultad.getInstance().getCarreras());
        carrerasOrdenadas.sort((c1, c2) -> c2.getNombre().compareToIgnoreCase(c1.getNombre()));
        return carrerasOrdenadas;
    }
}