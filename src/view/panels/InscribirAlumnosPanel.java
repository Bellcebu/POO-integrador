package view.panels;

import model.Alumno;
import model.Carrera;
import model.Facultad;
import view.components.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class InscribirAlumnosPanel extends JPanel {

    private JList<Alumno> listaAlumnos;
    private DefaultListModel<Alumno> modeloLista;
    private MyButton btnInscribir;
    private MyButton btnCancelar;
    private Carrera carrera;
    private ActionListener onInscribir;
    private ActionListener onCancelar;

    public InscribirAlumnosPanel(Carrera carrera, ActionListener onInscribir, ActionListener onCancelar) {
        this.carrera = carrera;
        this.onInscribir = onInscribir;
        this.onCancelar = onCancelar;

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
        setPreferredSize(new Dimension(600, 500));
    }

    private void crearComponentes() {
        modeloLista = new DefaultListModel<>();
        listaAlumnos = new JList<>(modeloLista);
        listaAlumnos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaAlumnos.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        listaAlumnos.setForeground(ThemeConfig.COLOR_TEXTO);
        listaAlumnos.setFont(new Font("Arial", Font.PLAIN, 12));

        // Renderer personalizado para mostrar nombre y legajo
        listaAlumnos.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Alumno) {
                    Alumno alumno = (Alumno) value;
                    setText(alumno.getNombre() + " (" + alumno.getLegajo() + ")");
                }
                return this;
            }
        });

        btnInscribir = MyButton.boton6("Inscribir", onInscribir);
        btnCancelar = MyButton.boton7("Cancelar", onCancelar);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout());

        // Título
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.add(MyLabel.titulo("Inscribir Alumnos a: " + carrera.getNombre()));
        add(headerPanel, BorderLayout.NORTH);

        // Lista de alumnos
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        centerPanel.add(MyLabel.subtitulo("Alumnos disponibles (Ctrl+Click para seleccionar múltiples):"), BorderLayout.NORTH);

        MyScroll scrollAlumnos = MyScroll.crearVertical(listaAlumnos);
        scrollAlumnos.setPreferredSize(new Dimension(550, 300));
        centerPanel.add(scrollAlumnos, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Botones
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        bottomPanel.add(btnInscribir);
        bottomPanel.add(btnCancelar);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void cargarAlumnos() {
        List<Alumno> todosLosAlumnos = Facultad.getInstance().getAlumnos();
        List<Alumno> alumnosCarrera = carrera.getAlumnos();

        // Solo mostrar alumnos que NO están ya inscriptos en la carrera
        for (Alumno alumno : todosLosAlumnos) {
            if (!alumnosCarrera.contains(alumno)) {
                modeloLista.addElement(alumno);
            }
        }
    }

    public List<String> getAlumnosSeleccionados() {
        List<Alumno> seleccionados = listaAlumnos.getSelectedValuesList();
        List<String> legajos = new ArrayList<>();

        for (Alumno alumno : seleccionados) {
            legajos.add(alumno.getLegajo());
        }

        return legajos;
    }

    public boolean haySeleccion() {
        return !listaAlumnos.getSelectedValuesList().isEmpty();
    }
}