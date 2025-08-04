package view.panels;

import model.*;
import controller.AlumnoController;
import view.components.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class InscribirMateriasPanel extends JPanel {

    private Alumno alumno;
    private AlumnoController alumnoController;
    private JComboBox<String> cmbCarreras;
    private JList<Materia> listaMaterias;
    private DefaultListModel<Materia> modeloLista;
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
        // ComboBox carreras
        cmbCarreras = new JComboBox<>();
        cmbCarreras.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbCarreras.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        cmbCarreras.setForeground(ThemeConfig.COLOR_TEXTO);

        // Lista materias
        modeloLista = new DefaultListModel<>();
        listaMaterias = new JList<>(modeloLista);
        listaMaterias.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaMaterias.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        listaMaterias.setForeground(ThemeConfig.COLOR_TEXTO);
        listaMaterias.setFont(new Font("Arial", Font.PLAIN, 12));

        // Renderer personalizado
        listaMaterias.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Materia) {
                    Materia materia = (Materia) value;
                    String tipo = materia.esObligatoria() ? "Obligatoria" : "Optativa";
                    setText(materia.getNombre() + " (" + materia.getCodigo() + ") - " + tipo + " - Cuatr: " + materia.getCuatrimestre());
                }
                return this;
            }
        });

        // Botones
        btnCargar = MyButton.boton5("Cargar Materias", e -> cargarMaterias());
        btnInscribir = MyButton.boton6("Inscribir Seleccionadas", onInscribir);
        btnCancelar = MyButton.boton7("Cancelar", onCancelar);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout());

        // Título
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.add(MyLabel.titulo("Inscribir a Materias: " + alumno.getNombre()));
        add(headerPanel, BorderLayout.NORTH);

        // Panel central
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel filtro carrera
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        filtroPanel.add(MyLabel.texto("Seleccionar carrera:"));
        filtroPanel.add(cmbCarreras);
        filtroPanel.add(btnCargar);
        centerPanel.add(filtroPanel, BorderLayout.NORTH);

        // Panel materias
        JPanel materiasPanel = new JPanel(new BorderLayout());
        materiasPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        materiasPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        materiasPanel.add(MyLabel.subtitulo("Materias disponibles (Ctrl+Click para múltiples):"), BorderLayout.NORTH);

        MyScroll scrollMaterias = MyScroll.crearVertical(listaMaterias);
        scrollMaterias.setPreferredSize(new Dimension(650, 350));
        materiasPanel.add(scrollMaterias, BorderLayout.CENTER);

        centerPanel.add(materiasPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Botones
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
        modeloLista.clear();

        if (carrerasAlumno.isEmpty()) {
            return;
        }

        int selectedIndex = cmbCarreras.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < carrerasAlumno.size()) {
            Carrera carreraSeleccionada = carrerasAlumno.get(selectedIndex);
            List<Materia> materiasDisponibles = alumnoController.obtenerMateriasDisponibles(
                    alumno.getLegajo(), carreraSeleccionada.getCodigo());

            if (materiasDisponibles.isEmpty()) {
                // Mostrar mensaje en la lista
                JOptionPane.showMessageDialog(this,
                        "No hay materias disponibles para esta carrera.\nPuede que ya esté inscripto en todas o no cumpla correlativas.",
                        "Sin materias disponibles", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Materia materia : materiasDisponibles) {
                    modeloLista.addElement(materia);
                }
            }
        }
    }

    public List<String> getMateriasSeleccionadas() {
        List<Materia> seleccionadas = listaMaterias.getSelectedValuesList();
        List<String> codigos = new ArrayList<>();

        for (Materia materia : seleccionadas) {
            codigos.add(materia.getCodigo());
        }

        return codigos;
    }

    public boolean haySeleccion() {
        return !listaMaterias.getSelectedValuesList().isEmpty();
    }
}