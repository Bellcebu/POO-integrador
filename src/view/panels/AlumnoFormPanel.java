package view.panels;

import view.components.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AlumnoFormPanel extends JPanel {

    private JTextField txtNombre;
    private JTextField txtLegajo;
    private MyButton btnGuardar;
    private MyButton btnCancelar;

    private ActionListener onGuardar;
    private ActionListener onCancelar;
    private boolean esEliminar;
    private String nombreValor;
    private String legajoValor;

    // Constructor para CREAR
    public AlumnoFormPanel(ActionListener onGuardar, ActionListener onCancelar) {
        this("", "", onGuardar, onCancelar, false);
    }

    // Constructor para EDITAR
    public AlumnoFormPanel(String nombre, String legajo, ActionListener onGuardar, ActionListener onCancelar) {
        this(nombre, legajo, onGuardar, onCancelar, false);
    }

    // Constructor para ELIMINAR
    public AlumnoFormPanel(String nombre, String legajo, ActionListener onGuardar, ActionListener onCancelar, boolean esEliminar) {
        this.onGuardar = onGuardar;
        this.onCancelar = onCancelar;
        this.esEliminar = esEliminar;
        this.nombreValor = nombre;
        this.legajoValor = legajo;

        configurarPanel();
        if (esEliminar) {
            crearComponentesEliminar();
            configurarLayoutEliminar();
        } else {
            crearComponentes();
            configurarLayout();
            txtNombre.setText(nombre);
            txtLegajo.setText(legajo);
        }
    }

    private void configurarPanel() {
        setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setPreferredSize(new Dimension(400, 250));
    }

    private void crearComponentes() {
        txtNombre = new JTextField(20);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNombre.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        txtNombre.setForeground(ThemeConfig.COLOR_TEXTO);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        txtLegajo = new JTextField(15);
        txtLegajo.setFont(new Font("Arial", Font.PLAIN, 12));
        txtLegajo.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        txtLegajo.setForeground(ThemeConfig.COLOR_TEXTO);
        txtLegajo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        btnGuardar = MyButton.general1("Guardar", onGuardar);
        btnCancelar = MyButton.general2("Cancelar", onCancelar);
    }

    private void crearComponentesEliminar() {
        btnGuardar = MyButton.general1("Confirmar", onGuardar);
        btnCancelar = MyButton.general2("Cancelar", onCancelar);
    }

    private void configurarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(MyLabel.titulo("Datos del Alumno"), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(MyLabel.texto("Nombre:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        add(MyLabel.texto("Legajo:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtLegajo, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(panelBotones, gbc);
    }

    private void configurarLayoutEliminar() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(MyLabel.titulo("¿Eliminar este alumno?"), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(MyLabel.texto("Nombre:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(MyLabel.texto(nombreValor), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(MyLabel.texto("Legajo:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(MyLabel.texto(legajoValor), gbc);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(panelBotones, gbc);
    }

    public String getNombre() {
        return esEliminar ? nombreValor : txtNombre.getText().trim();
    }

    public String getLegajo() {
        return esEliminar ? legajoValor : txtLegajo.getText().trim();
    }
}