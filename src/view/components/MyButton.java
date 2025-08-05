package view.components;

import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MyButton extends JButton {

    public MyButton(String texto) {
        super(texto);
        configurarEstiloBasico();
    }

    public MyButton(String texto, ActionListener listener) {
        super(texto);
        configurarEstiloBasico();
        addActionListener(listener);
    }

    private void configurarEstiloBasico() {
        setFont(new Font("Arial", Font.BOLD, 18));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arcSize = height;

        if (getModel().isPressed()) {
            g2d.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            g2d.setColor(getBackground().brighter());
        } else {
            g2d.setColor(getBackground());
        }

        g2d.fillRoundRect(0, 0, width, height, arcSize, arcSize);

        Icon icon = getIcon();
        if (icon != null) {
            int iconX = (width - icon.getIconWidth()) / 2;
            int iconY = (height - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g2d, iconX, iconY);
        } else {
            String text = getText();
            if (text != null && !text.isEmpty()) {
                g2d.setColor(getForeground());
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getAscent();
                int x = (width - textWidth) / 2;
                int y = (height + textHeight) / 2 - 2;
                g2d.drawString(text, x, y);
            }
        }

        g2d.dispose();
    }


    public static MyButton boton1(String texto, ActionListener listener) {
        MyButton btn = new MyButton(texto, listener);
        btn.setBackground(ThemeConfig.COLOR_CREAR);
        btn.setForeground(ThemeConfig.COLOR_TEXTO_BUTTON);
        return btn;
    }

    public static MyButton boton2(String texto, ActionListener listener) {
        MyButton btn = new MyButton(texto, listener);
        btn.setBackground(ThemeConfig.COLOR_EDITAR);
        btn.setForeground(ThemeConfig.COLOR_TEXTO_BUTTON);
        return btn;
    }

    public static MyButton boton3(String texto, ActionListener listener) {
        MyButton btn = new MyButton(texto, listener);
        btn.setBackground(ThemeConfig.COLOR_ELIMINAR);
        btn.setForeground(ThemeConfig.COLOR_TEXTO_BUTTON);
        return btn;
    }

    public static MyButton boton4(String texto, ActionListener listener) {
        MyButton btn = new MyButton(texto, listener);
        btn.setBackground(ThemeConfig.COLOR_ACTUALIZAR);
        btn.setForeground(ThemeConfig.COLOR_TEXTO_BUTTON);
        return btn;
    }

    public static MyButton boton5(String texto, ActionListener listener) {
        MyButton btn = new MyButton(texto, listener);
        btn.setBackground(ThemeConfig.COLOR_ACTUALIZAR);
        btn.setForeground(ThemeConfig.COLOR_TEXTO_BUTTON);
        return btn;
    }

    public static MyButton boton6(String texto, ActionListener listener) {
        MyButton btn = new MyButton(texto, listener);
        btn.setBackground(ThemeConfig.COLOR_CREAR);
        btn.setForeground(ThemeConfig.COLOR_TEXTO_BUTTON);
        return btn;
    }

    public static MyButton boton7(String texto, ActionListener listener) {
        MyButton btn = new MyButton(texto, listener);
        btn.setBackground(ThemeConfig.COLOR_ELIMINAR);
        btn.setForeground(ThemeConfig.COLOR_TEXTO_BUTTON);
        return btn;
    }

    public static MyButton boton8(String texto, ActionListener listener) {
        MyButton btn = new MyButton(texto, listener);
        btn.setBackground(ThemeConfig.COLOR_ACTUALIZAR);
        btn.setForeground(ThemeConfig.COLOR_TEXTO_BUTTON);
        return btn;
    }

    public static MyButton tema(String texto, ActionListener listener) {
        MyButton btn = new MyButton("", listener);

        ImageIcon icono;
        if (ThemeConfig.modoOscuro) {
            icono = new ImageIcon(MyButton.class.getResource("/view/resources/sol.png"));
            btn.setBackground(ThemeConfig.COLOR_TEMA);
        } else {
            icono = new ImageIcon(MyButton.class.getResource("/view/resources/luna.png"));
            btn.setBackground(ThemeConfig.COLOR_TEMA);
        }

        Image imagen = icono.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        btn.setIcon(new ImageIcon(imagen));

        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(50, 50));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);

        return btn;
    }
}
