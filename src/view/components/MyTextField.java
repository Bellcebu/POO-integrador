package view.components;

import view.config.ThemeConfig;
import javax.swing.*;
import java.awt.*;

public class MyTextField extends JTextField {

    public MyTextField(int columns) {
        super(columns);
        configurarEstilo();
    }

    public MyTextField(String text, int columns) {
        super(text, columns);
        configurarEstilo();
    }

    private void configurarEstilo() {
        setFont(new Font("Arial", Font.PLAIN, 12));
        setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        setForeground(ThemeConfig.COLOR_TEXTO);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    public static MyTextField buscar(String placeholder) {
        MyTextField textField = new MyTextField(20);
        textField.setToolTipText(placeholder);
        return textField;
    }
}