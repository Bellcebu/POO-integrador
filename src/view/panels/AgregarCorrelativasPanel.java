package view.panels;

import model.Materia;
import model.Facultad;
import view.components.*;
import view.config.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AgregarCorrelativasPanel extends JPanel {

    private JList<Materia> listaMaterias;
    private DefaultListModel<Materia> modeloLista;
    private MyButton btnAgregar;
    private MyButton btnCancelar;
    private Materia materia;
    private ActionListener onAgregar;
    private ActionListener onCancelar;

    public AgregarCorrelativasPanel(Materia materia, ActionListener onAgregar, ActionListener onCancelar) {
        this.materia = materia;
        this.onAgregar = onAgregar;
        this.onCancelar = onCancelar;

        configurarPanel();
        crearComponentes();
        configurarLayout();
        cargarMaterias();
    }

    private void configurarPanel() {
        setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeConfig.COLOR_BORDE_LINEA_SUAVE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setPreferredSize(new Dimension(600, 500));
    }

    private void crearComponentes() {
        modeloLista = new DefaultListModel<>();
        listaMaterias = new JList<>(modeloLista);
        listaMaterias.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaMaterias.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        listaMaterias.setForeground(ThemeConfig.COLOR_TEXTO);
        listaMaterias.setFont(new Font("Arial", Font.PLAIN, 12));

        // Renderer personalizado
        listaMaterias.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Materia) {
                    Materia m = (Materia) value;
                    setText(m.getNombre() + " (" + m.getCodigo() + ") - Cuatr: " + m.getCuatrimestre());
                }
                return this;
            }
        });

        btnAgregar = MyButton.boton6("Agregar Seleccionadas", onAgregar);
        btnCancelar = MyButton.boton7("Cancelar", onCancelar);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout());

        // Título
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        headerPanel.add(MyLabel.titulo("Agregar Correlativas a: " + materia.getNombre()));
        add(headerPanel, BorderLayout.NORTH);

        // Información actual
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        String correlativasActuales = materia.getCorrelativas().isEmpty() ? "Ninguna" :
                materia.getCorrelativas().size() + " correlativas";
        infoPanel.add(MyLabel.info("Correlativas actuales: " + correlativasActuales), BorderLayout.CENTER);
        add(infoPanel, BorderLayout.NORTH);

        // Lista de materias
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        centerPanel.add(MyLabel.subtitulo("Materias disponibles (Ctrl+Click para múltiples):"), BorderLayout.NORTH);

        MyScroll scrollMaterias = MyScroll.crearVertical(listaMaterias);
        scrollMaterias.setPreferredSize(new Dimension(550, 300));
        centerPanel.add(scrollMaterias, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Botones
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(ThemeConfig.COLOR_SECCIONPANEL_BACKGROUND);
        bottomPanel.add(btnAgregar);
        bottomPanel.add(btnCancelar);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void cargarMaterias() {
        List<Materia> todasLasMaterias = Facultad.getInstance().getMaterias();

        for (Materia m : todasLasMaterias) {
            // No mostrar la materia actual ni sus correlativas existentes
            if (!m.getCodigo().equals(materia.getCodigo()) &&
                    !materia.getCorrelativas().contains(m)) {
                modeloLista.addElement(m);
            }
        }
    }

    public List<String> getCorrelativasSeleccionadas() {
        List<Materia> seleccionadas = listaMaterias.getSelectedValuesList();
        List<String> codigos = new ArrayList<>();

        for (Materia m : seleccionadas) {
            codigos.add(m.getCodigo());
        }

        return codigos;
    }

    public boolean haySeleccion() {
        return !listaMaterias.getSelectedValuesList().isEmpty();
    }
}