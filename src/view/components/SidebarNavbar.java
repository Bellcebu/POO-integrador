package view.components;

import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SidebarNavbar extends JPanel {

    private static final int SIDEBAR_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;

    private JButton selectedButton;
    private MyButton themeToggleButton;
    private ActionListener onThemeChange;

    public SidebarNavbar(ActionListener alumnos, ActionListener materias, ActionListener carreras) {
        configurarPanel();
        crearBotones(alumnos, materias, carreras);
        crearBotonToggleTema();
    }

    public SidebarNavbar(ActionListener alumnos, ActionListener materias, ActionListener carreras, ActionListener onThemeChange) {
        this.onThemeChange = onThemeChange;
        configurarPanel();
        crearBotones(alumnos, materias, carreras);
        crearBotonToggleTema();
    }

    private void configurarPanel() {
        setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
        setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(30));
    }

    private void crearBotones(ActionListener alumnos, ActionListener materias, ActionListener carreras) {
        JButton btnAlumnos = crearBoton("ALUMNOS", alumnos);
        add(btnAlumnos);
        add(Box.createVerticalStrut(5));

        JButton btnMaterias = crearBoton("MATERIAS", materias);
        add(btnMaterias);
        add(Box.createVerticalStrut(5));

        JButton btnCarreras = crearBoton("CARRERAS", carreras);
        add(btnCarreras);

        seleccionarBoton(btnAlumnos);
    }

    private void crearBotonToggleTema() {
        add(Box.createVerticalGlue());

        JPanel togglePanel = new JPanel();
        togglePanel.setLayout(null);
        togglePanel.setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
        togglePanel.setPreferredSize(new Dimension(200, 60));

        themeToggleButton = MyButton.tema("", e -> toggleTema());
        themeToggleButton.setBounds(15, 210, 50, 50);
        togglePanel.add(themeToggleButton);

        add(togglePanel);
    }

    private void actualizarBotonTema() {
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                Component[] panelComponents = panel.getComponents();
                for (Component panelComp : panelComponents) {
                    if (panelComp == themeToggleButton) {
                        panel.removeAll();
                        themeToggleButton = MyButton.tema("", e -> toggleTema());

                        if (panel.getLayout() == null) {
                            themeToggleButton.setBounds(15, 210, 50, 50);
                        }

                        panel.add(themeToggleButton);
                        panel.revalidate();
                        panel.repaint();
                        return;
                    }
                }
            }
        }
    }

    private void toggleTema() {
        ThemeConfig.modoOscuro = !ThemeConfig.modoOscuro;
        ThemeConfig.aplicarTema();
        actualizarBotonTema();
        actualizarColoresSidebar();

        if (onThemeChange != null) {
            onThemeChange.actionPerformed(new java.awt.event.ActionEvent(this, 0, "theme_changed"));
        }
    }

    private void actualizarColoresSidebar() {
        setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);

        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton && comp != themeToggleButton) {
                JButton btn = (JButton) comp;
                btn.setForeground(ThemeConfig.COLOR_SIDEBAR_TEXT);
                if (btn == selectedButton) {
                    btn.setBackground(ThemeConfig.COLOR_SIDEBAR_BUTTON_SELECTED);
                } else {
                    btn.setBackground(ThemeConfig.COLOR_SIDEBAR_BUTTON);
                }
            } else if (comp instanceof JPanel) {
                comp.setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
            }
        }
        repaint();
    }

    private JButton crearBoton(String texto, ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setBackground(ThemeConfig.COLOR_SIDEBAR_BUTTON);
        btn.setForeground(ThemeConfig.COLOR_SIDEBAR_TEXT);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setPreferredSize(new Dimension(180, BUTTON_HEIGHT));
        btn.setMaximumSize(new Dimension(180, BUTTON_HEIGHT));
        btn.setMinimumSize(new Dimension(180, BUTTON_HEIGHT));

        btn.addActionListener(e -> {
            action.actionPerformed(e);
            seleccionarBoton(btn);
        });

        return btn;
    }

    private void seleccionarBoton(JButton boton) {
        if (selectedButton != null) {
            selectedButton.setBackground(ThemeConfig.COLOR_SIDEBAR_BUTTON);
        }
        boton.setBackground(ThemeConfig.COLOR_SIDEBAR_BUTTON_SELECTED);
        selectedButton = boton;
    }

    public void refrescarTema() {
        actualizarBotonTema();
        actualizarColoresSidebar();
    }
}
