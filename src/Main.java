import view.Interfaz;
import model.Facultad;

public class Main {
    public static void main(String[] args) {
        Facultad.getInstance().cargarTodoDesdeArchivos();
        Thread hilo = new Thread(Interfaz::new);
        hilo.start();
    }
}