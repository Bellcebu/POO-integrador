package view.panels;

import model.Carrera;
import model.Facultad;
import view.components.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MateriaFormPanel extends JPanel {

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtCuatrimestre;
    private JRadioButton rbObligatoria;
    private JRadioButton rbOptativa;
    private JComboBox<String> cmbCarrera;
    private MyButton btnGuardar;
    private MyButton btnCancelar;

    private ActionListener onGuardar;
    private ActionListener onCancelar;

    public MateriaFormPanel(ActionListener onGuardar, ActionListener onCancelar) {
        this.onGuardar = onGuardar;
        this.onCancelar = onCancelar;

        configurarPanel();
        crearComponentes();
        configurarLayout();
        cargarCarreras();
    }

    private void configurarPanel() {
        setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setPreferredSize(new Dimension(500, 400));
    }

    private void crearComponentes() {
        txtCodigo = new JTextField(15);
        configurarTextField(txtCodigo);

        txtNombre = new JTextField(25);
        configurarTextField(txtNombre);

        txtCuatrimestre = new JTextField(10);
        configurarTextField(txtCuatrimestre);

        // Radio buttons para obligatoria/optativa
        rbObligatoria = new JRadioButton("Obligatoria");
        rbOptativa = new JRadioButton("Optativa");
        configurarRadioButton(rbObligatoria);
        configurarRadioButton(rbOptativa);

        ButtonGroup grupoObligatoria = new ButtonGroup();
        grupoObligatoria.add(rbObligatoria);
        grupoObligatoria.add(rbOptativa);
        rbObligatoria.setSelected(true); // Por defecto obligatoria

        // ComboBox para carreras
        cmbCarrera = new JComboBox<>();
        cmbCarrera.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbCarrera.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        cmbCarrera.setForeground(ThemeConfig.COLOR_TEXTO);

        btnGuardar = MyButton.boton6("Guardar", onGuardar);
        btnCancelar = MyButton.boton7("Cancelar", onCancelar);
    }

    private void configurarTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        textField.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        textField.setForeground(ThemeConfig.COLOR_TEXTO);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private void configurarRadioButton(JRadioButton radioButton) {
        radioButton.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        radioButton.setForeground(ThemeConfig.COLOR_TEXTO);
        radioButton.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    private void configurarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);

        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(MyLabel.titulo("Nueva Materia"), gbc);

        // Código
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(MyLabel.texto("Código:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtCodigo, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        add(MyLabel.texto("Nombre:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtNombre, gbc);

        // Cuatrimestre
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        add(MyLabel.texto("Cuatrimestre:"), gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtCuatrimestre, gbc);

        // Tipo (Obligatoria/Optativa)
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        add(MyLabel.texto("Tipo:"), gbc);

        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTipo.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panelTipo.add(rbObligatoria);
        panelTipo.add(rbOptativa);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(panelTipo, gbc);

        // Carrera
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        add(MyLabel.texto("Carrera:"), gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(cmbCarrera, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(panelBotones, gbc);
    }

    private void cargarCarreras() {
        cmbCarrera.removeAllItems();
        for (Carrera carrera : Facultad.getInstance().getCarreras()) {
            cmbCarrera.addItem(carrera.getCodigo() + " - " + carrera.getNombre());
        }
    }

    // Getters
    public String getCodigo() {
        return txtCodigo.getText().trim();
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public int getCuatrimestre() {
        try {
            return Integer.parseInt(txtCuatrimestre.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean esObligatoria() {
        return rbObligatoria.isSelected();
    }

    public String getCodigoCarrera() {
        String seleccion = (String) cmbCarrera.getSelectedItem();
        if (seleccion != null && seleccion.contains(" - ")) {
            return seleccion.split(" - ")[0];
        }
        return "";
    }
}