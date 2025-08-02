package view.panels;

import model.Alumno;
import view.components.MyLayout;
import view.components.MyLayout.AlumnoVisual;
import controller.AlumnoController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AlumnosPanel extends JPanel {

    private AlumnoController alumnoController;
    private AlumnoFormPanel formularioAlumno;


    public AlumnosPanel() {
        this.alumnoController = new AlumnoController();
        configurarPanel();
    }

    private void configurarPanel() {
        JPanel seccionAlumnos = MyLayout.crearSeccion(
                "Alumnos",
                cargarListaAlumnos(),
                e -> crearAlumno(),
                e -> editarAlumno(e.getActionCommand()),
                e -> eliminarAlumno(e.getActionCommand()),
                e -> {}
        );

        setLayout(new BorderLayout());
        add(seccionAlumnos, BorderLayout.CENTER);
    }

    private List<AlumnoVisual> cargarListaAlumnos() {
        return alumnoController.obtenerTodos().stream()
                .map(a -> new AlumnoVisual(a.getNombre(), a.getLegajo()))
                .collect(Collectors.toList());
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

