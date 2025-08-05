package view.panels;

import model.*;
import controller.*;
import view.components.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

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
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.add(MyLabel.titulo("Información de la Materia"));
        mainPanel.add(headerPanel);

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(crearSeccionInfo("Datos Básicos", crearInfoBasica()));
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(crearSeccionInfo("Correlativas (" + materia.getCorrelativas().size() + ")", crearInfoCorrelativas()));
        mainPanel.add(Box.createVerticalStrut(15));

        List<Carrera> carrerasQueIncluyen = obtenerCarrerasQueIncluyenMateria();
        mainPanel.add(crearSeccionInfo("Carreras que incluyen esta materia (" + carrerasQueIncluyen.size() + ")", crearInfoCarreras(carrerasQueIncluyen)));
        mainPanel.add(Box.createVerticalStrut(15));

        List<InscripcionMateria> inscripciones = materiaController.obtenerInscripcionesPorMateria(materia.getCodigo());
        mainPanel.add(crearSeccionInfo("Alumnos Inscriptos (" + inscripciones.size() + ")", crearInfoAlumnos(inscripciones)));

        MyScroll scrollPanel = MyScroll.crearVertical(mainPanel);
        add(scrollPanel, BorderLayout.CENTER);

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

        JPanel headerSeccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerSeccion.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerSeccion.add(MyLabel.subtitulo(titulo));
        seccion.add(headerSeccion, BorderLayout.NORTH);
        seccion.add(contenido, BorderLayout.CENTER);

        return seccion;
    }

    private JPanel crearInfoBasica() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        panel.add(MyLabel.textoFormulario("Código: " + materia.getCodigo()));
        panel.add(MyLabel.textoFormulario("Nombre: " + materia.getNombre()));
        panel.add(MyLabel.textoFormulario("Cuatrimestre: " + materia.getCuatrimestre()));
        panel.add(MyLabel.textoFormulario("Tipo: " + (materia.esObligatoria() ? "Obligatoria" : "Optativa")));

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
                correlativaPanel.add(MyLabel.textoFormulario("• " + correlativa.getNombre() + " (" + correlativa.getCodigo() + ")"));
                panel.add(correlativaPanel);
            }
        }
        return panel;
    }

    private List<Carrera> obtenerCarrerasQueIncluyenMateria() {
        List<Carrera> carrerasQueIncluyen = new ArrayList<>();
        for (Carrera carrera : Facultad.getInstance().getCarreras()) {
            if (carrera.getMaterias().contains(materia)) {
                carrerasQueIncluyen.add(carrera);
            }
        }
        return carrerasQueIncluyen;
    }

    private JPanel crearInfoCarreras(List<Carrera> carreras) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        if (carreras.isEmpty()) {
            panel.add(MyLabel.info("Esta materia no está asignada a ninguna carrera"));
        } else {
            for (Carrera carrera : carreras) {
                JPanel carreraPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                carreraPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
                carreraPanel.add(MyLabel.textoFormulario("• " + carrera.getNombre() + " (" + carrera.getCodigo() + ")"));
                panel.add(carreraPanel);
            }
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

                String estado;
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

                alumnoPanel.add(MyLabel.textoFormulario(texto));
                panel.add(alumnoPanel);
            }
        }
        return panel;
    }
}
