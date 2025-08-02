package view.panels;

import model.Alumno;
import model.Facultad;
import view.components.MyLayout;
import view.components.MyLayout.AlumnoVisual;
import controller.AlumnoController;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AlumnosPanel extends JPanel {

    private AlumnoController alumnoController;
    private AlumnoFormPanel formularioAlumno;
    private boolean ordenAZ = false;
    private String textoBusqueda = ""; // Mantener el estado de búsqueda

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
                e -> editarAlumno(e.getActionCommand()),
                e -> eliminarAlumno(e.getActionCommand()),
                e -> {}, // Sin gestionar por ahora
                e -> { ordenAZ = !ordenAZ; actualizarLista(); },
                ordenAZ ? "A→Z" : "Z→A",
                e -> { // NUEVO: ActionListener para búsqueda
                    textoBusqueda = e.getActionCommand(); // El texto viene en getActionCommand()
                    actualizarLista();
                },
                "Buscar por nombre o legajo" // NUEVO: Placeholder
        );

        add(seccionAlumnos, BorderLayout.CENTER);
    }

    private List<MyLayout.AlumnoVisual> obtenerAlumnosFiltrados() {
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
        configurarPanel();
        revalidate();
        repaint();
    }

    private void eliminarAlumno(String legajo) {
        Alumno alumno = alumnoController.buscarPorLegajo(legajo);
        if (alumno != null) {
            formularioAlumno = new AlumnoFormPanel(alumno.getNombre(), alumno.getLegajo(),
                    e -> {
                        alumnoController.eliminarAccion(legajo);
                        volverAListaPrincipal();
                    },
                    e -> volverAListaPrincipal(),
                    true
            );
            mostrarFormulario();
        }
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

    private void editarAlumno(String legajoViejo) {
        Alumno alumno = alumnoController.buscarPorLegajo(legajoViejo);
        if (alumno != null) {
            formularioAlumno = new AlumnoFormPanel(alumno.getNombre(), alumno.getLegajo(),
                    e -> {
                        String nombre = formularioAlumno.getNombre();
                        String legajo = formularioAlumno.getLegajo();

                        if (nombre.isEmpty() || legajo.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        boolean exito = alumnoController.editarAccion(legajoViejo, nombre, legajo);
                        if (exito) {
                            volverAListaPrincipal();
                        } else {
                            JOptionPane.showMessageDialog(this, "Error al editar alumno",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    },
                    e -> volverAListaPrincipal()
            );
            mostrarFormulario();
        }
    }

    private void mostrarFormulario() {
        removeAll();
        add(formularioAlumno, BorderLayout.CENTER);
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