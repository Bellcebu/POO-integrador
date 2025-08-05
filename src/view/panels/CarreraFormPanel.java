package view.panels;

import view.components.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CarreraFormPanel extends JPanel {

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtCantidadOptativas;
    private JComboBox<String> cmbTipoPlan;
    private MyButton btnGuardar;
    private MyButton btnCancelar;

    private ActionListener onGuardar;
    private ActionListener onCancelar;

    public CarreraFormPanel(ActionListener onGuardar, ActionListener onCancelar) {
        this.onGuardar = onGuardar;
        this.onCancelar = onCancelar;

        configurarPanel();
        crearComponentes();
        configurarLayout();
    }

    private void configurarPanel() {
        setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setPreferredSize(new Dimension(450, 350));
    }

    private void crearComponentes() {
        txtCodigo = new JTextField(15);
        txtCodigo.setFont(new Font("Arial", Font.PLAIN, 12));
        txtCodigo.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        txtCodigo.setForeground(ThemeConfig.COLOR_TEXTO);
        txtCodigo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        txtNombre = new JTextField(25);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNombre.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        txtNombre.setForeground(ThemeConfig.COLOR_TEXTO);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        txtCantidadOptativas = new JTextField(10);
        txtCantidadOptativas.setFont(new Font("Arial", Font.PLAIN, 18));
        txtCantidadOptativas.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        txtCantidadOptativas.setForeground(ThemeConfig.COLOR_TEXTO);
        txtCantidadOptativas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        String[] tiposPlanes = {"PLANA", "PLANB", "PLANC", "PLAND", "PLANE"};
        cmbTipoPlan = new JComboBox<>(tiposPlanes);
        cmbTipoPlan.setFont(new Font("Arial", Font.PLAIN, 18));
        cmbTipoPlan.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        cmbTipoPlan.setForeground(ThemeConfig.COLOR_TEXTO);

        btnGuardar = MyButton.boton6("Guardar", onGuardar);
        btnCancelar = MyButton.boton7("Cancelar", onCancelar);
    }

    private void configurarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(MyLabel.titulo("Nueva Carrera"), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(MyLabel.textoFormulario("Código:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        add(MyLabel.textoFormulario("Nombre:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        add(MyLabel.textoFormulario("Optativas necesarias:"), gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtCantidadOptativas, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        add(MyLabel.textoFormulario("Plan de Estudio:"), gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(cmbTipoPlan, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(panelBotones, gbc);
    }

    public String getCodigo() {
        return txtCodigo.getText().trim();
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public int getCantidadOptativas() {
        try {
            return Integer.parseInt(txtCantidadOptativas.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String getTipoPlan() {
        return (String) cmbTipoPlan.getSelectedItem();
    }
}
