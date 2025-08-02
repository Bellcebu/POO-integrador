package view.components;

import view.config.ThemeConfig;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MyLayout {

    public static JPanel crearSeccion(String titulo, List<AlumnoVisual> elementos, ActionListener onCrear, ActionListener onEditar, ActionListener onEliminar, ActionListener onGestionar) {
        JPanel seccionPanel = new JPanel(new BorderLayout());
        seccionPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        seccionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = crearHeader(titulo, onCrear);
        JPanel listaPanel = crearLista(elementos, onEditar, onEliminar, onGestionar);

        MyScroll scroll = MyScroll.crearVertical(listaPanel);

        seccionPanel.add(headerPanel, BorderLayout.NORTH);
        seccionPanel.add(scroll, BorderLayout.CENTER);
        return seccionPanel;
    }

    private static JPanel crearHeader(String titulo, ActionListener onCrear) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        MyLabel tituloLabel = MyLabel.titulo(titulo);

        MyButton btnCrear = MyButton.crear("Crear " + titulo.substring(0, titulo.length() - 1), onCrear);

        headerPanel.add(tituloLabel, BorderLayout.WEST);
        headerPanel.add(btnCrear, BorderLayout.EAST);

        return headerPanel;
    }

    private static JPanel crearLista(List<AlumnoVisual> elementos, ActionListener onEditar, ActionListener onEliminar, ActionListener onGestionar) {
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        listaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));

        if (elementos == null || elementos.isEmpty()) {
            MyLabel emptyLabel = MyLabel.centrado("No hay elementos registrados");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
            listaPanel.add(emptyLabel);
            return listaPanel;
        }

        for (AlumnoVisual alumno : elementos) {
            JPanel itemPanel = crearItemLista(alumno, onEditar, onEliminar, onGestionar);
            listaPanel.add(itemPanel);
            listaPanel.add(Box.createVerticalStrut(8));
        }

        return listaPanel;
    }

    private static JPanel crearItemLista(AlumnoVisual alumno, ActionListener onEditar, ActionListener onEliminar, ActionListener onGestionar) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        MyLabel nombreLabel = MyLabel.texto("Nombre: " + alumno.nombre());
        MyLabel legajoLabel = MyLabel.texto("Legajo: " + alumno.legajo());

        infoPanel.add(nombreLabel);
        infoPanel.add(legajoLabel);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        botonesPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        MyButton btnEditar = MyButton.editar("Editar", e -> {
            onEditar.actionPerformed(new java.awt.event.ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, alumno.legajo()));
        });

        MyButton btnEliminar = MyButton.eliminar("Eliminar", e -> {
            onEliminar.actionPerformed(new java.awt.event.ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, alumno.legajo()));
        });

        botonesPanel.add(btnEditar);
        botonesPanel.add(btnEliminar);

        if (onGestionar != null) {
            MyButton btnGestionar = MyButton.info("Gestionar", e -> {
                onGestionar.actionPerformed(new java.awt.event.ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, alumno.legajo()));
            });
            botonesPanel.add(btnGestionar);
        }

        itemPanel.add(infoPanel, BorderLayout.WEST);
        itemPanel.add(botonesPanel, BorderLayout.EAST);

        return itemPanel;
    }

    public record AlumnoVisual(String nombre, String legajo) {}
}