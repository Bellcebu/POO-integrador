package view.panels;

import model.*;
import controller.MateriaController;
import view.components.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class InfoMateriaPanel extends JPanel {

    private Materia materia;
    private MateriaController materiaController;
    private MyButton btnVolver;
    private ActionListener onVolver;

    public InfoMateriaPanel(Materia materia, MateriaController materiaController, ActionListener onVolver) {
        this.materia = materia;
        this.materiaController = materiaController;
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
        headerPanel.add(MyLabel.titulo("Información de la Materia"));
        mainPanel.add(headerPanel);

        mainPanel.add(Box.createVerticalStrut(20));

        // Información básica
        JPanel infoBasicaPanel = crearSeccionInfo("Datos Básicos", crearInfoBasica());
        mainPanel.add(infoBasicaPanel);

        mainPanel.add(Box.createVerticalStrut(15));

        // Correlativas
        JPanel correlativasPanel = crearSeccionInfo("Correlativas (" + materia.getCorrelativas().size() + ")", crearInfoCorrelativas());
        mainPanel.add(correlativasPanel);

        mainPanel.add(Box.createVerticalStrut(15));

        // Carrera
        JPanel carreraPanel = crearSeccionInfo("Carrera", crearInfoCarrera());
        mainPanel.add(carreraPanel);

        mainPanel.add(Box.createVerticalStrut(15));

        // Alumnos inscriptos
        List<InscripcionMateria> inscripciones = materiaController.obtenerInscripcionesPorMateria(materia.getCodigo());
        JPanel alumnosPanel = crearSeccionInfo("Alumnos Inscriptos (" + inscripciones.size() + ")", crearInfoAlumnos(inscripciones));
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
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        panel.add(MyLabel.texto("Código: " + materia.getCodigo()));
        panel.add(MyLabel.texto("Nombre: " + materia.getNombre()));
        panel.add(MyLabel.texto("Cuatrimestre: " + materia.getCuatrimestre()));
        panel.add(MyLabel.texto("Tipo: " + (materia.esObligatoria() ? "Obligatoria" : "Optativa")));
        panel.add(MyLabel.texto("Código de carrera: " + materia.getCodigoCarrera()));

        return panel;
    }

    private JPanel crearInfoCorrelativas() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        if (materia.getCorrelativas().isEmpty()) {
            panel.add(MyLabel.info("No tiene correlativas"));
        } else {
            for (Materia correlativa : materia.getCorrelativas()) {
                JPanel correlativaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                correlativaPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
                correlativaPanel.add(MyLabel.texto("• " + correlativa.getNombre() + " (" + correlativa.getCodigo() + ")"));
                panel.add(correlativaPanel);
            }
        }

        return panel;
    }

    private JPanel crearInfoCarrera() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        // Buscar la carrera completa
        Carrera carrera = null;
        for (Carrera c : Facultad.getInstance().getCarreras()) {
            if (c.getCodigo().equals(materia.getCodigoCarrera())) {
                carrera = c;
                break;
            }
        }

        if (carrera != null) {
            panel.add(MyLabel.texto(carrera.getNombre() + " (" + carrera.getCodigo() + ")"));
        } else {
            panel.add(MyLabel.texto("Carrera: " + materia.getCodigoCarrera() + " (no encontrada)"));
        }

        return panel;
    }

    private JPanel crearInfoAlumnos(List<InscripcionMateria> inscripciones) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        if (inscripciones.isEmpty()) {
            panel.add(MyLabel.info("No hay alumnos inscriptos"));
        } else {
            for (InscripcionMateria inscripcion : inscripciones) {
                JPanel alumnoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                alumnoPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

                String estado = "";
                if (inscripcion.promociono()) {
                    estado = " [PROMOCIONADO]";
                } else if (inscripcion.aproboFinal()) {
                    estado = " [FINAL APROBADO]";
                } else if (inscripcion.aproboParcial()) {
                    estado = " [PARCIAL APROBADO]";
                } else {
                    estado = " [CURSANDO]";
                }

                String texto = "• " + inscripcion.getAlumno().getNombre() +
                        " (" + inscripcion.getAlumno().getLegajo() + ")" + estado;

                alumnoPanel.add(MyLabel.texto(texto));
                panel.add(alumnoPanel);
            }
        }

        return panel;
    }
}