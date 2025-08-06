package model;

import java.util.*;

public class Materia {
    private static final String SEPARADOR = "☆";

    private String codigo;
    private String nombre;
    private int cuatrimestre;
    private boolean esObligatoria;
    private Set<Materia> correlativas;

    public Materia(String codigo, String nombre, int cuatrimestre, boolean esObligatoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cuatrimestre = cuatrimestre;
        this.esObligatoria = esObligatoria;
        this.correlativas = new HashSet<>();
    }

    public String getCodigo() {
        return codigo;
    }
    public String getNombre() {
        return nombre;
    }
    public int getCuatrimestre() {
        return cuatrimestre;
    }
    public boolean esObligatoria() {
        return esObligatoria;
    }
    public Set<Materia> getCorrelativas() {
        return correlativas;
    }

    public static String[] getCodigosCorrelativas(String linea) {
        try {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 5 && !partes[4].trim().isEmpty()) {
                return partes[4].trim().split(",");
            }
        } catch (Exception e) {
            System.out.println("Error al obtener correlativas: " + linea);
        }
        return new String[0];
    }

    public void agregarCorrelativa(Materia m) {
        correlativas.add(m);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(codigo).append(SEPARADOR);
        sb.append(nombre).append(SEPARADOR);
        sb.append(cuatrimestre).append(SEPARADOR);
        sb.append(esObligatoria).append(SEPARADOR);

        if (!correlativas.isEmpty()) {
            boolean primera = true;
            for (Materia correlativa : correlativas) {
                if (!primera) {
                    sb.append(",");
                }
                sb.append(correlativa.getCodigo());
                primera = false;
            }
        }
        return sb.toString();
    }

    public static Materia buscarPorCodigo(String codigo, List<Materia> materias) {
        for (Materia materia : materias) {
            if (materia.getCodigo().equals(codigo)) {
                return materia;
            }
        }
        return null;
    }

    public static Materia fromString(String linea) {
        try {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 4) {
                String codigo = partes[0].trim();
                String nombre = partes[1].trim();
                int cuatrimestre = Integer.parseInt(partes[2].trim());
                boolean esObligatoria = Boolean.parseBoolean(partes[3].trim());

                return new Materia(codigo, nombre, cuatrimestre, esObligatoria);
            }
        } catch (Exception e) {
            System.out.println("Error al parsear materia: " + linea);
        }
        return null;
    }

    public String mostrarInfo() {
        return nombre + " (" + codigo + ")";
    }

    public String mostrarInfoCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append(mostrarInfo());
        sb.append(" - Cuatrimestre: ").append(cuatrimestre);
        sb.append(" - ").append(esObligatoria ? "Obligatoria" : "Optativa");

        if (!correlativas.isEmpty()) {
            sb.append(" - Correlativas: ");
            boolean primera = true;
            for (Materia correlativa : correlativas) {
                if (!primera) {
                    sb.append(", ");
                }
                sb.append(correlativa.getCodigo());
                primera = false;
            }
        }
        return sb.toString();
    }
}