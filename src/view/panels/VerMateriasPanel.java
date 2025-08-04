package view.panels;

import model.*;
import controller.AlumnoController;
import view.components.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VerMateriasPanel extends JPanel {

    private Alumno alumno;
    private AlumnoController alumnoController;
    private List<InscripcionMateria> inscripciones;
    private MyButton btnVolver;
    private ActionListener onVolver;

    public VerMateriasPanel(Alumno alumno, AlumnoController alumnoController, ActionListener onVolver) {
        this.alumno = alumno;
        this.alumnoController = alumnoController;
        this.onVolver = onVolver;

        configurarPanel();
        cargarInscripciones();
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

    private void cargarInscripciones() {
        inscripciones = alumnoController.obtenerTodasLasInscripciones(alumno.getLegajo());
    }

    private void crearComponentes() {
        // Título
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.add(MyLabel.titulo("Materias de: " + alumno.getNombre()));
        add(headerPanel, BorderLayout.NORTH);

        // Panel principal con scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        if (inscripciones.isEmpty()) {
            mainPanel.add(Box.createVerticalStrut(50));
            JPanel emptyPanel = new JPanel(new FlowLayout());
            emptyPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
            emptyPanel.add(MyLabel.info("El alumno no está inscripto en ninguna materia"));
            mainPanel.add(emptyPanel);
        } else {
            // Resumen
            JPanel resumenPanel = crearResumen();
            mainPanel.add(resumenPanel);
            mainPanel.add(Box.createVerticalStrut(20));

            // Encabezado de la tabla
            JPanel headerTablePanel = crearHeaderTabla();
            mainPanel.add(headerTablePanel);
            mainPanel.add(Box.createVerticalStrut(10));

            // Filas de materias
            for (InscripcionMateria inscripcion : inscripciones) {
                JPanel filaPanel = crearFilaMateria(inscripcion);
                mainPanel.add(filaPanel);
                mainPanel.add(Box.createVerticalStrut(5));
            }
        }

        MyScroll scrollPanel = MyScroll.crearVertical(mainPanel);
        add(scrollPanel, BorderLayout.CENTER);

        // Botón volver
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        btnVolver = MyButton.boton7("Volver", onVolver);
        bottomPanel.add(btnVolver);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel crearResumen() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 5));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        int totalMaterias = inscripciones.size();
        int promocionadas = 0;
        int finalesAprobados = 0;
        int parcialesAprobados = 0;
        int enCurso = 0;

        for (InscripcionMateria inscripcion : inscripciones) {
            if (inscripcion.promociono()) {
                promocionadas++;
            } else if (inscripcion.aproboFinal()) {
                finalesAprobados++;
            } else if (inscripcion.aproboParcial()) {
                parcialesAprobados++;
            } else {
                enCurso++;
            }
        }

        panel.add(crearCajaResumen("Total Materias", String.valueOf(totalMaterias), ThemeConfig.COLOR_ACTUALIZAR));
        panel.add(crearCajaResumen("Promocionadas", String.valueOf(promocionadas), ThemeConfig.COLOR_CREAR));
        panel.add(crearCajaResumen("Finales Aprobados", String.valueOf(finalesAprobados), ThemeConfig.COLOR_EDITAR));
        panel.add(crearCajaResumen("En Curso", String.valueOf(enCurso), ThemeConfig.COLOR_ELIMINAR));

        return panel;
    }

    private JPanel crearCajaResumen(String titulo, String valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        MyLabel labelTitulo = new MyLabel(titulo, SwingConstants.CENTER);
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 12));

        MyLabel labelValor = new MyLabel(valor, SwingConstants.CENTER);
        labelValor.setForeground(Color.WHITE);
        labelValor.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(labelTitulo, BorderLayout.NORTH);
        panel.add(labelValor, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearHeaderTabla() {
        JPanel panel = new JPanel(new GridLayout(1, 6, 10, 5));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        panel.add(MyLabel.subtitulo("Materia"));
        panel.add(MyLabel.subtitulo("Código"));
        panel.add(MyLabel.subtitulo("Cuatrimestre"));
        panel.add(MyLabel.subtitulo("Carrera"));
        panel.add(MyLabel.subtitulo("Estado"));
        panel.add(MyLabel.subtitulo("Tipo"));

        return panel;
    }

    private JPanel crearFilaMateria(InscripcionMateria inscripcion) {
        JPanel panel = new JPanel(new GridLayout(1, 6, 10, 5));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        Materia materia = inscripcion.getMateria();

        // Datos de la materia
        panel.add(MyLabel.texto(materia.getNombre()));
        panel.add(MyLabel.texto(materia.getCodigo()));
        panel.add(MyLabel.texto(String.valueOf(materia.getCuatrimestre())));
        panel.add(MyLabel.texto(materia.getCodigoCarrera()));

        // Estado
        String estado;
        Color colorEstado;
        if (inscripcion.promociono()) {
            estado = "PROMOCIONADO";
            colorEstado = ThemeConfig.COLOR_CREAR;
        } else if (inscripcion.aproboFinal()) {
            estado = "FINAL APROBADO";
            colorEstado = ThemeConfig.COLOR_EDITAR;
        } else if (inscripcion.aproboParcial()) {
            estado = "PARCIAL APROBADO";
            colorEstado = ThemeConfig.COLOR_ACTUALIZAR;
        } else {
            estado = "EN CURSO";
            colorEstado = ThemeConfig.COLOR_ELIMINAR;
        }

        MyLabel labelEstado = MyLabel.texto(estado);
        labelEstado.setForeground(colorEstado);
        labelEstado.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(labelEstado);

        // Tipo
        String tipo = materia.esObligatoria() ? "Obligatoria" : "Optativa";
        panel.add(MyLabel.texto(tipo));

        return panel;
    }
}