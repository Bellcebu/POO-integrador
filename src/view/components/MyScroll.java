package view.components;

import view.config.ThemeConfig;

import javax.swing.*;
import javax.swing.plaf.basic.*;
import java.awt.*;

public class MyScroll extends JScrollPane {

    public MyScroll(Component view) {
        super(view);
        configurarScroll();
    }

    public MyScroll() {
        super();
        configurarScroll();
    }

    private void configurarScroll() {
        setBorder(BorderFactory.createEmptyBorder());
        getVerticalScrollBar().setUnitIncrement(16);

        JScrollBar verticalScrollBar = getVerticalScrollBar();
        JScrollBar horizontalScrollBar = getHorizontalScrollBar();

        verticalScrollBar.setUI(new CustomScrollBarUI());
        horizontalScrollBar.setUI(new CustomScrollBarUI());

        getViewport().setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
    }

    public static MyScroll crearVertical(Component componente) {
        MyScroll scroll = new MyScroll(componente);
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private static class CustomScrollBarUI extends BasicScrollBarUI {
        private final int ARC = 10;

        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = ThemeConfig.COLOR_SCROLLBAR_THUMB;
            this.trackColor = ThemeConfig.COLOR_SCROLLBAR_TRACK;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0,0));
            btn.setMinimumSize(new Dimension(0,0));
            btn.setMaximumSize(new Dimension(0,0));
            return btn;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(thumbColor);

            int padding = 2;
            g2.fillRoundRect(thumbBounds.x + padding, thumbBounds.y + padding,
                    thumbBounds.width - padding * 2, thumbBounds.height - padding * 2,
                    6, 6);
            g2.dispose();
        }
    }
}
