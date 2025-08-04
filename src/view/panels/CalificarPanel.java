package view.panels;

import model.*;
import view.components.*;
import view.config.ThemeConfig;
import controller.MateriaController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CalificarPanel extends JPanel {

    private Materia materia;
    private MateriaController materiaController;
    private List<InscripcionMateria> inscripciones;
    private Map<String, JCheckBox> checkboxesParcial;
    private Map<String, JCheckBox> checkboxesFinal;
    private Map<String, JCheckBox> checkboxesPromocion;
    private MyButton btnGuardar;
    private MyButton btnCancelar;
    private ActionListener onGuardar;
    private ActionListener onCancelar;

    public CalificarPanel(Materia materia, MateriaController materiaController,
                          ActionListener onGuardar, ActionListener onCancelar) {
        this.materia = materia;
        this.materiaController = materiaController;
        this.onGuardar = onGuardar;
        this.onCancelar = onCancelar;
        this.checkboxesParcial = new HashMap<>();
        this.checkboxesFinal = new HashMap<>();
        this.checkboxesPromocion = new HashMap<>();

        configurarPanel();
        cargarInscripciones();
        crearComponentes();
    }

    private void configurarPanel() {
        setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setLayout(new BorderLayout());
    }

    private void cargarInscripciones() {
        inscripciones = materiaController.obtenerInscripcionesPorMateria(materia.getCodigo());
    }

    private void crearComponentes() {
        // Título
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.add(MyLabel.titulo("Calificar: " + materia.getNombre()));
        add(headerPanel, BorderLayout.NORTH);

        // Panel principal con scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        if (inscripciones.isEmpty()) {
            mainPanel.add(Box.createVerticalStrut(50));
            JPanel emptyPanel = new JPanel(new FlowLayout());
            emptyPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
            emptyPanel.add(MyLabel.info("No hay alumnos inscriptos en esta materia"));
            mainPanel.add(emptyPanel);
        } else {
            // Encabezado de la tabla
            JPanel headerTablePanel = crearHeaderTabla();
            mainPanel.add(headerTablePanel);
            mainPanel.add(Box.createVerticalStrut(10));

            // Filas de alumnos
            for (InscripcionMateria inscripcion : inscripciones) {
                JPanel filaPanel = crearFilaAlumno(inscripcion);
                mainPanel.add(filaPanel);
                mainPanel.add(Box.createVerticalStrut(5));
            }
        }

        MyScroll scrollPanel = MyScroll.crearVertical(mainPanel);
        add(scrollPanel, BorderLayout.CENTER);

        // Botones
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        btnGuardar = MyButton.boton6("Guardar Cambios", e -> guardarCambios());
        btnCancelar = MyButton.boton7("Cancelar", onCancelar);

        if (!inscripciones.isEmpty()) {
            bottomPanel.add(btnGuardar);
        }
        bottomPanel.add(btnCancelar);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel crearHeaderTabla() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 5));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        panel.add(MyLabel.subtitulo("Alumno"));
        panel.add(MyLabel.subtitulo("Legajo"));
        panel.add(MyLabel.subtitulo("Parcial"));
        panel.add(MyLabel.subtitulo("Final"));
        panel.add(MyLabel.subtitulo("Promoción"));

        return panel;
    }

    private JPanel crearFilaAlumno(InscripcionMateria inscripcion) {
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 5));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        String legajo = inscripcion.getAlumno().getLegajo();

        // Datos del alumno
        panel.add(MyLabel.texto(inscripcion.getAlumno().getNombre()));
        panel.add(MyLabel.texto(legajo));

        // Checkboxes
        JCheckBox cbParcial = new JCheckBox();
        cbParcial.setSelected(inscripcion.aproboParcial());
        cbParcial.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        checkboxesParcial.put(legajo, cbParcial);

        JCheckBox cbFinal = new JCheckBox();
        cbFinal.setSelected(inscripcion.aproboFinal());
        cbFinal.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        checkboxesFinal.put(legajo, cbFinal);

        JCheckBox cbPromocion = new JCheckBox();
        cbPromocion.setSelected(inscripcion.promociono());
        cbPromocion.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        checkboxesPromocion.put(legajo, cbPromocion);

        panel.add(cbParcial);
        panel.add(cbFinal);
        panel.add(cbPromocion);

        return panel;
    }

    private void guardarCambios() {
        boolean algunCambio = false;

        for (InscripcionMateria inscripcion : inscripciones) {
            String legajo = inscripcion.getAlumno().getLegajo();

            boolean parcial = checkboxesParcial.get(legajo).isSelected();
            boolean final_ = checkboxesFinal.get(legajo).isSelected();
            boolean promocion = checkboxesPromocion.get(legajo).isSelected();

            boolean exito = materiaController.actualizarInscripcion(legajo, materia.getCodigo(),
                    parcial, final_, promocion);
            if (exito) {
                algunCambio = true;
            }
        }

        if (algunCambio) {
            JOptionPane.showMessageDialog(this, "Calificaciones actualizadas exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            onGuardar.actionPerformed(null);
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar calificaciones",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}