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


    public static MyLabel titulo(String texto) {
        MyLabel label = new MyLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }

    public static MyLabel subtitulo(String texto) {
        MyLabel label = new MyLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }

    public static MyLabel texto(String texto) {
        return new MyLabel(texto);
    }

    public static MyLabel info(String texto) {
        MyLabel label = new MyLabel(texto);
        label.setFont(new Font("Arial", Font.ITALIC, 18));
        return label;
    }

    public static MyLabel centrado(String texto) {
        MyLabel label = new MyLabel(texto, SwingConstants.CENTER);
        return label;
    }

    public static MyLabel textoLista(String texto) {
        MyLabel label = new MyLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(ThemeConfig.COLOR_TEXTO);
        return label;
    }

    public static MyLabel textoListaVacio(String texto) {
        MyLabel label = new MyLabel(texto);
        label.setFont(new Font("Arial", Font.ITALIC, 18));
        label.setForeground(ThemeConfig.COLOR_TEXTO);
        return label;
    }

    public static MyLabel textoBusqueda(String texto) {
        MyLabel label = new MyLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(ThemeConfig.COLOR_TEXTO);
        return label;
    }

    public static MyLabel textoFormulario(String texto) {
        MyLabel label = new MyLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(ThemeConfig.COLOR_TEXTO);
        return label;
    }
}
