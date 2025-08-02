package view.panels;

import model.Alumno;
import model.Facultad;
import view.components.MyButton;
import view.components.MyLabel;
import view.components.MyLayout;
import view.components.MyLayout.AlumnoVisual;
import controller.AlumnoController;
import view.components.MyTextField;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AlumnosPanel extends JPanel {

    private AlumnoController alumnoController;
    private AlumnoFormPanel formularioAlumno;
    private MyTextField txtBuscar;
    private boolean ordenAZ = true;


    public AlumnosPanel() {
        this.alumnoController = new AlumnoController();
        configurarPanel();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panelSuperior.add(MyLabel.texto("Buscar:"));

        txtBuscar = new MyTextField(20);
        txtBuscar.setToolTipText("Buscar por nombre o legajo");
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarLista(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarLista(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarLista(); }
        });
        panelSuperior.add(txtBuscar);

        JPanel seccionAlumnos = MyLayout.crearSeccion(
                "Alumnos",
                obtenerAlumnosFiltrados(),
                e -> crearAlumno(),
                e -> editarAlumno(e.getActionCommand()),
                e -> eliminarAlumno(e.getActionCommand()),
                e -> {},
                e -> {ordenAZ = !ordenAZ;actualizarLista();}, ordenAZ ? "A→Z" : "Z→A"
        );

        add(panelSuperior, BorderLayout.NORTH);
        add(seccionAlumnos, BorderLayout.CENTER);
    }

    private List<MyLayout.AlumnoVisual> obtenerAlumnosFiltrados() {
        String textoBusqueda = txtBuscar != null ? txtBuscar.getText() : "";

        List<Alumno> alumnos = Facultad.getInstance().buscarAlumnos(textoBusqueda);

        if (ordenAZ) {
            alumnos.sort((a1, a2) -> a1.getNombre().compareToIgnoreCase(a2.getNombre()));
        } else {
            alumnos.sort((a1, a2) -> a2.getNombre().compareToIgnoreCase(a1.getNombre()));
        }

        return alumnos.stream()
                .map(a -> new MyLayout.AlumnoVisual(a.getNombre(), a.getLegajo()))
                .collect(Collectors.toList());
    }

    private void actualizarLista() {
        removeAll();
        configurarPanel(); // ← Esto recreará el botón con el texto correcto
        revalidate();
        repaint();
    }

    private void eliminarAlumno(String legajo) {
        Alumno alumno = alumnoController.buscarPorLegajo(legajo);
        formularioAlumno = new AlumnoFormPanel(alumno.getNombre(), alumno.getLegajo(),
                e -> {
                    alumnoController.eliminarAccion(legajo);
                    removeAll();
                    configurarPanel();
                    revalidate();
                    repaint();
                },
                e -> {
                    removeAll();
                    configurarPanel();
                    revalidate();
                    repaint();
                },
                true
        );
        removeAll();
        add(formularioAlumno, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void crearAlumno() {
        formularioAlumno = new AlumnoFormPanel(
                e -> {
                    String nombre = formularioAlumno.getNombre();
                    String legajo = formularioAlumno.getLegajo();
                    alumnoController.agregarAccion(nombre, legajo);
                    removeAll();
                    configurarPanel();
                    revalidate();
                    repaint();
                },
                e -> {
                    removeAll();
                    configurarPanel();
                    revalidate();
                    repaint();
                }
        );
        removeAll();
        add(formularioAlumno, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void editarAlumno(String legajoViejo) {
        Alumno alumno = alumnoController.buscarPorLegajo(legajoViejo);
        formularioAlumno = new AlumnoFormPanel(alumno.getNombre(), alumno.getLegajo(),
                e -> {
                    String nombre = formularioAlumno.getNombre();
                    String legajo = formularioAlumno.getLegajo();
                    alumnoController.editarAccion(legajoViejo, nombre, legajo);
                    removeAll();
                    configurarPanel();
                    revalidate();
                    repaint();
                },
                e -> {
                    removeAll();
                    configurarPanel();
                    revalidate();
                    repaint();
                }
        );
        removeAll();
        add(formularioAlumno, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}