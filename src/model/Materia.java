package model;

import java.util.*;

public class Materia {
    private static final String SEPARADOR = "☆";

    private String codigo;
    private String nombre;
    private int cuatrimestre;
    private boolean esObligatoria;
    private String codigoCarrera; // NUEVO: referencia a la carrera
    private Set<Materia> correlativas;

    public Materia(String codigo, String nombre, int cuatrimestre,
                   boolean esObligatoria, String codigoCarrera) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cuatrimestre = cuatrimestre;
        this.esObligatoria = esObligatoria;
        this.codigoCarrera = codigoCarrera; // NUEVO
        this.correlativas = new HashSet<>();
    }

    //get y set
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
    public String getCodigoCarrera() { // NUEVO
        return codigoCarrera;
    }
    public Set<Materia> getCorrelativas() {
        return correlativas;
    }

    public static String[] getCodigosCorrelativas(String linea) {
        try {
            String[] partes = linea.split(SEPARADOR);
            if (partes.length >= 6 && !partes[5].trim().isEmpty()) {
                return partes[5].trim().split(",");
            }
        } catch (Exception e) {
            System.out.println("Error al obtener correlativas: " + linea);
        }
        return new String[0];
    }

    public void agregarCorrelativa(Materia m) {
        correlativas.add(m);
    }

    //toString y fromString MODIFICADOS
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(codigo).append(SEPARADOR);
        sb.append(nombre).append(SEPARADOR);
        sb.append(cuatrimestre).append(SEPARADOR);
        sb.append(esObligatoria).append(SEPARADOR);
        sb.append(codigoCarrera).append(SEPARADOR); // NUEVO

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
            if (partes.length >= 5) {
                String codigo = partes[0].trim();
                String nombre = partes[1].trim();
                int cuatrimestre = Integer.parseInt(partes[2].trim());
                boolean esObligatoria = Boolean.parseBoolean(partes[3].trim());
                String codigoCarrera = partes[4].trim(); // NUEVO

                return new Materia(codigo, nombre, cuatrimestre, esObligatoria, codigoCarrera);
            }
        } catch (Exception e) {
            System.out.println("Error al parsear materia: " + linea);
        }
        return null;
    }

    //mostrar en terminal
    public String mostrarInfo() {
        return nombre + " (" + codigo + ")";
    }

    public String mostrarInfoCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append(mostrarInfo());
        sb.append(" - Carrera: ").append(codigoCarrera); // NUEVO
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