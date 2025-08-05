package view.panels;

import model.*;
import controller.*;
import view.components.*;
import view.config.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class InscribirMateriasPanel extends JPanel {

    private Alumno alumno;
    private AlumnoController alumnoController;
    private JComboBox<String> cmbCarreras;
    private JPanel listaPanelMaterias;
    private Map<String, JCheckBox> checkboxesMaterias;
    private MyButton btnCargar;
    private MyButton btnInscribir;
    private MyButton btnCancelar;
    private ActionListener onInscribir;
    private ActionListener onCancelar;
    private List<Carrera> carrerasAlumno;

    public InscribirMateriasPanel(Alumno alumno, AlumnoController alumnoController,
                                  ActionListener onInscribir, ActionListener onCancelar) {
        this.alumno = alumno;
        this.alumnoController = alumnoController;
        this.onInscribir = onInscribir;
        this.onCancelar = onCancelar;
        this.checkboxesMaterias = new HashMap<>();

        configurarPanel();
        crearComponentes();
        configurarLayout();
        cargarCarreras();
    }

    private void configurarPanel() {
        setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setPreferredSize(new Dimension(700, 600));
    }

    private void crearComponentes() {
        cmbCarreras = new JComboBox<>();
        cmbCarreras.setFont(new Font("Arial", Font.PLAIN, 18));
        cmbCarreras.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        cmbCarreras.setForeground(ThemeConfig.COLOR_TEXTO);

        listaPanelMaterias = new JPanel();
        listaPanelMaterias.setLayout(new BoxLayout(listaPanelMaterias, BoxLayout.Y_AXIS));
        listaPanelMaterias.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        btnCargar = MyButton.boton5("Cargar Materias", e -> cargarMaterias());
        btnInscribir = MyButton.boton6("Inscribir", onInscribir);
        btnCancelar = MyButton.boton7("Cancelar", onCancelar);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.add(MyLabel.textoFormulario("Inscribir a Materias: " + alumno.getNombre()));
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        filtroPanel.add(MyLabel.textoFormulario("Seleccionar carrera:"));
        filtroPanel.add(cmbCarreras);
        filtroPanel.add(btnCargar);
        centerPanel.add(filtroPanel, BorderLayout.NORTH);

        JPanel materiasPanel = new JPanel(new BorderLayout());
        materiasPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        materiasPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        materiasPanel.add(MyLabel.subtitulo("Materias disponibles:"), BorderLayout.NORTH);

        MyScroll scrollMaterias = MyScroll.crearVertical(listaPanelMaterias);
        scrollMaterias.setPreferredSize(new Dimension(650, 350));
        materiasPanel.add(scrollMaterias, BorderLayout.CENTER);

        centerPanel.add(materiasPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        bottomPanel.add(btnInscribir);
        bottomPanel.add(btnCancelar);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void cargarCarreras() {
        cmbCarreras.removeAllItems();
        carrerasAlumno = alumnoController.obtenerCarrerasDelAlumno(alumno.getLegajo());

        if (carrerasAlumno.isEmpty()) {
            cmbCarreras.addItem("El alumno no está inscripto en ninguna carrera");
            btnCargar.setEnabled(false);
        } else {
            for (Carrera carrera : carrerasAlumno) {
                cmbCarreras.addItem(carrera.getCodigo() + " - " + carrera.getNombre());
            }
        }
    }

    private void cargarMaterias() {
        checkboxesMaterias.clear();
        listaPanelMaterias.removeAll();

        if (carrerasAlumno.isEmpty()) {
            return;
        }

        int selectedIndex = cmbCarreras.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < carrerasAlumno.size()) {
            Carrera carreraSeleccionada = carrerasAlumno.get(selectedIndex);
            List<Materia> materiasDisponibles = alumnoController.obtenerMateriasDisponibles(
                    alumno.getLegajo(), carreraSeleccionada.getCodigo());

            if (materiasDisponibles.isEmpty()) {
                JPanel emptyPanel = new JPanel(new FlowLayout());
                emptyPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
                emptyPanel.add(MyLabel.info("No hay materias disponibles para esta carrera"));
                listaPanelMaterias.add(emptyPanel);
            } else {
                for (Materia materia : materiasDisponibles) {
                    JPanel itemPanel = crearItemMateria(materia);
                    listaPanelMaterias.add(itemPanel);
                    listaPanelMaterias.add(Box.createVerticalStrut(5));
                }
            }
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

        MyLabel nombreLabel = MyLabel.texto(materia.getNombre() + " (" + materia.getCodigo() + ")");
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
