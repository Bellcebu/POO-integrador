package view.panels;

import model.*;
import controller.*;
import view.components.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MateriasPanel extends JPanel {

    private MateriaController materiaController;
    private MateriaFormPanel formularioMateria;
    private AgregarCorrelativasPanel agregarCorrelativasPanel;
    private CalificarPanel calificarPanel;
    private InfoMateriaPanel infoMateriaPanel;
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
                e -> crearMateria(),
                e -> agregarCorrelativas(e.getActionCommand()),
                e -> calificar(e.getActionCommand()),
                e -> mostrarInfo(e.getActionCommand()),
                e -> {
                    ordenAZ = !ordenAZ;
                    actualizarLista();
                },
                ordenAZ ? "A→Z" : "Z→A",
                e -> {
                    textoBusqueda = e.getActionCommand();
                    actualizarLista();
                },
                "Buscar por nombre o código",
                "Agregar Correlativas",
                "Calificar",
                "Informacion"
        );

        add(seccionMaterias, BorderLayout.CENTER);
    }

    private void crearMateria() {
        formularioMateria = new MateriaFormPanel(
                e -> {
                    String codigo = formularioMateria.getCodigo();
                    String nombre = formularioMateria.getNombre();
                    int cuatrimestre = formularioMateria.getCuatrimestre();
                    boolean esObligatoria = formularioMateria.esObligatoria();

                    if (codigo.isEmpty() || nombre.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Código y Nombre son obligatorios",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (cuatrimestre <= 0) {
                        JOptionPane.showMessageDialog(this, "Cuatrimestre debe ser un número válido mayor a 0",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean exito = materiaController.agregarAccion(codigo, nombre, cuatrimestre, esObligatoria);
                    if (exito) {
                        JOptionPane.showMessageDialog(this, "Materia creada exitosamente.\nPuedes asignarla a carreras desde el panel de cada carrera.",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        volverAListaPrincipal();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error: El código de materia ya existe",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                },
                e -> volverAListaPrincipal()
        );
        mostrarFormulario();
    }

    private void agregarCorrelativas(String codigoMateria) {
        Materia materia = materiaController.buscarMateriaPorCodigo(codigoMateria);
        if (materia != null) {
            agregarCorrelativasPanel = new AgregarCorrelativasPanel(materia,
                    e -> {
                        if (!agregarCorrelativasPanel.haySeleccion()) {
                            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos una materia",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        List<String> correlativasSeleccionadas = agregarCorrelativasPanel.getCorrelativasSeleccionadas();
                        boolean exito = materiaController.agregarCorrelativasAMateria(codigoMateria, correlativasSeleccionadas);

                        if (exito) {
                            JOptionPane.showMessageDialog(this, "Correlativas agregadas exitosamente",
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            volverAListaPrincipal();
                        } else {
                            JOptionPane.showMessageDialog(this, "Error al agregar correlativas",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    },
                    e -> volverAListaPrincipal()
            );
            mostrarFormularioAgregarCorrelativas();
        }
    }

    private void calificar(String codigoMateria) {
        Materia materia = materiaController.buscarMateriaPorCodigo(codigoMateria);
        if (materia != null) {
            calificarPanel = new CalificarPanel(materia, materiaController,
                    e -> volverAListaPrincipal(),
                    e -> volverAListaPrincipal()
            );
            mostrarPanelCalificar();
        }
    }

    private void mostrarInfo(String codigoMateria) {
        Materia materia = materiaController.buscarMateriaPorCodigo(codigoMateria);
        if (materia != null) {
            infoMateriaPanel = new InfoMateriaPanel(materia, materiaController, e -> volverAListaPrincipal());
            mostrarPanelInfo();
        }
    }

    private void actualizarLista() {
        removeAll();
        configurarPanel();
        revalidate();
        repaint();
    }

    private List<MyLayout.AlumnoVisual> cargarListaMaterias() {
        List<Materia> materias = materiaController.buscarMaterias(textoBusqueda);

        if (ordenAZ) {
            materias.sort((m1, m2) -> m1.getNombre().compareToIgnoreCase(m2.getNombre()));
        } else {
            materias.sort((m1, m2) -> m2.getNombre().compareToIgnoreCase(m1.getNombre()));
        }

        return materias.stream()
                .map(m -> new MyLayout.AlumnoVisual(m.getNombre(), m.getCodigo()))
                .collect(Collectors.toList());
    }

    private void mostrarFormulario() {
        removeAll();
        add(formularioMateria, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrarFormularioAgregarCorrelativas() {
        removeAll();
        add(agregarCorrelativasPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrarPanelCalificar() {
        removeAll();
        add(calificarPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrarPanelInfo() {
        removeAll();
        add(infoMateriaPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void volverAListaPrincipal() {
        removeAll();
        configurarPanel();
        revalidate();
        repaint();
    }
}
