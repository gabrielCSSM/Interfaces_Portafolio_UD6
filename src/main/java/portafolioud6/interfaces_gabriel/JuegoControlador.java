package portafolioud6.interfaces_gabriel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

public class JuegoControlador implements Initializable {
    boolean juegoEmpezado = false;
    Baraja miBaraja = new Baraja();
    Casa laCasa = new Casa();
    ArrayList<Carta> barajaJugador = new ArrayList<>();
    ArrayList<Carta> barajaCasa = laCasa.getCartas();
    int cartasExtra = 0;
    int cartasDescubiertas = 0;
    @FXML
    HBox mesaJugador, mesaCasa;
    @FXML
    TextField puntosCasa, puntosJugador;
    @FXML
    Button empezarJuego, darCarta, turnoMaquina;

    //Metodo para barajar
    void barajar() {

        //NO SE REVELO NINGUNA CARTA
        for (Carta c : miBaraja.getBaraja()) {
            c.setDescubierta(false);
        }

        //NADIE TIENE PUNTOS
        puntosJugador.setText("0");
        puntosCasa.setText("0");
        laCasa.reiniciarse();

        //NADIE TIENE CARTAS
        barajaJugador.clear();
        barajaCasa.clear();

        //NO SE SE VIERON LAS CARTAS
        cartasExtra = 0;
        cartasDescubiertas = 0;

        //NADIE TIENE CARTAS EN MESA
        mesaJugador.getChildren().clear();
        mesaCasa.getChildren().clear();

    }

    //Metodo para obtener una Carta
    Carta obtenerCarta() {
        Carta miCarta = miBaraja.getBaraja()[obtenerAleatorio()];
        return miCarta;
    }

    //Metodo para obtener un Aleatorio
    int obtenerAleatorio() {
        Random r = new Random();
        return r.nextInt(0, 51);
    }

    //Metodo para obtener el formato de la imagen de la Carta
    ImageView formatoCarta(Image img) {
        ImageView miImagen = new ImageView(img);
        miImagen.setFitWidth(90);
        miImagen.setFitHeight(130);
        return miImagen;
    }

    //Metodos para establecer las puntuaciones de los jugadores
    void establecerPuntosJugador(int puntos) {
        int puntosAntes = puntosJugador.getText().isEmpty() ? 0 : Integer.parseInt(puntosJugador.getText());
        int nuevosPuntos = puntosAntes + puntos;
        puntosJugador.setText(String.valueOf(nuevosPuntos));
    }

    void establecerPuntosCasa(int puntosDespues) {
        puntosCasa.setText(String.valueOf(puntosDespues));
    }

    //Metodos de las acciones de ambas entidades (Jugador y Maquina (Casa))
    void accionJugador() {

        Carta miCarta = obtenerCarta();

        if (miCarta.isDescubierta() == true) {
            cartasDescubiertas += 1;
            seAcabaronLasCartas(cartasDescubiertas);
            accionJugador();
        } else {
            miCarta.setDescubierta(true);
            establecerPuntosJugador(miCarta.getValor());
            barajaJugador.add(miCarta);
            mesaJugador.getChildren().add(formatoCarta(miCarta.getImg()));
        }
    }

    void accionCasa(boolean oculta) {

        Carta miCarta = obtenerCarta();

        if (miCarta.isDescubierta() == true) {
            cartasDescubiertas += 1;
            System.out.println(cartasDescubiertas);
            seAcabaronLasCartas(cartasDescubiertas);
            accionCasa(oculta);
        } else {
            miCarta.setDescubierta(true);
            establecerPuntosCasa(laCasa.getPuntos());
            laCasa.cartaObtenida(miCarta);

            if (oculta == true) {
                mesaCasa.getChildren().add(formatoCarta(new Image(getClass().getResourceAsStream("baraja/secret_card.png"))));
            } else {
                mesaCasa.getChildren().add(formatoCarta(miCarta.getImg()));
            }

        }
    }

