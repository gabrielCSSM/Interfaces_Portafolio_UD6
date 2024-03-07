package portafolioud6.interfaces_gabriel;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import com.example.componentecarta.Carta;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.Optional;
import java.util.SplittableRandom;

public class Jugador {
    private String nombre;
    private int victorias;
    private int derrotas;
    private ArrayList<Carta> cartas = new ArrayList<>();
    private int puntos;

    public Jugador() {
        this.derrotas = 0;
        this.victorias = 0;
        this.puntos = 0;
    }


    void cartaObtenida(Carta carta) {
        cartas.add(carta);
        puntos += carta.devolverCarta().getValor();
    }

    void reiniciarse() {
        cartas.clear();
        puntos = 0;
    }

    boolean pensar() {

        boolean opcion = false;

        int valor1 = puntos + 1;
        int valor11 = puntos + 11;

        ButtonType btn1 = new ButtonType("1", ButtonBar.ButtonData.YES);
        ButtonType btn11 = new ButtonType("11", ButtonBar.ButtonData.NO);

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("LE HA SALIDO UN AS");
        alerta.setHeaderText("Escoge su opcion mas favorable:");
        alerta.setContentText("Si escoge 1: " + (valor1) + "\n" + "Si escoge 11:" + (valor11));

        alerta.getButtonTypes().clear();
        alerta.getButtonTypes().add(0, btn1);
        alerta.getButtonTypes().add(1, btn11);

        Optional<ButtonType> botonPulsado = alerta.showAndWait();

        if (botonPulsado.isPresent()) {
            if (botonPulsado.get().equals(btn1)) {
                opcion = true; //Vale 1
            } else {
                opcion = false; //Vale 11
            }
        }

        alerta.setOnCloseRequest(dialogEvent -> {
            System.out.println("no pulse");
        });

        return opcion;
    }

    public ArrayList<Carta> getCartas() {
        return cartas;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }
}


