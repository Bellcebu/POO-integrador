package view.panels;

import model.*;
import view.components.*;
import controller.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CarrerasPanel extends JPanel {

    private CarreraController carreraController;
    private CarreraFormPanel formularioCarrera;
    private AgregarMateriasPanel agregarMateriasPanel;
    private InscribirAlumnosPanel inscribirAlumnosPanel;
    private InfoCarreraPanel infoCarreraPanel;
    private boolean ordenAZ = false;
    private String textoBusqueda = "";

    public CarrerasPanel() {
        this.carreraController = new CarreraController();
        configurarPanel();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout());

        JPanel seccionCarreras = MyLayout.crearSeccion(
                "Carreras",
                cargarListaCarreras(),
                e -> crearCarrera(),
                e -> agregarMaterias(e.getActionCommand()),
                e -> inscribirAlumnos(e.getActionCommand()),
                e -> mostrarInfo(e.getActionCommand()),
                e -> { ordenAZ = !ordenAZ; actualizarLista(); },
                ordenAZ ? "A→Z" : "Z→A",
                e -> {
                    textoBusqueda = e.getActionCommand();
                    actualizarLista();
                },
                "Buscar por nombre o código",
                "Agregar Materias",
                "Inscribir Alumnos",
                "Informacion"
        );

        add(seccionCarreras, BorderLayout.CENTER);
    }

    private void crearCarrera() {
        formularioCarrera = new CarreraFormPanel(
                e -> {
                    String codigo = formularioCarrera.getCodigo();
                    String nombre = formularioCarrera.getNombre();
                    int cantidadOptativas = formularioCarrera.getCantidadOptativas();
                    String tipoPlan = formularioCarrera.getTipoPlan();

                    if (codigo.isEmpty() || nombre.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Código y Nombre son obligatorios",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (cantidadOptativas < 0) {
                        JOptionPane.showMessageDialog(this, "Cantidad de optativas debe ser un número válido",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean exito = carreraController.agregarAccion(codigo, nombre, cantidadOptativas, tipoPlan);
                    if (exito) {
                        JOptionPane.showMessageDialog(this, "Carrera creada exitosamente",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        volverAListaPrincipal();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error: El código de carrera ya existe",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                },
                e -> volverAListaPrincipal()
        );
        mostrarFormulario();
    }

    private void agregarMaterias(String codigoCarrera) {
        Carrera carrera = carreraController.buscarCarreraPorCodigo(codigoCarrera);
        if (carrera != null) {
            agregarMateriasPanel = new AgregarMateriasPanel(carrera,
                    e -> {
                        if (!agregarMateriasPanel.haySeleccion()) {
                            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos una materia",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        List<String> materiasSeleccionadas = agregarMateriasPanel.getMateriasSeleccionadas();
                        boolean exito = carreraController.agregarMateriasACarrera(codigoCarrera, materiasSeleccionadas);

                        if (exito) {
                            JOptionPane.showMessageDialog(this, "Materias agregadas exitosamente",
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            volverAListaPrincipal();
                        } else {
                            JOptionPane.showMessageDialog(this, "Error al agregar materias",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    },
                    e -> volverAListaPrincipal()
            );
            mostrarFormularioAgregarMaterias();
        }
    }

    private void inscribirAlumnos(String codigoCarrera) {
        Carrera carrera = carreraController.buscarCarreraPorCodigo(codigoCarrera);
        if (carrera != null) {
            inscribirAlumnosPanel = new InscribirAlumnosPanel(carrera,
                    e -> {
                        if (!inscribirAlumnosPanel.haySeleccion()) {
                            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un alumno",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        List<String> alumnosSeleccionados = inscribirAlumnosPanel.getAlumnosSeleccionados();
                        boolean exito = carreraController.inscribirAlumnosACarrera(codigoCarrera, alumnosSeleccionados);

                        if (exito) {
                            JOptionPane.showMessageDialog(this, "Alumnos inscriptos exitosamente",
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            volverAListaPrincipal();
                        } else {
                            JOptionPane.showMessageDialog(this, "Error al inscribir alumnos",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    },
                    e -> volverAListaPrincipal()
            );
            mostrarFormularioInscribirAlumnos();
        }
    }

    private void mostrarInfo(String codigoCarrera) {
        Carrera carrera = carreraController.buscarCarreraPorCodigo(codigoCarrera);
        if (carrera != null) {
            infoCarreraPanel = new InfoCarreraPanel(carrera, e -> volverAListaPrincipal());
            mostrarPanelInfo();
        }
    }

    private void actualizarLista() {
        removeAll();
        configurarPanel();
        revalidate();
        repaint();
    }

    private List<MyLayout.AlumnoVisual> cargarListaCarreras() {
        List<Carrera> carreras = carreraController.buscarCarreras(textoBusqueda);

        if (ordenAZ) {
            carreras.sort((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()));
        } else {
            carreras.sort((c1, c2) -> c2.getNombre().compareToIgnoreCase(c1.getNombre()));
        }

        return carreras.stream()
                .map(c -> new MyLayout.AlumnoVisual(c.getNombre(), c.getCodigo()))
                .collect(Collectors.toList());
    }

    private void mostrarFormulario() {
        removeAll();
        add(formularioCarrera, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrarFormularioAgregarMaterias() {
        removeAll();
        add(agregarMateriasPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrarFormularioInscribirAlumnos() {
        removeAll();
        add(inscribirAlumnosPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrarPanelInfo() {
        removeAll();
        add(infoCarreraPanel, BorderLayout.CENTER);
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
