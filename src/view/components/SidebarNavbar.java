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
    private MyButton fontButton;
    private ActionListener onThemeChange;
    private ActionListener onFontChange;

    public SidebarNavbar(ActionListener alumnos, ActionListener materias, ActionListener carreras) {
        this(alumnos, materias, carreras, null, null);
    }

    public SidebarNavbar(ActionListener alumnos, ActionListener materias, ActionListener carreras,
                         ActionListener onThemeChange) {
        this(alumnos, materias, carreras, onThemeChange, null);
    }

    public SidebarNavbar(ActionListener alumnos, ActionListener materias, ActionListener carreras,
                         ActionListener onThemeChange, ActionListener onFontChange) {
        this.onThemeChange = onThemeChange;
        this.onFontChange = onFontChange;
        configurarPanel();
        crearBotones(alumnos, materias, carreras);
        crearBotonesConfiguracion();
    }

    private void configurarPanel() {
        setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
        setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        setLayout(new BorderLayout());
    }

    private void crearBotones(ActionListener alumnos, ActionListener materias, ActionListener carreras) {
        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new BoxLayout(botonesPanel, BoxLayout.Y_AXIS));
        botonesPanel.setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
        botonesPanel.add(Box.createVerticalStrut(30));

        JButton btnAlumnos = crearBoton("ALUMNOS", alumnos);
        botonesPanel.add(btnAlumnos);
        botonesPanel.add(Box.createVerticalStrut(5));

        JButton btnMaterias = crearBoton("MATERIAS", materias);
        botonesPanel.add(btnMaterias);
        botonesPanel.add(Box.createVerticalStrut(5));

        JButton btnCarreras = crearBoton("CARRERAS", carreras);
        botonesPanel.add(btnCarreras);

        seleccionarBoton(btnAlumnos);

        add(botonesPanel, BorderLayout.CENTER);
    }

    private void crearBotonesConfiguracion() {
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BorderLayout());
        configPanel.setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
        configPanel.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 60));

        themeToggleButton = MyButton.tema("", e -> toggleTema());
        fontButton = MyButton.fuente(e -> cambiarFuente());

        themeToggleButton.setBackground(ThemeConfig.COLOR_TEMA);
        fontButton.setBackground(ThemeConfig.COLOR_TEMA);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
        leftPanel.add(themeToggleButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
        rightPanel.add(fontButton);

        configPanel.add(leftPanel, BorderLayout.WEST);
        configPanel.add(rightPanel, BorderLayout.EAST);

        add(configPanel, BorderLayout.SOUTH);
    }

    private void actualizarBotonesConfiguracion() {
        JPanel configPanel = (JPanel) ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        if (configPanel != null) {
            configPanel.removeAll();

            themeToggleButton = MyButton.tema("", e -> toggleTema());
            fontButton = MyButton.fuente(e -> cambiarFuente());

            themeToggleButton.setBackground(ThemeConfig.COLOR_TEMA);
            fontButton.setBackground(ThemeConfig.COLOR_TEMA);

            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            leftPanel.setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
            leftPanel.add(themeToggleButton);

            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            rightPanel.setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
            rightPanel.add(fontButton);

            configPanel.add(leftPanel, BorderLayout.WEST);
            configPanel.add(rightPanel, BorderLayout.EAST);

            configPanel.revalidate();
            configPanel.repaint();
        }
    }

    private void toggleTema() {
        ThemeConfig.modoOscuro = !ThemeConfig.modoOscuro;
        ThemeConfig.aplicarTema();
        refrescarTema();

        if (onThemeChange != null) {
            onThemeChange.actionPerformed(new ActionEvent(this, 0, "theme_changed"));
        }
    }

    private void cambiarFuente() {
        ThemeConfig.cambiarTamanoFuente();

        if (onFontChange != null) {
            onFontChange.actionPerformed(new ActionEvent(this, 0, "font_changed"));
        }
    }

    private void actualizarColoresSidebar() {
        setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
        actualizarColoresRecursivo(this);
        repaint();
    }

    private void actualizarColoresRecursivo(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                btn.setForeground(ThemeConfig.COLOR_SIDEBAR_TEXT);
                btn.setFont(new Font("Arial", Font.BOLD, ThemeConfig.tamanoFuente));
                if (btn == selectedButton) {
                    btn.setBackground(ThemeConfig.COLOR_SIDEBAR_BUTTON_SELECTED);
                } else if (btn == themeToggleButton || btn == fontButton) {
                    btn.setBackground(ThemeConfig.COLOR_TEMA);
                } else {
                    btn.setBackground(ThemeConfig.COLOR_SIDEBAR_BUTTON);
                }
            } else if (comp instanceof Container) {
                comp.setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
                actualizarColoresRecursivo((Container) comp);
            }
        }
    }

    private JButton crearBoton(String texto, ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setBackground(ThemeConfig.COLOR_SIDEBAR_BUTTON);
        btn.setForeground(ThemeConfig.COLOR_SIDEBAR_TEXT);
        btn.setFont(new Font("Arial", Font.BOLD, ThemeConfig.tamanoFuente));
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
        actualizarBotonesConfiguracion();
        actualizarColoresSidebar();
    }
}
