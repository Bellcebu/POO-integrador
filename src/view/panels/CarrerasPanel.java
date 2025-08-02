package view.panels;

import model.Carrera;
import model.Facultad;
import view.components.MyLayout;
import controller.CarreraController;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CarrerasPanel extends JPanel {

    private CarreraController carreraController;
    private boolean ordenAZ = false;
    private String textoBusqueda = "";

    public CarrerasPanel() {
        this.carreraController = new CarreraController();
        configurarPanel();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout());

        JPanel seccionCarreras = MyLayout.crearSeccion(
                "Carreras",
                cargarListaCarreras(),
                e -> {}, // Crear (sin implementar aún)
                e -> {}, // Editar (sin implementar aún)
                e -> {}, // Eliminar (sin implementar aún)
                e -> {}, // Gestionar (sin implementar aún)
                e -> { ordenAZ = !ordenAZ; actualizarLista(); },
                ordenAZ ? "A→Z" : "Z→A",
                e -> { // NUEVO: ActionListener para búsqueda
                    textoBusqueda = e.getActionCommand();
                    actualizarLista();
                },
                "Buscar por nombre o código" // NUEVO: Placeholder
        );

        add(seccionCarreras, BorderLayout.CENTER);
    }

    private void actualizarLista() {
        removeAll();
        configurarPanel();
        revalidate();
        repaint();
    }

    private List<MyLayout.AlumnoVisual> cargarListaCarreras() {
        List<Carrera> carreras = carreraController.buscarCarreras(textoBusqueda);
        if (ordenAZ) {
            carreras.sort((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
        } else {
            carreras.sort((c1, c2) -> c2.getNombre().compareToIgnoreCase(c1.getNombre()));
        }
        return carreras.stream()
                .map(c -> new MyLayout.AlumnoVisual(c.getNombre(), c.getCodigo()))
                .collect(Collectors.toList());
    }
}