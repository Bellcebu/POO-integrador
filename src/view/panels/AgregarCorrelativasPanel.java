package view.panels;

import model.Materia;
import model.Facultad;
import view.components.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AgregarCorrelativasPanel extends JPanel {

    private JPanel listaPanelMaterias;
    private Map<String, JCheckBox> checkboxesMaterias; // Mapa código -> checkbox
    private MyButton btnAgregar;
    private MyButton btnCancelar;
    private Materia materia;
    private ActionListener onAgregar;
    private ActionListener onCancelar;

    public AgregarCorrelativasPanel(Materia materia, ActionListener onAgregar, ActionListener onCancelar) {
        this.materia = materia;
        this.onAgregar = onAgregar;
        this.onCancelar = onCancelar;
        this.checkboxesMaterias = new HashMap<>();

        configurarPanel();
        crearComponentes();
        configurarLayout();
        cargarMaterias();
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
        listaPanelMaterias = new JPanel();
        listaPanelMaterias.setLayout(new BoxLayout(listaPanelMaterias, BoxLayout.Y_AXIS));
        listaPanelMaterias.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        btnAgregar = MyButton.boton6("Agregar", onAgregar);
        btnCancelar = MyButton.boton7("Cancelar", onCancelar);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout());

        // Título
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.add(MyLabel.titulo("Agregar Correlativas a: " + materia.getNombre()));
        add(headerPanel, BorderLayout.NORTH);

        // Información actual
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        String correlativasActuales = materia.getCorrelativas().isEmpty() ? "Ninguna" :
                materia.getCorrelativas().size() + " correlativas";
        infoPanel.add(MyLabel.info("Correlativas actuales: " + correlativasActuales), BorderLayout.CENTER);
        add(infoPanel, BorderLayout.NORTH);

        // Panel central con scroll
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        centerPanel.add(MyLabel.subtitulo("Selecciona las materias correlativas:"), BorderLayout.NORTH);

        MyScroll scrollMaterias = MyScroll.crearVertical(listaPanelMaterias);
        scrollMaterias.setPreferredSize(new Dimension(650, 300));
        centerPanel.add(scrollMaterias, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Botones
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        bottomPanel.add(btnAgregar);
        bottomPanel.add(btnCancelar);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void cargarMaterias() {
        List<Materia> todasLasMaterias = Facultad.getInstance().getMaterias();

        checkboxesMaterias.clear();
        listaPanelMaterias.removeAll();

        for (Materia m : todasLasMaterias) {
            // No mostrar la materia actual ni sus correlativas existentes
            if (!m.getCodigo().equals(materia.getCodigo()) &&
                    !materia.getCorrelativas().contains(m)) {
                JPanel itemPanel = crearItemMateria(m);
                listaPanelMaterias.add(itemPanel);
                listaPanelMaterias.add(Box.createVerticalStrut(5));
            }
        }

        // Si no hay materias disponibles
        if (checkboxesMaterias.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout());
            emptyPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
            emptyPanel.add(MyLabel.info("No hay materias disponibles para agregar como correlativas"));
            listaPanelMaterias.add(emptyPanel);
        }

        listaPanelMaterias.revalidate();
        listaPanelMaterias.repaint();
    }

    private JPanel crearItemMateria(Materia m) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Checkbox
        JCheckBox checkbox = new JCheckBox();
        checkbox.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        checkbox.setForeground(ThemeConfig.COLOR_TEXTO);
        checkboxesMaterias.put(m.getCodigo(), checkbox);

        // Información de la materia
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        String tipoTexto = m.esObligatoria() ? "Obligatoria" : "Optativa";
        String correlativasTexto = m.getCorrelativas().isEmpty() ? "Sin correlativas" :
                m.getCorrelativas().size() + " correlativas";

        MyLabel nombreLabel = MyLabel.texto(m.getNombre() + " (" + m.getCodigo() + ")");
        MyLabel detallesLabel = MyLabel.info("Cuatrimestre: " + m.getCuatrimestre() +
                " | " + tipoTexto + " | " + correlativasTexto);

        infoPanel.add(nombreLabel);
        infoPanel.add(detallesLabel);

        itemPanel.add(checkbox, BorderLayout.WEST);
        itemPanel.add(infoPanel, BorderLayout.CENTER);

        return itemPanel;
    }

    public List<String> getCorrelativasSeleccionadas() {
        List<String> seleccionadas = new ArrayList<>();

        for (Map.Entry<String, JCheckBox> entry : checkboxesMaterias.entrySet()) {
            if (entry.getValue().isSelected()) {
                seleccionadas.add(entry.getKey());
            }
        }

        return seleccionadas;
    }

    public boolean haySeleccion() {
        for (JCheckBox checkbox : checkboxesMaterias.values()) {
            if (checkbox.isSelected()) {
                return true;
            }
        }
        return false;
    }
}