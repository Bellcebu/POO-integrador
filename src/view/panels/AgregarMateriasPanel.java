package view.panels;

import model.*;
import view.components.*;
import view.config.ThemeConfig;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AgregarMateriasPanel extends JPanel {

    private JPanel listaPanelMaterias;
    private Map<String, Boolean> materiasSeleccionadas;
    private Map<String, JPanel> panelesMaterias;
    private MyButton btnAgregar;
    private MyButton btnCancelar;
    private Carrera carrera;
    private ActionListener onAgregar;
    private ActionListener onCancelar;

    public AgregarMateriasPanel(Carrera carrera, ActionListener onAgregar, ActionListener onCancelar) {
        this.carrera = carrera;
        this.onAgregar = onAgregar;
        this.onCancelar = onCancelar;
        this.materiasSeleccionadas = new HashMap<>();
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
        headerPanel.add(MyLabel.textoFormulario("Agregar Materias a: " + carrera.getNombre()));
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        centerPanel.add(MyLabel.subtitulo("Selecciona las materias que deseas agregar:"), BorderLayout.NORTH);

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
        List<Materia> materiasCarrera = carrera.getMaterias();

        materiasSeleccionadas.clear();
        panelesMaterias.clear();
        listaPanelMaterias.removeAll();

        for (Materia materia : todasLasMaterias) {
            if (!materiasCarrera.contains(materia)) {
                JPanel itemPanel = crearItemMateria(materia);
                listaPanelMaterias.add(itemPanel);
                listaPanelMaterias.add(Box.createVerticalStrut(5));

                materiasSeleccionadas.put(materia.getCodigo(), false);
                panelesMaterias.put(materia.getCodigo(), itemPanel);
            }
        }

        if (materiasSeleccionadas.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout());
            emptyPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
            emptyPanel.add(MyLabel.info("No hay materias disponibles para agregar"));
            listaPanelMaterias.add(emptyPanel);
        }

        listaPanelMaterias.revalidate();
        listaPanelMaterias.repaint();
    }

    private JPanel crearItemMateria(Materia materia) {
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

        String tipoTexto = materia.esObligatoria() ? "Obligatoria" : "Optativa";
        String correlativasTexto = materia.getCorrelativas().isEmpty() ? "Sin correlativas" :
                materia.getCorrelativas().size() + " correlativas";

        MyLabel nombreLabel = MyLabel.textoFormulario(materia.getNombre() + " (" + materia.getCodigo() + ")");
        MyLabel detallesLabel = MyLabel.info("Cuatrimestre: " + materia.getCuatrimestre() +
                " | " + tipoTexto + " | " + correlativasTexto);

        infoPanel.add(nombreLabel);
        infoPanel.add(detallesLabel);

        itemPanel.add(infoPanel, BorderLayout.CENTER);

        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSeleccion(materia.getCodigo());
            }
        });

        return itemPanel;
    }

    private void toggleSeleccion(String codigoMateria) {
        boolean estaSeleccionado = materiasSeleccionadas.get(codigoMateria);
        boolean nuevaSeleccion = !estaSeleccionado;

        materiasSeleccionadas.put(codigoMateria, nuevaSeleccion);

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

    public List<String> getMateriasSeleccionadas() {
        List<String> seleccionadas = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : materiasSeleccionadas.entrySet()) {
            if (entry.getValue()) {
                seleccionadas.add(entry.getKey());
            }
        }
        return seleccionadas;
    }

    public boolean haySeleccion() {
        return materiasSeleccionadas.values().contains(true);
    }
}