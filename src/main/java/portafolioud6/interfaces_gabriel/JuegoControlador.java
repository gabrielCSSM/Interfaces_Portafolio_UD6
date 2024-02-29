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

    //Declaracion de Variables
    int creditos = 10;
    boolean juegoEmpezado = false;
    Baraja miBaraja = new Baraja();
    Maquina maquina = new Maquina();
    Jugador jugador = new Jugador();
    ArrayList<Carta> barajaJugador = jugador.getCartas();
    ArrayList<Carta> barajaMaquina = maquina.getCartas();
    int cartasExtra = 0;
    int cartasDescubiertas = 0;
    @FXML
    HBox mesaJugador, mesaMaquina;
    @FXML
    TextField puntosMaquina, puntosJugador, campoCreditos;
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
        puntosMaquina.setText("0");
        maquina.reiniciarse();
        jugador.reiniciarse();

        //NADIE TIENE CARTAS
        barajaJugador.clear();
        barajaMaquina.clear();

        //NO SE SE VIERON LAS CARTAS
        cartasExtra = 0;
        cartasDescubiertas = 0;

        //NADIE TIENE CARTAS EN MESA
        mesaJugador.getChildren().clear();
        mesaMaquina.getChildren().clear();

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

    void establecerPuntosMaquina(int puntos) {
        puntosMaquina.setText(String.valueOf(puntos));
    }

    //Metodos de las acciones de ambas entidades (Jugador y Maquina)
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


    void accionMaquina(boolean oculta) {

        Carta miCarta = obtenerCarta();

        if (miCarta.isDescubierta() == true) {
            cartasDescubiertas += 1;
            accionMaquina(oculta);
        } else {
            miCarta.setDescubierta(true);

            establecerPuntosMaquina(maquina.getPuntos());

            if (miCarta.getCarta().equalsIgnoreCase("ace")) {
                if (maquina.pensar()) {
                    miCarta.setValor(1);
                } else {
                    miCarta.setValor(11);
                }
            }

            maquina.cartaObtenida(miCarta);

            if (oculta == true) {
                mesaMaquina.getChildren().add(formatoCarta(new Image(getClass().getResourceAsStream("baraja/secret_card.png"))));
            } else {
                mesaMaquina.getChildren().add(formatoCarta(miCarta.getImg()));
            }

            establecerPuntosMaquina(maquina.getPuntos());
        }
    }

    //Comenzar un Juego Barajando
    void comenzar() {
        sinCreditos();
        barajar();
        mostrarManoInicial();
    }

    void mostrarManoInicial() {
        //Repartir 2 cartas al jugador
        accionJugador();
        accionJugador();

        //Repartir 2 cartas a la maquina
        accionMaquina(false);
        accionMaquina(true);

        //Comprobar si la mano del Jugador, tiene las cartas necesarias para ganar al principio
        esBlackJack();
    }

    //Metodo para revelar la carta oculta de la maquina
    void revelarMano() {
        mesaMaquina.getChildren().clear(); //Se limpia la mesa de la maquina
        barajaMaquina.forEach(carta -> {
            mesaMaquina.getChildren().add(formatoCarta(carta.getImg())); //Se revelan las cartas
        });
        establecerPuntosMaquina(maquina.getPuntos()); // Se actualiza la puntuacion
    }

    //Metodo para hacer el "pop-up" de victoria / derrota / tablas / sin creditos
    void mensajeDeVictoria() {

        int jugador = Integer.parseInt(puntosJugador.getText());
        int maquina = Integer.parseInt(puntosMaquina.getText());

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

    //Metodo para hacer el "pop-up"
    void hacerAlerta(String nombre) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        if (nombre.equalsIgnoreCase("creditos")) {
            alerta.setAlertType(Alert.AlertType.WARNING);
            alerta.setTitle("Se quedo sin creditos");
            alerta.setHeaderText("No quedan creditos restantes");
        }

        if (nombre.equalsIgnoreCase("tablas")) {
            alerta.setTitle("Nadie ha ganado");
            alerta.setHeaderText("Fue un empate");
            alerta.setContentText("Tus puntos: (" + puntosJugador.getText() + "), Puntos de la Maquina: (" + puntosMaquina.getText() + ")");

        } else {
            if (nombre.equalsIgnoreCase("Jugador")) {
                creditos += 1;
            } else {
                creditos -= 1;
            }
            alerta.setTitle(nombre + " ha ganado!");
            alerta.setHeaderText("Felicidades " + nombre + ", por su victoria");
            alerta.setContentText("Tus puntos: (" + puntosJugador.getText() + "), Puntos de la Maquina: (" + puntosMaquina.getText() + ")");
        }

        alerta.setOnCloseRequest(dialogEvent -> {
            comenzar();
            juegoEmpezado = true;
            empezarJuego.setText("Reiniciar el Juego");
        });

        alerta.showAndWait();
    }

    //Metodo para comprobar que no tienes creditos
    void sinCreditos() {
        if (creditos == 0) {
            hacerAlerta("creditos");
        }
    }

    //Metodo para comprobar la mano inicial
    void esBlackJack() {

        boolean contieneFigura = false;
        boolean contieneAs = false;

        Carta[] ases = {
                new Carta("spades", "ace"),
                new Carta("hearts", "ace"),
                new Carta("clubs", "ace"),
                new Carta("diamonds", "ace")};

        Carta[] figuras = {
                new Carta("spades", "jack"),
                new Carta("hearts", "jack"),
                new Carta("clubs", "jack"),
                new Carta("diamonds", "jack"),

                new Carta("spades", "queen"),
                new Carta("hearts", "queen"),
                new Carta("clubs", "queen"),
                new Carta("diamonds", "queen"),

                new Carta("spades", "king"),
                new Carta("hearts", "king"),
                new Carta("clubs", "king"),
                new Carta("diamonds", "king")};

        for (int i = 0; i < ases.length; i++) {
            if (barajaJugador.contains(ases[i])) {
                contieneAs = true;
            }
        }

        for (int i = 0; i < figuras.length; i++) {
            if (barajaJugador.contains(figuras[i])) {
                contieneFigura = true;
            }
        }

        if (contieneAs && contieneFigura || Integer.parseInt(puntosJugador.getText()) == 21) {
            hacerAlerta("Jugador");
        }

    }

    //Metodo para comprobar que la maquina robe hasta un valor de cartas minimo de 17
    boolean comprobar17(int suma) {
        for (Carta c : maquina.getCartas()) {
            suma += c.getValor();
        }
        System.out.println(suma);
        if (suma >= 17) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        puntosMaquina.setEditable(false);
        puntosJugador.setEditable(false);

        empezarJuego.setOnAction(actionEvent -> {

            if (juegoEmpezado == false) {
                creditos = 10;
            }
            comenzar();
            juegoEmpezado = true;

            if (juegoEmpezado) {
                empezarJuego.setText("Reiniciar el Juego");
            }
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
                int suma = 0;
                revelarMano();

                while (!comprobar17(suma)) {
                    accionMaquina(false);
                }
                mensajeDeVictoria();

            } else {
                revelarMano();
                int turnos = 0;
                while (turnos < cartasExtra) {
                    accionMaquina(false);
                    turnos++;
                }
                mensajeDeVictoria();
            }


        });

        salir.setOnAction(actionEvent -> {
            barajar();
            juegoEmpezado = false;
            creditos = 10;
            empezarJuego.setText("Empezar");

        });
    }
}