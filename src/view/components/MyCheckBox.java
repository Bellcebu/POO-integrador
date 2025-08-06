package view.components;

import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;

public class MyCheckBox extends JCheckBox {

    public MyCheckBox() {
        super();
        configurarEstilo();
    }

    public MyCheckBox(boolean selected) {
        super();
        setSelected(selected);
        configurarEstilo();
    }

    private void configurarEstilo() {
        setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        setForeground(ThemeConfig.COLOR_TEXTO);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight()) - 4;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        g2d.setColor(ThemeConfig.COLOR_CHECKBOX_BACKGROUND);
        g2d.fillRoundRect(x, y, size, size, 6, 6);

        g2d.setColor(ThemeConfig.COLOR_BORDE_LINEA_SUAVE);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(x, y, size, size, 6, 6);

        if (isSelected()) {
            g2d.setColor(ThemeConfig.COLOR_CHECKBOX_CHECKED);
            g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int checkSize = size / 3;
            int checkX = x + size / 4;
            int checkY = y + size / 2;

            g2d.drawLine(checkX, checkY, checkX + checkSize / 2, checkY + checkSize / 2);
            g2d.drawLine(checkX + checkSize / 2, checkY + checkSize / 2, checkX + checkSize, checkY - checkSize / 2);
        }

        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(20, 20);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
}