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
        scroll.setPreferredSize(new Dimension(900, 400));
        scroll.setMinimumSize(new Dimension(900, 400));

        seccionPanel.add(headerPanel, BorderLayout.NORTH);
        seccionPanel.add(scroll, BorderLayout.CENTER);
        return seccionPanel;
    }

    private static JPanel crearHeader(String titulo, ActionListener onCrear,
                                      ActionListener onOrdenar, String textoOrdenar,
                                      ActionListener onBuscar, String placeholderBusqueda) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        panelIzquierdo.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panelIzquierdo.add(MyLabel.titulo(titulo));

        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelCentro.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        if (onBuscar != null && placeholderBusqueda != null) {
            panelCentro.add(MyLabel.textoBusqueda("Buscar:"));

            MyTextField txtBuscar = MyTextField.buscar(placeholderBusqueda);
            txtBuscar.setName("campoBusqueda");
            panelCentro.add(txtBuscar);

            MyButton btnBuscar = MyButton.boton4("Buscar", e -> {
                String texto = txtBuscar.getText();
                onBuscar.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, texto));
            });
            panelCentro.add(btnBuscar);
        }

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelDerecho.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panelDerecho.add(MyButton.boton8(textoOrdenar, onOrdenar));
        panelDerecho.add(MyButton.boton1("Crear " + titulo.substring(0, titulo.length() - 1), onCrear));

        headerPanel.add(panelIzquierdo, BorderLayout.WEST);
        headerPanel.add(panelCentro, BorderLayout.CENTER);
        headerPanel.add(panelDerecho, BorderLayout.EAST);
        return headerPanel;
    }

    private static JPanel crearLista(List<AlumnoVisual> elementos, ActionListener onEditar,
                                     ActionListener onEliminar, ActionListener onGestionar,
                                     String textoBoton2, String textoBoton3, String textoBoton4) {
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        listaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));

        if (elementos == null || elementos.isEmpty()) {
            listaPanel.add(Box.createVerticalStrut(100));

            MyLabel emptyLabel = MyLabel.textoListaVacio("No hay elementos registrados");
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
            listaPanel.add(emptyLabel);

            listaPanel.add(Box.createVerticalGlue());
            return listaPanel;
        }

        for (AlumnoVisual alumno : elementos) {
            JPanel itemPanel = crearItemLista(alumno, onEditar, onEliminar, onGestionar,
                    textoBoton2, textoBoton3, textoBoton4);
            listaPanel.add(itemPanel);
            listaPanel.add(Box.createVerticalStrut(8));
        }

        listaPanel.add(Box.createVerticalGlue());
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
        itemPanel.setPreferredSize(new Dimension(860, 65));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        itemPanel.setMinimumSize(new Dimension(860, 65));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        infoPanel.add(MyLabel.textoLista("Nombre: " + alumno.nombre()));
        infoPanel.add(MyLabel.textoLista("Legajo: " + alumno.legajo()));

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        botonesPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);

        botonesPanel.add(MyButton.boton2(textoBoton2, e ->
                onEditar.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, alumno.legajo()))
        ));

        botonesPanel.add(MyButton.boton3(textoBoton3, e ->
                onEliminar.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, alumno.legajo()))
        ));

        if (onGestionar != null) {
            botonesPanel.add(MyButton.boton4(textoBoton4, e ->
                    onGestionar.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, alumno.legajo()))
            ));
        }

        itemPanel.add(infoPanel, BorderLayout.WEST);
        itemPanel.add(botonesPanel, BorderLayout.EAST);
        return itemPanel;
    }

    public record AlumnoVisual(String nombre, String legajo) {}
}
