package view;

import view.components.MyFrame;
import view.components.SidebarNavbar;
import view.panels.AlumnosPanel;
import view.panels.MateriasPanel;
import view.panels.CarrerasPanel;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;

public class Interfaz {

    private MyFrame frame;
    private JPanel contentPanel;
    private SidebarNavbar sidebar;

    public Interfaz() {
        // IMPORTANTE: Aplicar el tema antes de crear los componentes
        ThemeConfig.aplicarTema();

        // Crear la interfaz en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            inicializarComponentes();
            configurarLayout();
            mostrarAlumnos(); // Panel inicial
            frame.setVisible(true);
        });
    }

    private void inicializarComponentes() {
        frame = new MyFrame();

        // Crear sidebar con los listeners, incluyendo callback para cambio de tema
        sidebar = new SidebarNavbar(
                e -> mostrarAlumnos(),
                e -> mostrarMaterias(),
                e -> mostrarCarreras(),
                e -> refrescarTodaLaInterfaz() // Callback para cambio de tema
        );

        // Panel principal para el contenido
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
    }

    private void configurarLayout() {
        frame.setLayout(new BorderLayout());
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);

        // Aplicar el color de fondo al frame
        frame.getContentPane().setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
    }

    private void mostrarAlumnos() {
        cambiarPanel(new AlumnosPanel());
    }

    private void mostrarMaterias() {
        cambiarPanel(new MateriasPanel());
    }

    private void mostrarCarreras() {
        cambiarPanel(new CarrerasPanel());
    }

    private void cambiarPanel(JPanel nuevoPanel) {
        contentPanel.removeAll();
        contentPanel.add(nuevoPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Método para refrescar toda la interfaz cuando cambia el tema
    private void refrescarTodaLaInterfaz() {
        // Actualizar colores del frame principal
        frame.getContentPane().setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        contentPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        // Recrear el panel actual para aplicar los nuevos colores
        if (contentPanel.getComponentCount() > 0) {
            Component currentPanel = contentPanel.getComponent(0);
            if (currentPanel instanceof AlumnosPanel) {
                mostrarAlumnos();
            } else if (currentPanel instanceof MateriasPanel) {
                mostrarMaterias();
            } else if (currentPanel instanceof CarrerasPanel) {
                mostrarCarreras();
            }
        }

        // Repintar todo
        frame.repaint();
    }

    // Método público para cambiar entre modo claro y oscuro (ahora redundante pero útil)
    public void toggleTema() {
        ThemeConfig.modoOscuro = !ThemeConfig.modoOscuro;
        ThemeConfig.aplicarTema();
        sidebar.refrescarTema();
        refrescarTodaLaInterfaz();
    }
}