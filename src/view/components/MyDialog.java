package view.components;

import view.config.ThemeConfig;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MyDialog {

    public static void showMessage(Component parent, String message, String title, int messageType) {
        JPanel messagePanel = createMessagePanel(message);

        MyButton btnAceptar = MyButton.boton1("Aceptar", null);
        btnAceptar.setPreferredSize(new Dimension(100, 35));

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setUndecorated(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE, 2));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(ThemeConfig.COLOR_SIDEBAR_BACKGROUND);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        MyLabel titleLabel = MyLabel.titulo(title);
        titleLabel.setForeground(ThemeConfig.COLOR_TEXTO);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));
        contentPanel.add(messagePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));
        buttonPanel.add(btnAceptar);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);

        btnAceptar.addActionListener(e -> dialog.dispose());

        dialog.pack();

        Dimension size = dialog.getSize();
        int minWidth = 300;
        int maxWidth = 600;
        int minHeight = 150;

        if (size.width < minWidth) size.width = minWidth;
        if (size.width > maxWidth) size.width = maxWidth;
        if (size.height < minHeight) size.height = minHeight;

        dialog.setSize(size);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static JPanel createMessagePanel(String message) {
        JPanel panel = new JPanel();
        panel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String[] lines = message.split("\n");

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                panel.add(Box.createVerticalStrut(5));
                continue;
            }

            if (line.length() > 70) {
                String[] wrappedLines = wrapText(line, 70);
                for (String wrappedLine : wrappedLines) {
                    MyLabel label = MyLabel.textoFormulario(wrappedLine);
                    label.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panel.add(label);
                }
            } else {
                MyLabel label = MyLabel.textoFormulario(line);
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.add(label);
            }
        }

        return panel;
    }

    private static String[] wrapText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return new String[]{text};
        }

        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxLength) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    lines.add(word.substring(0, maxLength - 3) + "...");
                }
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines.toArray(new String[0]);
    }

    public static void showInfo(Component parent, String message, String title) {
        showMessage(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent, String message, String title) {
        showMessage(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarning(Component parent, String message, String title) {
        showMessage(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void showSuccess(Component parent, String message) {
        showInfo(parent, message, "Éxito");
    }
}