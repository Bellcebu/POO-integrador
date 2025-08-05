package view.panels;

import model.*;
import view.components.*;
import controller.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AlumnosPanel extends JPanel {

    private AlumnoController alumnoController;
    private AlumnoFormPanel formularioAlumno;
    private InscribirMateriasPanel inscribirMateriasPanel;
    private VerMateriasPanel verMateriasPanel;
    private InfoAlumnoPanel infoAlumnoPanel;
    private boolean ordenAZ = false;
    private String textoBusqueda = "";

    public AlumnosPanel() {
        this.alumnoController = new AlumnoController();
        configurarPanel();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout());

        JPanel seccionAlumnos = MyLayout.crearSeccion(
                "Alumnos",
                obtenerAlumnosFiltrados(),
                e -> crearAlumno(),
                e -> inscribirAMaterias(e.getActionCommand()),
                e -> verMaterias(e.getActionCommand()),
                e -> mostrarInfo(e.getActionCommand()),
                e -> { ordenAZ = !ordenAZ; actualizarLista(); },
                ordenAZ ? "A→Z" : "Z→A",
                e -> {
                    textoBusqueda = e.getActionCommand();
                    actualizarLista();
                },
                "Buscar por nombre o legajo",
                "Inscribir a Materias",
                "Ver Materias",
                "Informacion"
        );

        add(seccionAlumnos, BorderLayout.CENTER);
    }

    private List<MyLayout.AlumnoVisual> obtenerAlumnosFiltrados() {
        List<Alumno> alumnos = alumnoController.buscarAlumnos(textoBusqueda);

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
        configurarPanel();
        revalidate();
        repaint();
    }

    private void crearAlumno() {
        formularioAlumno = new AlumnoFormPanel(
                e -> {
                    String nombre = formularioAlumno.getNombre();
                    String legajo = formularioAlumno.getLegajo();

                    if (nombre.isEmpty() || legajo.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean exito = alumnoController.agregarAccion(nombre, legajo);
                    if (exito) {
                        volverAListaPrincipal();
                    } else {
                        JOptionPane.showMessageDialog(this, "El legajo ya existe",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                },
                e -> volverAListaPrincipal()
        );
        mostrarFormulario();
    }

    private void inscribirAMaterias(String legajo) {
        Alumno alumno = alumnoController.buscarPorLegajo(legajo);
        if (alumno != null) {
            inscribirMateriasPanel = new InscribirMateriasPanel(alumno, alumnoController,
                    e -> {
                        if (!inscribirMateriasPanel.haySeleccion()) {
                            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos una materia",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        List<String> materiasSeleccionadas = inscribirMateriasPanel.getMateriasSeleccionadas();
                        boolean exito = alumnoController.inscribirAlumnoAMaterias(legajo, materiasSeleccionadas);

                        if (exito) {
                            JOptionPane.showMessageDialog(this, "Materias inscriptas exitosamente",
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            volverAListaPrincipal();
                        } else {
                            JOptionPane.showMessageDialog(this, "Error al inscribir materias",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    },
                    e -> volverAListaPrincipal()
            );
            mostrarFormularioInscribirMaterias();
        }
    }

    private void verMaterias(String legajo) {
        Alumno alumno = alumnoController.buscarPorLegajo(legajo);
        if (alumno != null) {
            verMateriasPanel = new VerMateriasPanel(alumno, alumnoController, e -> volverAListaPrincipal());
            mostrarPanelVerMaterias();
        }
    }

    private void mostrarInfo(String legajo) {
        Alumno alumno = alumnoController.buscarPorLegajo(legajo);
        if (alumno != null) {
            infoAlumnoPanel = new InfoAlumnoPanel(alumno, alumnoController, e -> volverAListaPrincipal());
            mostrarPanelInfo();
        }
    }

    private void mostrarFormulario() {
        removeAll();
        add(formularioAlumno, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrarFormularioInscribirMaterias() {
        removeAll();
        add(inscribirMateriasPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrarPanelVerMaterias() {
        removeAll();
        add(verMateriasPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrarPanelInfo() {
        removeAll();
        add(infoAlumnoPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void volverAListaPrincipal() {
        removeAll();
        configurarPanel();
        revalidate();
        repaint();
    }
}
