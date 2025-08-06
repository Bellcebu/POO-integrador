package view.panels;

import model.*;
import view.components.*;
import view.config.ThemeConfig;
import controller.*;
import view.components.MyDialog;

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
    private Map<String, MyCheckBox> checkboxesParcial;
    private Map<String, MyCheckBox> checkboxesFinal;
    private Map<String, MyCheckBox> checkboxesPromocion;
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
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.add(MyLabel.titulo("Calificar: " + materia.getNombre()));
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (inscripciones.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            emptyPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
            emptyPanel.add(MyLabel.textoFormulario("No hay alumnos inscriptos en esta materia"));
            mainPanel.add(emptyPanel);
        } else {
            JPanel headerTablePanel = crearHeaderTabla();
            mainPanel.add(headerTablePanel);
            mainPanel.add(Box.createVerticalStrut(10));

            for (InscripcionMateria inscripcion : inscripciones) {
                JPanel filaPanel = crearFilaAlumno(inscripcion);
                mainPanel.add(filaPanel);
                mainPanel.add(Box.createVerticalStrut(5));
            }
        }

        mainPanel.add(Box.createVerticalGlue());

        MyScroll scrollPanel = MyScroll.crearVertical(mainPanel);
        add(scrollPanel, BorderLayout.CENTER);

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

        panel.setPreferredSize(new Dimension(800, 45));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        panel.setMinimumSize(new Dimension(800, 45));

        panel.add(MyLabel.textoFormulario("Alumno"));
        panel.add(MyLabel.textoFormulario("Legajo"));
        panel.add(MyLabel.textoFormulario("Parcial"));
        panel.add(MyLabel.textoFormulario("Final"));
        panel.add(MyLabel.textoFormulario("Promoción"));

        return panel;
    }

    private JPanel crearFilaAlumno(InscripcionMateria inscripcion) {
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 5));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panel.setPreferredSize(new Dimension(800, 50));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panel.setMinimumSize(new Dimension(800, 50));

        String legajo = inscripcion.getAlumno().getLegajo();

        panel.add(MyLabel.textoFormulario(inscripcion.getAlumno().getNombre()));
        panel.add(MyLabel.textoFormulario(legajo));

        MyCheckBox cbParcial = new MyCheckBox(inscripcion.aproboParcial());
        checkboxesParcial.put(legajo, cbParcial);

        MyCheckBox cbFinal = new MyCheckBox(inscripcion.aproboFinal());
        checkboxesFinal.put(legajo, cbFinal);

        MyCheckBox cbPromocion = new MyCheckBox(inscripcion.promociono());
        checkboxesPromocion.put(legajo, cbPromocion);

        JPanel panelParcial = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelParcial.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panelParcial.add(cbParcial);

        JPanel panelFinal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFinal.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panelFinal.add(cbFinal);

        JPanel panelPromocion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPromocion.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panelPromocion.add(cbPromocion);

        panel.add(panelParcial);
        panel.add(panelFinal);
        panel.add(panelPromocion);

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
            MyDialog.showSuccess(this, "Calificaciones actualizadas exitosamente");
            onGuardar.actionPerformed(null);
        } else {
            MyDialog.showError(this, "Error al actualizar calificaciones", "Error");
        }
    }
}