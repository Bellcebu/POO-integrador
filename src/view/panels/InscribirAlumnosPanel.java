package view.panels;

import model.*;
import controller.*;
import java.util.List;
import view.components.*;
import view.config.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class InscribirAlumnosPanel extends JPanel {

    private JPanel listaPanelAlumnos;
    private Map<String, JCheckBox> checkboxesAlumnos;
    private MyButton btnInscribir;
    private MyButton btnCancelar;
    private Carrera carrera;
    private ActionListener onInscribir;
    private ActionListener onCancelar;

    public InscribirAlumnosPanel(Carrera carrera, ActionListener onInscribir, ActionListener onCancelar) {
        this.carrera = carrera;
        this.onInscribir = onInscribir;
        this.onCancelar = onCancelar;
        this.checkboxesAlumnos = new HashMap<>();

        configurarPanel();
        crearComponentes();
        configurarLayout();
        cargarAlumnos();
    }

    private void configurarPanel() {
        setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setPreferredSize(new Dimension(700, 500));
    }

    private void crearComponentes() {
        listaPanelAlumnos = new JPanel();
        listaPanelAlumnos.setLayout(new BoxLayout(listaPanelAlumnos, BoxLayout.Y_AXIS));
        listaPanelAlumnos.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        btnInscribir = MyButton.boton6("Inscribir", onInscribir);
        btnCancelar = MyButton.boton7("Cancelar", onCancelar);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.add(MyLabel.textoFormulario("Inscribir Alumnos a: " + carrera.getNombre()));
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        centerPanel.add(MyLabel.subtitulo("Selecciona los alumnos que deseas inscribir:"), BorderLayout.NORTH);

        MyScroll scrollAlumnos = MyScroll.crearVertical(listaPanelAlumnos);
        scrollAlumnos.setPreferredSize(new Dimension(650, 300));
        centerPanel.add(scrollAlumnos, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        bottomPanel.add(btnInscribir);
        bottomPanel.add(btnCancelar);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void cargarAlumnos() {
        List<Alumno> todosLosAlumnos = Facultad.getInstance().getAlumnos();
        List<Alumno> alumnosCarrera = carrera.getAlumnos();

        checkboxesAlumnos.clear();
        listaPanelAlumnos.removeAll();

        for (Alumno alumno : todosLosAlumnos) {
            if (!alumnosCarrera.contains(alumno)) {
                JPanel itemPanel = crearItemAlumno(alumno);
                listaPanelAlumnos.add(itemPanel);
                listaPanelAlumnos.add(Box.createVerticalStrut(5));
            }
        }

        if (checkboxesAlumnos.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout());
            emptyPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
            emptyPanel.add(MyLabel.info("No hay alumnos disponibles para inscribir"));
            listaPanelAlumnos.add(emptyPanel);
        }

        listaPanelAlumnos.revalidate();
        listaPanelAlumnos.repaint();
    }

    private JPanel crearItemAlumno(Alumno alumno) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JCheckBox checkbox = new JCheckBox();
        checkbox.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        checkbox.setForeground(ThemeConfig.COLOR_TEXTO);
        checkboxesAlumnos.put(alumno.getLegajo(), checkbox);

        JPanel infoPanel = new JPanel(new GridLayout(1, 1));
        infoPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        MyLabel nombreLabel = MyLabel.textoFormulario(alumno.getNombre() + " (" + alumno.getLegajo() + ")");
        infoPanel.add(nombreLabel);

        itemPanel.add(checkbox, BorderLayout.WEST);
        itemPanel.add(infoPanel, BorderLayout.CENTER);

        return itemPanel;
    }

    public List<String> getAlumnosSeleccionados() {
        List<String> seleccionados = new ArrayList<>();

        for (Map.Entry<String, JCheckBox> entry : checkboxesAlumnos.entrySet()) {
            if (entry.getValue().isSelected()) {
                seleccionados.add(entry.getKey());
            }
        }

        return seleccionados;
    }

    public boolean haySeleccion() {
        for (JCheckBox checkbox : checkboxesAlumnos.values()) {
            if (checkbox.isSelected()) {
                return true;
            }
        }
        return false;
    }
}
