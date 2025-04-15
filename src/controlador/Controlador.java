package controlador;

import model.Model;
import vista.Vista;

public class Controlador implements Notificar {

    private Vista vista;
    private Model model;

    public static void main(String[] args) {
        new Controlador().inicialitzar();
    }

    private void inicialitzar() {
        this.model = new Model(this);
        this.vista = new Vista(this);
        this.vista.mostrar();
    }

    @Override
    public void notificar(Notificacio notificacio) {
        // Lògica per gestionar esdeveniments segons el tipus
        switch (notificacio) {
            case ARRANCAR -> System.out.println("Sistema inicialitzat");
            // Altres casos aquí
        }
    }
}
