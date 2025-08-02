package view.panels;

import view.components.MyLayout;
import controller.CarreraController;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CarrerasPanel extends JPanel {

    private CarreraController carreraController;
    private boolean ordenAZ = true;

    public CarrerasPanel() {
        this.carreraController = new CarreraController();
        configurarPanel();
    }

    private void configurarPanel() {
        JPanel seccionCarreras = MyLayout.crearSeccion(
                "Carreras",
                cargarListaCarreras(),
                e -> {},  // Crear
                e -> {},  // Editar
                e -> {},  // Eliminar
                e -> {} ,  // Gestionar
                e -> {ordenAZ = !ordenAZ;actualizarLista();}, ordenAZ ? "A→Z" : "Z→A"
        );

        setLayout(new BorderLayout());
        add(seccionCarreras, BorderLayout.CENTER);
    }

    private void actualizarLista() {
        removeAll();
        configurarPanel();
        revalidate();
        repaint();
    }

    private List<MyLayout.AlumnoVisual> cargarListaCarreras() {
        return carreraController.obtenerTodas().stream()
                .map(c -> new MyLayout.AlumnoVisual(c.getNombre(), c.getCodigo())) // usa AlumnoVisual aunque es para mostrar nombre/codigo
                .collect(Collectors.toList());
    }
}
