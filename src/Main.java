import view.Interfaz;

public class Main {
    public static void main(String[] args) {
        Thread hilo = new Thread(Interfaz::new);
        hilo.start();
    }
}