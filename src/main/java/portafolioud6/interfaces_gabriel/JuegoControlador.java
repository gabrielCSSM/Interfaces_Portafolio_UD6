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
            accionJugador();
        } else {
            miCarta.setDescubierta(true);

            if (miCarta.getCarta().equalsIgnoreCase("ace")) {
                if (pensarJugador()) {
                    miCarta.setValor(1);
                } else {
                    miCarta.setValor(11);
                }
                establecerPuntosJugador(miCarta.getValor());
            } else {
                establecerPuntosJugador(miCarta.getValor());
            }

            if (Integer.parseInt(puntosJugador.getText()) >= 21) {
                mensajeDeVictoria();
            } else {
                barajaJugador.add(miCarta);
                mesaJugador.getChildren().add(formatoCarta(miCarta.getImg()));
            }

        }
    }


    void accionCasa(boolean oculta) {

        Carta miCarta = obtenerCarta();

        if (miCarta.isDescubierta() == true) {
            cartasDescubiertas += 1;
            accionCasa(oculta);
        } else {
            miCarta.setDescubierta(true);
            establecerPuntosCasa(laCasa.getPuntos());
            if (miCarta.getCarta().equalsIgnoreCase("ace")) {
                if (laCasa.pensar()) {
                    miCarta.setValor(1);
                } else {
                    miCarta.setValor(11);
                }
            }
            laCasa.cartaObtenida(miCarta);

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
            mensajeDeVictoria();

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

    boolean pensarJugador() {
        boolean opcion = false;

        int valor1 = Integer.parseInt(puntosJugador.getText() + 1);
        int valor11 = Integer.parseInt(puntosJugador.getText() + 11);

        ButtonType btn1 = new ButtonType("1", ButtonBar.ButtonData.OK_DONE);
        ButtonType btn11 = new ButtonType("11", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("LE HA SALIDO UN AS");
        alerta.setHeaderText("Escoge su opcion mas favorable:");
        alerta.setContentText("1: " + (valor1) + "\n" + "11:" + (valor11));
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
}