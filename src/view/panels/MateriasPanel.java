package view.panels;

import view.components.MyLayout;
import controller.MateriaController;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MateriasPanel extends JPanel {

    private MateriaController materiaController;

    public MateriasPanel() {
        this.materiaController = new MateriaController();
        configurarPanel();
    }

    private void configurarPanel() {
        JPanel seccionMaterias = MyLayout.crearSeccion(
                "Materias",
                cargarListaMaterias(),
                e -> {},  // Crear sin acción
                e -> {},  // Editar sin acción
                e -> {},  // Eliminar sin acción
                e -> {}   // Gestionar sin acción
        );

        setLayout(new BorderLayout());
        add(seccionMaterias, BorderLayout.CENTER);
    }

    private List<MyLayout.AlumnoVisual> cargarListaMaterias() {
        return materiaController.obtenerTodas().stream()
                .map(m -> new MyLayout.AlumnoVisual(m.getNombre(), m.getCodigo())) // usando AlumnoVisual para nombre/código
                .collect(Collectors.toList());
    }
}
