package view.components;

import view.config.ThemeConfig;
import javax.swing.*;
import java.awt.*;

public class MyLabel extends JLabel {

    public MyLabel(String texto) {
        super(texto);
        configurarEstiloBasico();
    }

    public MyLabel(String texto, int alignment) {
        super(texto, alignment);
        configurarEstiloBasico();
    }

    private void configurarEstiloBasico() {
        setForeground(ThemeConfig.COLOR_TEXTO);
        setFont(new Font("Arial", Font.PLAIN, 12));
    }

    // Métodos estáticos para crear diferentes tipos de labels
    public static MyLabel titulo(String texto) {
        MyLabel label = new MyLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(ThemeConfig.COLOR_TEXTO);
        return label;
    }

    public static MyLabel subtitulo(String texto) {
        MyLabel label = new MyLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(ThemeConfig.COLOR_TEXTO);
        return label;
    }

    public static MyLabel texto(String texto) {
        MyLabel label = new MyLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(ThemeConfig.COLOR_TEXTO);
        return label;
    }

    public static MyLabel info(String texto) {
        MyLabel label = new MyLabel(texto);
        label.setFont(new Font("Arial", Font.ITALIC, 11));
        label.setForeground(ThemeConfig.COLOR_TEXTO);
        return label;
    }

    public static MyLabel centrado(String texto) {
        MyLabel label = new MyLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(ThemeConfig.COLOR_TEXTO);
        return label;
    }
}