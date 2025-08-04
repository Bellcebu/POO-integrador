package view.panels;

import model.Carrera;
import model.Materia;
import model.Alumno;
import view.components.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InfoCarreraPanel extends JPanel {

    private Carrera carrera;
    private MyButton btnVolver;
    private ActionListener onVolver;

    public InfoCarreraPanel(Carrera carrera, ActionListener onVolver) {
        this.carrera = carrera;
        this.onVolver = onVolver;

        configurarPanel();
        crearComponentes();
    }

    private void configurarPanel() {
        setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setLayout(new BorderLayout());
    }

    private void crearComponentes() {
        // Panel principal con scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        // Título
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.add(MyLabel.titulo("Información de la Carrera"));
        mainPanel.add(headerPanel);

        mainPanel.add(Box.createVerticalStrut(20));

        // Información básica
        JPanel infoBasicaPanel = crearSeccionInfo("Datos Básicos", crearInfoBasica());
        mainPanel.add(infoBasicaPanel);

        mainPanel.add(Box.createVerticalStrut(15));

        // Materias
        JPanel materiasPanel = crearSeccionInfo("Materias (" + carrera.getMaterias().size() + ")", crearInfoMaterias());
        mainPanel.add(materiasPanel);

        mainPanel.add(Box.createVerticalStrut(15));

        // Alumnos
        JPanel alumnosPanel = crearSeccionInfo("Alumnos Inscriptos (" + carrera.getAlumnos().size() + ")", crearInfoAlumnos());
        mainPanel.add(alumnosPanel);

        // Scroll para todo el contenido
        MyScroll scrollPanel = MyScroll.crearVertical(mainPanel);
        add(scrollPanel, BorderLayout.CENTER);

        // Botón volver
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        btnVolver = MyButton.boton7("Volver", onVolver);
        bottomPanel.add(btnVolver);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel crearSeccionInfo(String titulo, JPanel contenido) {
        JPanel seccion = new JPanel(new BorderLayout());
        seccion.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        seccion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Título de la sección
        JPanel headerSeccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerSeccion.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerSeccion.add(MyLabel.subtitulo(titulo));
        seccion.add(headerSeccion, BorderLayout.NORTH);

        // Contenido
        seccion.add(contenido, BorderLayout.CENTER);

        return seccion;
    }

    private JPanel crearInfoBasica() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        panel.add(MyLabel.texto("Código: " + carrera.getCodigo()));
        panel.add(MyLabel.texto("Nombre: " + carrera.getNombre()));
        panel.add(MyLabel.texto("Optativas necesarias: " + carrera.getCantidadOptativasNecesarias()));
        panel.add(MyLabel.texto("Plan de estudio: " + carrera.getPlanEstudio().getClass().getSimpleName()));

        return panel;
    }

    private JPanel crearInfoMaterias() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        if (carrera.getMaterias().isEmpty()) {
            panel.add(MyLabel.info("No hay materias asignadas"));
        } else {
            for (Materia materia : carrera.getMaterias()) {
                JPanel materiaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                materiaPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
                materiaPanel.add(MyLabel.texto("• " + materia.getNombre() + " (" + materia.getCodigo() + ")"));
                panel.add(materiaPanel);
            }
        }

        return panel;
    }

    private JPanel crearInfoAlumnos() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        if (carrera.getAlumnos().isEmpty()) {
            panel.add(MyLabel.info("No hay alumnos inscriptos"));
        } else {
            for (Alumno alumno : carrera.getAlumnos()) {
                JPanel alumnoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                alumnoPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
                alumnoPanel.add(MyLabel.texto("• " + alumno.getNombre() + " (" + alumno.getLegajo() + ")"));
                panel.add(alumnoPanel);
            }
        }

        return panel;
    }
}