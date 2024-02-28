package portafolioud6.interfaces_gabriel;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.Optional;

public class Jugador {
    private ArrayList<Carta> cartas = new ArrayList<>();
    private int puntos = 0;

    void cartaObtenida(Carta carta) {
        cartas.add(carta);
        puntos += carta.getValor();
    }

    void reiniciarse() {
        cartas.clear();
        puntos = 0;
    }

    boolean pensar() {

        boolean opcion = false;

        int valor1 = puntos + 1;
        int valor11 = puntos + 11;

        ButtonType btn1 = new ButtonType("1", ButtonBar.ButtonData.OK_DONE);
        ButtonType btn11 = new ButtonType("11", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("LE HA SALIDO UN AS");
        alerta.setHeaderText("Escoge su opcion mas favorable:");
        alerta.setContentText("Si escoge 1: " + (valor1) + "\n" + "Si escoge 11:" + (valor11));

        alerta.getButtonTypes().clear();
        alerta.getButtonTypes().add(0, btn1);
        alerta.getButtonTypes().add(1, btn11);

        Optional<ButtonType> botonPulsado = alerta.showAndWait();

        if (!botonPulsado.isPresent()) {
            opcion = true; //Vale 1
        } else if (botonPulsado.get().equals(ButtonType.OK)) {
            opcion = true; //Vale 1
        } else if (botonPulsado.get().equals(ButtonType.CANCEL)) {
            opcion = false; //Vale 11
        }

        return opcion;
    }

    public ArrayList<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(ArrayList<Carta> cartas) {
        this.cartas = cartas;
    }
    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}


