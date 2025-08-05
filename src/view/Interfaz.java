package view;

import view.components.*;
import view.config.ThemeConfig;
import view.panels.*;

import javax.swing.*;
import java.awt.*;

public class Interfaz {

    private MyFrame frame;
    private JPanel contentPanel;
    private SidebarNavbar sidebar;

    public Interfaz() {
        ThemeConfig.aplicarTema();

        SwingUtilities.invokeLater(() -> {
            inicializarComponentes();
            configurarLayout();
            mostrarAlumnos();
            frame.setVisible(true);
        });
    }

    private void inicializarComponentes() {
        frame = new MyFrame();
        sidebar = new SidebarNavbar(
                e -> mostrarAlumnos(),
                e -> mostrarMaterias(),
                e -> mostrarCarreras(),
                e -> refrescarTodaLaInterfaz()
        );

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
    }

    private void configurarLayout() {
        frame.setLayout(new BorderLayout());
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);
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

    private void refrescarTodaLaInterfaz() {
        frame.getContentPane().setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        contentPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

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

        frame.repaint();
    }

    public void toggleTema() {
        ThemeConfig.modoOscuro = !ThemeConfig.modoOscuro;
        ThemeConfig.aplicarTema();
        sidebar.refrescarTema();
        refrescarTodaLaInterfaz();
    }
}
