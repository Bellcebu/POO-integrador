package view.panels;

import model.*;
import view.components.*;
import view.config.ThemeConfig;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AgregarCorrelativasPanel extends JPanel {

    private JPanel listaPanelMaterias;
    private Map<String, Boolean> correlativasSeleccionadas;
    private Map<String, JPanel> panelesMaterias;
    private MyButton btnAgregar;
    private MyButton btnCancelar;
    private Materia materia;
    private ActionListener onAgregar;
    private ActionListener onCancelar;

    public AgregarCorrelativasPanel(Materia materia, ActionListener onAgregar, ActionListener onCancelar) {
        this.materia = materia;
        this.onAgregar = onAgregar;
        this.onCancelar = onCancelar;
        this.correlativasSeleccionadas = new HashMap<>();
        this.panelesMaterias = new HashMap<>();

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

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.add(MyLabel.textoFormulario("Agregar Correlativas a: " + materia.getNombre()));
        add(headerPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        String correlativasActuales = materia.getCorrelativas().isEmpty() ? "Ninguna" :
                materia.getCorrelativas().size() + " correlativas";
        infoPanel.add(MyLabel.info("Correlativas actuales: " + correlativasActuales), BorderLayout.CENTER);
        add(infoPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        centerPanel.add(MyLabel.subtitulo("Selecciona las materias correlativas:"), BorderLayout.NORTH);

        MyScroll scrollMaterias = MyScroll.crearVertical(listaPanelMaterias);
        scrollMaterias.setPreferredSize(new Dimension(650, 300));
        centerPanel.add(scrollMaterias, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        bottomPanel.add(btnAgregar);
        bottomPanel.add(btnCancelar);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void cargarMaterias() {
        List<Materia> todasLasMaterias = Facultad.getInstance().getMaterias();

        correlativasSeleccionadas.clear();
        panelesMaterias.clear();
        listaPanelMaterias.removeAll();

        List<Materia> materiasDisponibles = new ArrayList<>();
        for (Materia m : todasLasMaterias) {
            if (!m.getCodigo().equals(materia.getCodigo()) &&
                    !materia.getCorrelativas().contains(m)) {
                materiasDisponibles.add(m);
            }
        }

        materiasDisponibles.sort((m1, m2) -> m1.getNombre().compareToIgnoreCase(m2.getNombre()));

        for (Materia m : materiasDisponibles) {
            JPanel itemPanel = crearItemMateria(m);
            listaPanelMaterias.add(itemPanel);
            listaPanelMaterias.add(Box.createVerticalStrut(5));

            correlativasSeleccionadas.put(m.getCodigo(), false);
            panelesMaterias.put(m.getCodigo(), itemPanel);
        }
    }

    private JPanel crearItemMateria(Materia m) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(ThemeConfig.COLOR_ITEM_NO_SELECCIONADO);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(ThemeConfig.COLOR_ITEM_NO_SELECCIONADO);

        String tipoTexto = m.esObligatoria() ? "Obligatoria" : "Optativa";
        String correlativasTexto = m.getCorrelativas().isEmpty() ? "Sin correlativas" :
                m.getCorrelativas().size() + " correlativas";

        MyLabel nombreLabel = MyLabel.textoFormulario(m.getNombre() + " (" + m.getCodigo() + ")");
        MyLabel detallesLabel = MyLabel.info("Cuatrimestre: " + m.getCuatrimestre() +
                " | " + tipoTexto + " | " + correlativasTexto);

        infoPanel.add(nombreLabel);
        infoPanel.add(detallesLabel);

        itemPanel.add(infoPanel, BorderLayout.CENTER);

        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSeleccion(m.getCodigo());
            }
        });

        return itemPanel;
    }

    private void toggleSeleccion(String codigoMateria) {
        boolean estaSeleccionado = correlativasSeleccionadas.get(codigoMateria);
        boolean nuevaSeleccion = !estaSeleccionado;

        correlativasSeleccionadas.put(codigoMateria, nuevaSeleccion);

        JPanel panel = panelesMaterias.get(codigoMateria);
        Color nuevoColor = nuevaSeleccion ? ThemeConfig.COLOR_ITEM_SELECCIONADO : ThemeConfig.COLOR_ITEM_NO_SELECCIONADO;

        panel.setBackground(nuevoColor);

        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(nuevoColor);
            }
        }

        panel.repaint();
    }

    public List<String> getCorrelativasSeleccionadas() {
        List<String> seleccionadas = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : correlativasSeleccionadas.entrySet()) {
            if (entry.getValue()) {
                seleccionadas.add(entry.getKey());
            }
        }
        return seleccionadas;
    }

    public boolean haySeleccion() {
        return correlativasSeleccionadas.values().contains(true);
    }
}