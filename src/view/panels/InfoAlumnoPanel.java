package view.panels;

import model.*;
import controller.AlumnoController;
import view.components.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class InfoAlumnoPanel extends JPanel {

    private Alumno alumno;
    private AlumnoController alumnoController;
    private MyButton btnVolver;
    private ActionListener onVolver;

    public InfoAlumnoPanel(Alumno alumno, AlumnoController alumnoController, ActionListener onVolver) {
        this.alumno = alumno;
        this.alumnoController = alumnoController;
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
        headerPanel.add(MyLabel.titulo("Información del Alumno"));
        mainPanel.add(headerPanel);

        mainPanel.add(Box.createVerticalStrut(20));

        // Información básica
        JPanel infoBasicaPanel = crearSeccionInfo("Datos Básicos", crearInfoBasica());
        mainPanel.add(infoBasicaPanel);

        mainPanel.add(Box.createVerticalStrut(15));

        // Carreras
        JPanel carrerasPanel = crearSeccionInfo("Carreras", crearInfoCarreras());
        mainPanel.add(carrerasPanel);

        mainPanel.add(Box.createVerticalStrut(15));

        // Estadísticas académicas
        JPanel estadisticasPanel = crearSeccionInfo("Resumen Académico", crearEstadisticas());
        mainPanel.add(estadisticasPanel);

        mainPanel.add(Box.createVerticalStrut(15));

        // Progreso por carrera
        JPanel progresoPanel = crearSeccionInfo("Progreso por Carrera", crearProgresoCarreras());
        mainPanel.add(progresoPanel);

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
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        panel.add(MyLabel.texto("Nombre: " + alumno.getNombre()));
        panel.add(MyLabel.texto("Legajo: " + alumno.getLegajo()));

        return panel;
    }

    private JPanel crearInfoCarreras() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        List<Carrera> carreras = alumnoController.obtenerCarrerasDelAlumno(alumno.getLegajo());

        if (carreras.isEmpty()) {
            panel.add(MyLabel.info("No está inscripto en ninguna carrera"));
        } else {
            for (Carrera carrera : carreras) {
                JPanel carreraPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                carreraPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
                carreraPanel.add(MyLabel.texto("• " + carrera.getNombre() + " (" + carrera.getCodigo() + ")"));
                panel.add(carreraPanel);
            }
        }

        return panel;
    }

    private JPanel crearEstadisticas() {
        Map<String, Integer> stats = alumnoController.obtenerEstadisticasAlumno(alumno.getLegajo());

        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        // Cajas de estadísticas
        panel.add(crearCajaEstadistica("Total Materias", stats.get("total"), ThemeConfig.COLOR_ACTUALIZAR));
        panel.add(crearCajaEstadistica("Aprobadas", stats.get("aprobadas"), ThemeConfig.COLOR_CREAR));
        panel.add(crearCajaEstadistica("En Curso", stats.get("curso"), ThemeConfig.COLOR_ELIMINAR));
        panel.add(crearCajaEstadistica("Promocionadas", stats.get("promocionadas"), ThemeConfig.COLOR_CREAR));
        panel.add(crearCajaEstadistica("Por Final", stats.get("finales"), ThemeConfig.COLOR_EDITAR));
        panel.add(crearCajaEstadistica("Por Parcial", stats.get("parciales"), ThemeConfig.COLOR_ACTUALIZAR));

        return panel;
    }

    private JPanel crearCajaEstadistica(String titulo, int valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.setPreferredSize(new Dimension(120, 60));

        MyLabel labelTitulo = new MyLabel(titulo, SwingConstants.CENTER);
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 10));

        MyLabel labelValor = new MyLabel(String.valueOf(valor), SwingConstants.CENTER);
        labelValor.setForeground(Color.WHITE);
        labelValor.setFont(new Font("Arial", Font.BOLD, 18));

        panel.add(labelTitulo, BorderLayout.NORTH);
        panel.add(labelValor, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearProgresoCarreras() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        List<Carrera> carreras = alumnoController.obtenerCarrerasDelAlumno(alumno.getLegajo());

        if (carreras.isEmpty()) {
            panel.add(MyLabel.info("No hay carreras para mostrar progreso"));
        } else {
            for (Carrera carrera : carreras) {
                JPanel progresoCarrera = crearProgresoCarrera(carrera);
                panel.add(progresoCarrera);
                panel.add(Box.createVerticalStrut(10));
            }
        }

        return panel;
    }

    private JPanel crearProgresoCarrera(Carrera carrera) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Título de la carrera
        MyLabel labelCarrera = MyLabel.texto(carrera.getNombre());
        labelCarrera.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(labelCarrera, BorderLayout.NORTH);

        // Progreso
        JPanel progresoPanel = new JPanel(new GridLayout(1, 2, 10, 5));
        progresoPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        // Materias obligatorias
        int obligatoriasTotal = 0;
        int obligatoriasAprobadas = 0;

        // Materias optativas
        int optativasAprobadas = 0;
        int optativasNecesarias = carrera.getCantidadOptativasNecesarias();

        for (Materia materia : carrera.getMaterias()) {
            if (alumno.estaInscriptoEn(materia)) {
                if (materia.esObligatoria()) {
                    obligatoriasTotal++;
                    if (alumno.aproboMateria(materia)) {
                        obligatoriasAprobadas++;
                    }
                } else {
                    if (alumno.aproboMateria(materia)) {
                        optativasAprobadas++;
                    }
                }
            } else if (materia.esObligatoria()) {
                obligatoriasTotal++;
            }
        }

        String textoObligatorias = "Obligatorias: " + obligatoriasAprobadas + "/" + obligatoriasTotal;
        String textoOptativas = "Optativas: " + optativasAprobadas + "/" + optativasNecesarias;

        progresoPanel.add(MyLabel.texto(textoObligatorias));
        progresoPanel.add(MyLabel.texto(textoOptativas));

        panel.add(progresoPanel, BorderLayout.CENTER);

        // Estado de finalización
        boolean finalizoCarrera = carrera.finalizoCarrera(alumno);
        MyLabel labelEstado = MyLabel.info(finalizoCarrera ? "CARRERA FINALIZADA" : "En progreso");
        if (finalizoCarrera) {
            labelEstado.setForeground(ThemeConfig.COLOR_CREAR);
            labelEstado.setFont(new Font("Arial", Font.BOLD, 11));
        }
        panel.add(labelEstado, BorderLayout.SOUTH);

        return panel;
    }
}