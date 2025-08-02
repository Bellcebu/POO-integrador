package view.panels;

import view.components.MyLayout;
import view.components.MyLayout.AlumnoVisual;
import controller.AlumnoController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AlumnosPanel extends JPanel {

    private AlumnoController alumnoController;

    public AlumnosPanel() {
        this.alumnoController = new AlumnoController();
        configurarPanel();
    }

    private void configurarPanel() {
        JPanel seccionAlumnos = MyLayout.crearSeccion(
                "Alumnos",
                cargarListaAlumnos(),
                e -> {},  // Crear
                e -> {},  // Editar
                e -> {},  // Eliminar
                e -> {}
        );

        setLayout(new BorderLayout());
        add(seccionAlumnos, BorderLayout.CENTER);
    }

    private List<AlumnoVisual> cargarListaAlumnos() {
        return alumnoController.obtenerTodos().stream()
                .map(a -> new AlumnoVisual(a.getNombre(), a.getLegajo()))
                .collect(Collectors.toList());
    }
}
