package controller;

import model.Materia;
import model.Facultad;

import java.util.ArrayList;
import java.util.List;

public class MateriaController {

    public List<Materia> obtenerTodas() {
        return Facultad.getInstance().getMaterias();
    }

    public List<Materia> buscarMaterias(String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return new ArrayList<>(Facultad.getInstance().getMaterias());
        }

        String busqueda = textoBusqueda.trim().toLowerCase();
        List<Materia> resultados = new ArrayList<>();

        for (Materia materia : Facultad.getInstance().getMaterias()) {
            if (materia.getNombre().toLowerCase().contains(busqueda) ||
                    materia.getCodigo().toLowerCase().contains(busqueda)) {
                resultados.add(materia);
            }
        }

        return resultados;
    }

    public List<Materia> ordenarMateriasAZ() {
        List<Materia> materiasOrdenadas = new ArrayList<>(Facultad.getInstance().getMaterias());
        materiasOrdenadas.sort((m1, m2) -> m1.getNombre().compareToIgnoreCase(m2.getNombre()));
        return materiasOrdenadas;
    }
}