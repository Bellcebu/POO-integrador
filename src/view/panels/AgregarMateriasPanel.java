package view.panels;

import model.*;
import view.components.*;
import view.config.ThemeConfig;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

public class AgregarMateriasPanel extends JPanel {

    private JPanel listaPanelMaterias;
    private Map<String, JCheckBox> checkboxesMaterias;
    private MyButton btnAgregar;
    private MyButton btnCancelar;
    private Carrera carrera;
    private ActionListener onAgregar;
    private ActionListener onCancelar;

    public AgregarMateriasPanel(Carrera carrera, ActionListener onAgregar, ActionListener onCancelar) {
        this.carrera = carrera;
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

        checkboxesMaterias.clear();
        listaPanelMaterias.removeAll();

        for (Materia materia : todasLasMaterias) {
            if (!materiasCarrera.contains(materia)) {
                JPanel itemPanel = crearItemMateria(materia);
                listaPanelMaterias.add(itemPanel);
                listaPanelMaterias.add(Box.createVerticalStrut(5));
            }
        }

        if (checkboxesMaterias.isEmpty()) {
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
        itemPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JCheckBox checkbox = new JCheckBox();
        checkbox.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        checkbox.setForeground(ThemeConfig.COLOR_TEXTO);
        checkboxesMaterias.put(materia.getCodigo(), checkbox);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        String tipoTexto = materia.esObligatoria() ? "Obligatoria" : "Optativa";
        String correlativasTexto = materia.getCorrelativas().isEmpty() ? "Sin correlativas" :
                materia.getCorrelativas().size() + " correlativas";

        MyLabel nombreLabel = MyLabel.textoFormulario(materia.getNombre() + " (" + materia.getCodigo() + ")");
        MyLabel detallesLabel = MyLabel.info("Cuatrimestre: " + materia.getCuatrimestre() +
                " | " + tipoTexto + " | " + correlativasTexto);

        infoPanel.add(nombreLabel);
        infoPanel.add(detallesLabel);

        itemPanel.add(checkbox, BorderLayout.WEST);
        itemPanel.add(infoPanel, BorderLayout.CENTER);

        return itemPanel;
    }

    public List<String> getMateriasSeleccionadas() {
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
