package view.panels;

import model.Materia;
import model.Facultad;
import view.components.MyLayout;
import controller.MateriaController;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MateriasPanel extends JPanel {

    private MateriaController materiaController;
    private boolean ordenAZ = false;
    private String textoBusqueda = "";

    public MateriasPanel() {
        this.materiaController = new MateriaController();
        configurarPanel();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout());

        JPanel seccionMaterias = MyLayout.crearSeccion(
                "Materias",
                cargarListaMaterias(),
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

        add(seccionMaterias, BorderLayout.CENTER);
    }

    private void actualizarLista() {
        removeAll();
        configurarPanel();
        revalidate();
        repaint();
    }

    private List<MyLayout.AlumnoVisual> cargarListaMaterias() {
        List<Materia> materias = Facultad.getInstance().buscarMaterias(textoBusqueda);

        // Aplicar ordenamiento
        if (ordenAZ) {
            materias.sort((m1, m2) -> m1.getNombre().compareToIgnoreCase(m2.getNombre()));
        } else {
            materias.sort((m1, m2) -> m2.getNombre().compareToIgnoreCase(m1.getNombre()));
        }

        return materias.stream()
                .map(m -> new MyLayout.AlumnoVisual(m.getNombre(), m.getCodigo()))
                .collect(Collectors.toList());
    }
}