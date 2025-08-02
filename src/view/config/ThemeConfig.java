package view.config;

import java.awt.Color;

public class ThemeConfig {

    public static boolean modoOscuro = true;

    public static Color COLOR_CREAR;
    public static Color COLOR_EDITAR;
    public static Color COLOR_ELIMINAR;
    public static Color COLOR_VER;
    public static Color COLOR_ACTUALIZAR;
    public static Color COLOR_TEMA;
    public static Color COLOR_TEXTO;

    public static Color COLOR_SCROLLBAR_BACKGROUND;
    public static Color COLOR_SCROLLBAR_THUMB;
    public static Color COLOR_SCROLLBAR_TRACK;


    public static Color COLOR_SIDEBAR_BACKGROUND;
    public static Color COLOR_SIDEBAR_BUTTON;
    public static Color COLOR_SIDEBAR_BUTTON_SELECTED;
    public static Color COLOR_SIDEBAR_TEXT;

    public static Color COLOR_SECCIONPANEL_BACKGROUND;
    public static Color COLOR_BORDE_LINEA_SUAVE;

    public static void aplicarTema() {
        if (modoOscuro) {
            COLOR_CREAR = new Color(111, 78, 55);
            COLOR_EDITAR = new Color(133, 68, 66);
            COLOR_ELIMINAR = new Color(184, 0, 0);
            COLOR_VER = new Color(236, 224, 209);
            COLOR_ACTUALIZAR = new Color(171, 135, 108);
            COLOR_TEMA = new Color(255, 230, 204);
            COLOR_TEXTO = Color.WHITE;

            COLOR_SCROLLBAR_BACKGROUND = new Color(70, 55, 50);
            COLOR_SCROLLBAR_TRACK = new Color(45, 35, 30);
            COLOR_SCROLLBAR_THUMB = new Color(150, 120, 90);

            COLOR_SIDEBAR_BACKGROUND = new Color(60, 47, 47); // casi negro café
            COLOR_SIDEBAR_BUTTON = new Color(111, 78, 55);
            COLOR_SIDEBAR_BUTTON_SELECTED = new Color(133, 100, 80);
            COLOR_SIDEBAR_TEXT = Color.WHITE;

            COLOR_SECCIONPANEL_BACKGROUND = new Color(70, 55, 50);
            COLOR_BORDE_LINEA_SUAVE = new Color(99, 72, 50);

        } else {
            COLOR_CREAR = new Color(143, 114, 87);
            COLOR_EDITAR = new Color(159, 130, 105);
            COLOR_ELIMINAR = new Color(184, 0, 0);
            COLOR_VER = new Color(120, 100, 80);
            COLOR_ACTUALIZAR = new Color(150, 120, 90);
            COLOR_TEMA = new Color(60, 47, 47);
            COLOR_TEXTO = new Color(51, 34, 17);

            COLOR_SCROLLBAR_BACKGROUND = new Color(219, 193, 172);
            COLOR_SCROLLBAR_TRACK = new Color(200, 180, 160);
            COLOR_SCROLLBAR_THUMB = new Color(143, 114, 87);

            COLOR_SIDEBAR_BACKGROUND = new Color(236, 224, 209);
            COLOR_SIDEBAR_BUTTON = new Color(219, 193, 172);
            COLOR_SIDEBAR_BUTTON_SELECTED = new Color(150, 120, 90);
            COLOR_SIDEBAR_TEXT = new Color(51, 34, 17);

            COLOR_SECCIONPANEL_BACKGROUND = new Color(241, 224, 209);
            COLOR_BORDE_LINEA_SUAVE = new Color(150, 120, 90);
        }
    }
}