    //Metodo para controlar que no se vayan de la baraja
    void seAcabaronLasCartas(int cartas) {
        if (cartas == 21) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("CARTAS ACABADAS");
            alert.setHeaderText("Se ha acabado el numero de cartas que ud. puede pedir");
            alert.setContentText("El juego se reiniciara");
            alert.showAndWait();
            comenzar();
        }
    }

    //Comenzar un Juego Barajando
    void comenzar() {
        barajar();
        barajaJugador.clear();
        mesaJugador.getChildren().clear();
        mostrarManoInicial();
    }

    void mostrarManoInicial() {
        //Repartir 2 cartas al jugador
        accionJugador();
        accionJugador();

        //Repartir 2 cartas a la maquina
        accionCasa(false);
        accionCasa(true);
    }

    void revelarMano() {
        mesaCasa.getChildren().clear(); //Se limpia la mesa de la maquina
        barajaCasa.forEach(carta -> {
            mesaCasa.getChildren().add(formatoCarta(carta.getImg())); //Se revelan las cartas
        });
        establecerPuntosCasa(laCasa.getPuntos()); // Se actualiza la puntuacion
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        puntosCasa.setEditable(false);
        puntosJugador.setEditable(false);

        empezarJuego.setOnAction(actionEvent -> {
            comenzar();
            juegoEmpezado = true;
            empezarJuego.setText("Reiniciar el Juego");
        });

        darCarta.setOnAction(actionEvent -> {
            if (juegoEmpezado == true) {
                accionJugador();
                cartasExtra += 1;
            } else {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El Juego no ha sigo EMPEZADO");
                alerta.setHeaderText("Por favor, inicia el juego");
                alerta.showAndWait();
            }
        });

        turnoMaquina.setOnAction(actionEvent -> {
            //Se revelan tanto las cartas ocultadas como las puntuaciones de la maquina
            if (cartasExtra == 0) {
                revelarMano();
            } else {
                revelarMano();
                int turnos = 0;
                while (turnos < cartasExtra) {
                    accionCasa(false);
                    turnos++;
                }
            }
            mensajeDeVictoria(Integer.parseInt(puntosJugador.getText()), Integer.parseInt(puntosCasa.getText()));

        });
    }

    void mensajeDeVictoria(int puntosJugador, int puntosCasa) {
        if (puntosJugador == puntosCasa) {
            hacerAlerta("tablas");
        } else if (puntosJugador == 21) {
            hacerAlerta("Jugador");
        } else if (puntosCasa == 21) {
            hacerAlerta("Maquina");
        } else if (puntosJugador > 21) {
            hacerAlerta("Maquina");
        } else if (puntosCasa > 21) {
            hacerAlerta("Jugador");
        } else {
            int menorJg = 21 - puntosJugador;
            int menorCs = 21 - puntosCasa;
            if (menorJg < menorCs) {
                hacerAlerta("Jugador");
            } else {
                hacerAlerta("Maquina");
            }
        }
    }

    void hacerAlerta(String nombre) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        if (nombre.equalsIgnoreCase("tablas")) {
            alerta.setTitle("Nadie ha ganado");
            alerta.setHeaderText("Fue un empate");
            alerta.setContentText("Tus puntos: ("+puntosJugador.getText()+"), Puntos de la Maquina: ("+puntosCasa.getText()+")");

        } else {
            alerta.setTitle(nombre + " ha ganado!");
            alerta.setHeaderText("Felicidades " + nombre + ", por su victoria");
            alerta.setContentText("Tus puntos: ("+puntosJugador.getText()+"), Puntos de la Maquina: ("+puntosCasa.getText()+")");
        }

        alerta.setOnCloseRequest(dialogEvent -> {
            comenzar();
            juegoEmpezado = true;
            empezarJuego.setText("Reiniciar el Juego");
        });

        alerta.showAndWait();

    }
}