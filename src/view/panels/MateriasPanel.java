package view.panels;

import model.*;
import controller.*;
import controller.MateriaController.ResultadoOperacion;
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

                    ResultadoOperacion resultado = materiaController.agregarAccion(codigo, nombre, cuatrimestre, esObligatoria);

                    if (resultado.isExito()) {
                        JOptionPane.showMessageDialog(this, resultado.getMensaje(),
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        volverAListaPrincipal();
                    } else {
                        JOptionPane.showMessageDialog(this, resultado.getMensaje(),
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
                        List<String> correlativasSeleccionadas = agregarCorrelativasPanel.getCorrelativasSeleccionadas();
                        ResultadoOperacion resultado = materiaController.agregarCorrelativasAMateria(codigoMateria, correlativasSeleccionadas);

                        if (resultado.isExito()) {
                            JOptionPane.showMessageDialog(this, resultado.getMensaje(),
                                    "Correlativas Agregadas", JOptionPane.INFORMATION_MESSAGE);
                            volverAListaPrincipal();
                        } else {
                            JOptionPane.showMessageDialog(this, resultado.getMensaje(),
                                    "Error al Agregar Correlativas", JOptionPane.ERROR_MESSAGE);
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