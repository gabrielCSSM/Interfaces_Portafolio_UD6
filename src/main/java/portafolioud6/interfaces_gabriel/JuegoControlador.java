package portafolioud6.interfaces_gabriel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;

public class JuegoControlador implements Initializable {
    int creditos = 10;
    boolean juegoEmpezado = false;
    Baraja miBaraja = new Baraja();
    Casa maquina = new Casa();
    Jugador jugador = new Jugador();
    ArrayList<Carta> barajaJugador = jugador.getCartas();
    ArrayList<Carta> barajaCasa = maquina.getCartas();
    int cartasExtra = 0;
    int cartasDescubiertas = 0;
    @FXML
    HBox mesaJugador, mesaCasa;
    @FXML
    TextField puntosCasa, puntosJugador, campoCreditos;
    @FXML
    Button empezarJuego, darCarta, turnoMaquina, salir;

    //Metodo para barajar
    void barajar() {

        //NO SE REVELO NINGUNA CARTA
        for (Carta c : miBaraja.getBaraja()) {
            c.setDescubierta(false);
        }

        //NADIE TIENE PUNTOS
        puntosJugador.setText("0");
        puntosCasa.setText("0");
        maquina.reiniciarse();
        jugador.reiniciarse();

        //NADIE TIENE CARTAS
        barajaJugador.clear();
        barajaCasa.clear();

        //NO SE SE VIERON LAS CARTAS
        cartasExtra = 0;
        cartasDescubiertas = 0;

        //NADIE TIENE CARTAS EN MESA
        mesaJugador.getChildren().clear();
        mesaCasa.getChildren().clear();

        //CREDITOS RESTANTES
        campoCreditos.setText(String.valueOf(creditos));

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
        puntosJugador.setText(String.valueOf(puntos));
    }

    void establecerPuntosCasa(int puntos) {
        puntosCasa.setText(String.valueOf(puntos));
    }

    //Metodos de las acciones de ambas entidades (Jugador y Maquina (Casa))
    void accionJugador() {

        Carta miCarta = obtenerCarta();

        if (miCarta.isDescubierta() == true) {
            cartasDescubiertas += 1;
            accionJugador();
        } else {
            miCarta.setDescubierta(true);

            if (miCarta.getCarta().equalsIgnoreCase("ace")) {
                if (jugador.pensar()) {
                    miCarta.setValor(1);
                } else {
                    miCarta.setValor(11);
                }
            }

            jugador.cartaObtenida(miCarta);
            establecerPuntosJugador(jugador.getPuntos());

            mesaJugador.getChildren().add(formatoCarta(miCarta.getImg()));

        }
    }


    void accionCasa(boolean oculta) {

        Carta miCarta = obtenerCarta();

        if (miCarta.isDescubierta() == true) {
            cartasDescubiertas += 1;
            accionCasa(oculta);
        } else {
            miCarta.setDescubierta(true);
            establecerPuntosCasa(maquina.getPuntos());

            if (miCarta.getCarta().equalsIgnoreCase("ace")) {
                if (maquina.pensar()) {
                    miCarta.setValor(1);
                } else {
                    miCarta.setValor(11);
                }
            }

            maquina.cartaObtenida(miCarta);

            if (oculta == true) {
                mesaCasa.getChildren().add(formatoCarta(new Image(getClass().getResourceAsStream("baraja/secret_card.png"))));
            } else {
                mesaCasa.getChildren().add(formatoCarta(miCarta.getImg()));
            }

        }
    }

    //Comenzar un Juego Barajando
    void comenzar() {
        barajar();
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
        establecerPuntosCasa(maquina.getPuntos()); // Se actualiza la puntuacion
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        puntosCasa.setEditable(false);
        puntosJugador.setEditable(false);

        empezarJuego.setOnAction(actionEvent -> {
            if (juegoEmpezado == false) {
                creditos = 10;
            }
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
            mensajeDeVictoria();

        });

        salir.setOnAction(actionEvent -> {
            System.exit(0);
        });
    }

    void mensajeDeVictoria() {

        int jugador = Integer.parseInt(puntosJugador.getText());
        int maquina = Integer.parseInt(puntosCasa.getText());

        if (jugador == maquina) {
            hacerAlerta("tablas");
        } else if (jugador == 21) {
            hacerAlerta("Jugador");
        } else if (maquina == 21) {
            hacerAlerta("Maquina");
        } else if (jugador > 21) {
            hacerAlerta("Maquina");
        } else if (maquina > 21) {
            hacerAlerta("Jugador");
        } else {
            int menorJg = 21 - jugador;
            int menorCs = 21 - maquina;
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
            alerta.setContentText("Tus puntos: (" + puntosJugador.getText() + "), Puntos de la Maquina: (" + puntosCasa.getText() + ")");

        } else {
            if (nombre.equalsIgnoreCase("Jugador")) {
                creditos += 1;
            } else {
                creditos -= 1;
            }
            alerta.setTitle(nombre + " ha ganado!");
            alerta.setHeaderText("Felicidades " + nombre + ", por su victoria");
            alerta.setContentText("Tus puntos: (" + puntosJugador.getText() + "), Puntos de la Maquina: (" + puntosCasa.getText() + ")");
        }

        alerta.setOnCloseRequest(dialogEvent -> {
            comenzar();
            juegoEmpezado = true;
            empezarJuego.setText("Reiniciar el Juego");
        });

        alerta.showAndWait();
    }
}