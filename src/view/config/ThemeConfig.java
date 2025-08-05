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
    public static Color COLOR_TEXTO_BUTTON;

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
            COLOR_TEXTO_BUTTON = Color.WHITE;

            COLOR_SCROLLBAR_BACKGROUND = new Color(70, 55, 50);
            COLOR_SCROLLBAR_TRACK = new Color(45, 35, 30);
            COLOR_SCROLLBAR_THUMB = new Color(150, 120, 90);

            COLOR_SIDEBAR_BACKGROUND = new Color(60, 47, 47);
            COLOR_SIDEBAR_BUTTON = new Color(111, 78, 55);
            COLOR_SIDEBAR_BUTTON_SELECTED = new Color(133, 100, 80);
            COLOR_SIDEBAR_TEXT = Color.WHITE;

            COLOR_SECCIONPANEL_BACKGROUND = new Color(70, 55, 50);
            COLOR_BORDE_LINEA_SUAVE = new Color(99, 72, 50);

        } else {
            COLOR_CREAR = new Color(72, 133, 237);
            COLOR_EDITAR = new Color(38, 166, 154);
            COLOR_ELIMINAR = new Color(244, 67, 54);
            COLOR_VER = new Color(96, 125, 139);
            COLOR_ACTUALIZAR = new Color(255, 193, 7);
            COLOR_TEMA = new Color(236, 239, 241);
            COLOR_TEXTO = new Color(33, 33, 33);
            COLOR_TEXTO_BUTTON = Color.WHITE;

            COLOR_SCROLLBAR_BACKGROUND = new Color(224, 224, 224);
            COLOR_SCROLLBAR_TRACK = new Color(72, 133, 237);
            COLOR_SCROLLBAR_THUMB = new Color(100, 181, 246);

            COLOR_SIDEBAR_BACKGROUND = new Color(250, 250, 250);
            COLOR_SIDEBAR_BUTTON = new Color(72, 133, 237);
            COLOR_SIDEBAR_BUTTON_SELECTED = new Color(100, 181, 246);
            COLOR_SIDEBAR_TEXT = Color.white;

            COLOR_SECCIONPANEL_BACKGROUND = new Color(255, 255, 255);
            COLOR_BORDE_LINEA_SUAVE = new Color(224, 224, 224);
        }
    }
}
