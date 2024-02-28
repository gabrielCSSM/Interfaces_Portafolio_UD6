package portafolioud6.interfaces_gabriel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    boolean juegoEmpezado = false;
    Baraja miBaraja = new Baraja();

    Casa laCasa = new Casa();

    ArrayList<Carta> barajaJugador = new ArrayList<>();

    ArrayList<Carta> barajaCasa = laCasa.getCartas();

    int cartasExtra, cartasDescubiertas;

    @FXML
    HBox mesaJugador, mesaCasa;

    @FXML
    TextField puntosCasa, puntosJugador;

    @FXML
    Button empezarJuego, darCarta, turnoMaquina;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        empezarJuego.setOnMousePressed(actionEvent -> {
            comenzar();
            juegoEmpezado = true;
        });

        darCarta.setOnMousePressed(actionEvent -> {
            if (juegoEmpezado) {
                accionJugador();
            }
        });

        turnoMaquina.setOnAction(actionEvent -> {
            for (int i = 0; i < cartasExtra; i++) {
                accionCasa();
            }
        });

    }

    void barajar() {

        //NO SE REVELO NINGUNA CARTA
        for (Carta c : miBaraja.getBaraja()) {
            c.setDescubierta(false);
        }

        //NADIE TIENE PUNTOS
        puntosJugador.setText("");
        puntosCasa.setText("");

        //NADIE TIENE CARTAS
        barajaJugador.clear();
        barajaCasa.clear();

        //NO SE SE VIERON LAS CARTAS
        cartasExtra = 0;
        cartasDescubiertas = 0;

        //NADIE TIENE CARTAS EN MESA
        mesaJugador.getChildren().clear();
        mesaCasa.getChildren().clear();

        juegoEmpezado = false;
    }

    int obtenerAleatorio() {
        Random r = new Random();
        return r.nextInt(0, 51);
    }

    ImageView formatoCarta(Image img) {
        ImageView miImagen = new ImageView(img);
        miImagen.setFitWidth(90);
        miImagen.setFitHeight(130);
        return miImagen;
    }

    void establecerPuntosJugador(int puntos) {
        int puntosAntes = puntosJugador.getText().isEmpty() ? 0 : Integer.parseInt(puntosJugador.getText());
        int nuevosPuntos = puntosAntes + puntos;
        puntosJugador.setText(String.valueOf(nuevosPuntos));
    }

    void establecerPuntosCasa(int puntosDespues) {
        puntosCasa.setText(String.valueOf(puntosDespues));
    }

    void accionJugador() {
        System.out.println("Entro");
        if (juegoEmpezado == true) {
            System.out.println("Sigo");
            Carta miCarta = obtenerCarta();

            if (miCarta.isDescubierta() == true) {
                cartasDescubiertas++;
                System.out.println(cartasDescubiertas);
                seAcabaronLasCartas(cartasDescubiertas);
                accionJugador();
            } else {
                miCarta.setDescubierta(true);
                establecerPuntosJugador(miCarta.getValor());
                barajaJugador.add(miCarta);
                mesaJugador.getChildren().add(formatoCarta(miCarta.getImg()));
                cartasExtra++;
            }
        } else {
            System.out.println("Salgo");
            barajaJugador.clear();
            mesaJugador.getChildren().clear();
        }
    }


    Carta obtenerCarta() {
        Carta miCarta = miBaraja.getBaraja()[obtenerAleatorio()];
        return miCarta;
    }

    void accionCasa() {
        System.out.println("Entro");
        if (juegoEmpezado == true) {
            System.out.println("Sigo");
            Carta miCarta = obtenerCarta();

            if (miCarta.isDescubierta() == true) {
                cartasDescubiertas++;
                System.out.println(cartasDescubiertas);
                seAcabaronLasCartas(cartasDescubiertas);
                accionJugador();
            } else {



                miCarta.setDescubierta(true);
                laCasa.cartaObtenida(miCarta);

                int puntosDespues = laCasa.getPuntos();
                System.out.println(puntosDespues);
                establecerPuntosCasa(puntosDespues);

                mesaCasa.getChildren().add(formatoCarta(miCarta.getImg()));
            }
        } else {
            System.out.println("Salgo");
            puntosCasa.setText("0");
            mesaJugador.getChildren().clear();
        }
    }

    void seAcabaronLasCartas(int cartas) {
        if (cartas == 52) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("LA BARAJA SE HA ACABADO");
            alert.setHeaderText("Se acabo la baraja, se reiniciara el juego");
            alert.onCloseRequestProperty().set(dialogEvent -> {
                juegoEmpezado = false;
                comenzar();
            });
            alert.showAndWait();
        }
    }

    void comenzar() {
        barajar();
    }
}