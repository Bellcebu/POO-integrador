package view.components;

import view.config.ThemeConfig;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MyLayout {

    public static JPanel crearSeccion(String titulo, List<AlumnoVisual> elementos,
                                      ActionListener onCrear, ActionListener onEditar,
                                      ActionListener onEliminar, ActionListener onGestionar,
                                      ActionListener onOrdenar, String textoOrdenar,
                                      ActionListener onBuscar, String placeholderBusqueda,
                                      String textoBoton2, String textoBoton3, String textoBoton4) {
        JPanel seccionPanel = new JPanel(new BorderLayout());
        seccionPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        seccionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = crearHeader(titulo, onCrear, onOrdenar, textoOrdenar, onBuscar, placeholderBusqueda);
        JPanel listaPanel = crearLista(elementos, onEditar, onEliminar, onGestionar, textoBoton2, textoBoton3, textoBoton4);

        MyScroll scroll = MyScroll.crearVertical(listaPanel);

        seccionPanel.add(headerPanel, BorderLayout.NORTH);
        seccionPanel.add(scroll, BorderLayout.CENTER);
        return seccionPanel;
    }

    public static JPanel crearSeccion(String titulo, List<AlumnoVisual> elementos,
                                      ActionListener onCrear, ActionListener onEditar,
                                      ActionListener onEliminar, ActionListener onGestionar,
                                      ActionListener onOrdenar, String textoOrdenar,
                                      ActionListener onBuscar, String placeholderBusqueda) {
        return crearSeccion(titulo, elementos, onCrear, onEditar, onEliminar, onGestionar,
                onOrdenar, textoOrdenar, onBuscar, placeholderBusqueda,
                "Editar", "Eliminar", "Gestionar");
    }

    private static JPanel crearHeader(String titulo, ActionListener onCrear,
                                      ActionListener onOrdenar, String textoOrdenar,
                                      ActionListener onBuscar, String placeholderBusqueda) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        panelIzquierdo.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        MyLabel tituloLabel = MyLabel.titulo(titulo);
        panelIzquierdo.add(tituloLabel);

        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelCentro.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        if (onBuscar != null && placeholderBusqueda != null) {
            MyLabel buscarLabel = MyLabel.texto("Buscar:");
            panelCentro.add(buscarLabel);

            MyTextField txtBuscar = MyTextField.buscar(placeholderBusqueda);
            txtBuscar.setName("campoBusqueda");
            panelCentro.add(txtBuscar);

            MyButton btnBuscar = MyButton.boton4("Buscar", e -> {
                String textoBusqueda = txtBuscar.getText();
                ActionEvent evento = new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, textoBusqueda);
                onBuscar.actionPerformed(evento);
            });
            panelCentro.add(btnBuscar);
        }

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelDerecho.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        MyButton btnOrdenar = MyButton.boton8(textoOrdenar, onOrdenar);
        panelDerecho.add(btnOrdenar);

        MyButton btnCrear = MyButton.boton1("Crear " + titulo.substring(0, titulo.length() - 1), onCrear);
        panelDerecho.add(btnCrear);

        headerPanel.add(panelIzquierdo, BorderLayout.WEST);
        headerPanel.add(panelCentro, BorderLayout.CENTER);
        headerPanel.add(panelDerecho, BorderLayout.EAST);

        return headerPanel;
    }

    private static JPanel crearHeader(String titulo, ActionListener onCrear, ActionListener onOrdenar, String textoOrdenar) {
        return crearHeader(titulo, onCrear, onOrdenar, textoOrdenar, null, null);
    }

    private static JPanel crearLista(List<AlumnoVisual> elementos, ActionListener onEditar,
                                     ActionListener onEliminar, ActionListener onGestionar,
                                     String textoBoton2, String textoBoton3, String textoBoton4) {
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
            JPanel itemPanel = crearItemLista(alumno, onEditar, onEliminar, onGestionar,
                    textoBoton2, textoBoton3, textoBoton4);
            listaPanel.add(itemPanel);
            listaPanel.add(Box.createVerticalStrut(8));
        }

        return listaPanel;
    }


    private static JPanel crearItemLista(AlumnoVisual alumno, ActionListener onEditar,
                                         ActionListener onEliminar, ActionListener onGestionar,
                                         String textoBoton2, String textoBoton3, String textoBoton4) {
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

        MyButton btnEditar = MyButton.boton2(textoBoton2, e -> {
            onEditar.actionPerformed(new java.awt.event.ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, alumno.legajo()));
        });

        MyButton btnEliminar = MyButton.boton3(textoBoton3, e -> {
            onEliminar.actionPerformed(new java.awt.event.ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, alumno.legajo()));
        });

        botonesPanel.add(btnEditar);
        botonesPanel.add(btnEliminar);

        if (onGestionar != null) {
            MyButton btnGestionar = MyButton.boton4(textoBoton4, e -> {
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