package view.components;


import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    private static final String TITULO = "Sistema Académico - UNTDF - Integrador POO";

    public MyFrame() {
        super(TITULO);
        configurarFrame();
    }

    private void configurarFrame() {
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        setResizable(false);
    }


}