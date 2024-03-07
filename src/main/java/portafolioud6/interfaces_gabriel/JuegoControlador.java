package portafolioud6.interfaces_gabriel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import com.example.componentecarta.Carta;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class JuegoControlador implements Initializable {

    //Declaracion de Variables
    TratarUsuario respuesta = TratarUsuario.obtenerInstancia();
    TratarPuntuaciones envio = TratarPuntuaciones.obtenerInstancia();
    private Path ruta = Path.of("src/main/resources/portafolioud6/interfaces_gabriel/ranking.txt");
    int creditos = 10;
    boolean juegoEmpezado = false;
    boolean esbj = false;
    Baraja miBaraja = new Baraja();
    Maquina maquina = new Maquina();
    Jugador jugador = new Jugador();
    ArrayList<Carta> barajaJugador = jugador.getCartas();
    ArrayList<Carta> barajaMaquina = maquina.getCartas();
    int victorias = jugador.getVictorias();
    int derrotas = jugador.getDerrotas();
    int cartasDescubiertas = 0;
    @FXML
    HBox mesaJugador, mesaMaquina;
    @FXML
    Label campoPuntosMaquina, campoPuntosJugador, campoCreditos, labelNombre;
    @FXML
    Button empezarJuego, darCarta, turnoMaquina, salir, ranking;


    //Metodo para barajar
    void barajar() {

        //NO SE REVELO NINGUNA CARTA
        for (Carta c : miBaraja.getBaraja()) {
            c.devolverCarta().setDescubierta(false);
        }

        //NADIE TIENE PUNTOS
        campoPuntosJugador.setText("0");
        campoPuntosMaquina.setText("0");
        maquina.reiniciarse();
        jugador.reiniciarse();

        //NADIE TIENE CARTAS
        barajaJugador.clear();
        barajaMaquina.clear();

        //NO SE SE VIERON LAS CARTAS
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

    //Metodos para establecer las puntuaciones de los jugadores
    void establecerPuntosJugador(int puntos) {
        campoPuntosJugador.setText(String.valueOf(puntos));
    }

    void establecerPuntosMaquina(int puntos) {
        campoPuntosMaquina.setText(String.valueOf(puntos));
    }

    //Metodos de las acciones de ambas entidades (Jugador y Maquina)
    void accionJugador() {

        Carta miCarta = obtenerCarta();

        if (miCarta.devolverCarta().isDescubierta() == true) {
            cartasDescubiertas += 1;
            accionJugador();
        } else {

            miCarta.devolverCarta().setDescubierta(true);

            if (miCarta.devolverCarta().getCarta().equalsIgnoreCase("ace")) {
                if (jugador.pensar()) {
                    miCarta.devolverCarta().setValor(1);
                } else {
                    miCarta.devolverCarta().setValor(11);
                }
            }

            jugador.cartaObtenida(miCarta);

            establecerPuntosJugador(jugador.getPuntos());

            mesaJugador.getChildren().add(miCarta);

            comprobarPuntos();
        }
    }


    void accionMaquina(boolean oculta) {

        Carta miCarta = obtenerCarta();

        if (miCarta.devolverCarta().isDescubierta() == true) {
            cartasDescubiertas += 1;
            accionMaquina(oculta);
        } else {

            miCarta.devolverCarta().setDescubierta(true);

            establecerPuntosMaquina(maquina.getPuntos());

            if (miCarta.devolverCarta().getCarta().equalsIgnoreCase("ace")) {
                if (maquina.pensar()) {
                    miCarta.devolverCarta().setValor(1);
                } else {
                    miCarta.devolverCarta().setValor(11);
                }
            }

            maquina.cartaObtenida(miCarta);

            if (oculta == true) {
                miCarta.cambiarEstado(false);
                mesaMaquina.getChildren().add(miCarta);
            } else {
                mesaMaquina.getChildren().add(miCarta);
            }
        }
    }

    //Comenzar un Juego Barajando
    void comenzar() {
        barajar();
        sinCreditos();
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
        comprobarPuntos();
    }

    void comprobarPuntos() {
        if (jugador.getPuntos() > 21) {
            hacerAlerta("Maquina");
        }
    }

    //Metodo para revelar la carta oculta de la maquina
    void revelarMano() {

        mesaMaquina.getChildren().clear(); //Se limpia la mesa de la maquina

        barajaMaquina.forEach(carta -> {
            carta.cambiarEstado(true);
            mesaMaquina.getChildren().add(carta); //Se revelan las cartas
        });

        establecerPuntosMaquina(maquina.getPuntos()); // Se actualiza la puntuacion
    }

    //Metodo para hacer el "pop-up" de victoria / derrota / tablas / sin creditos
    void mensajeDeVictoria() {

        int jugador = Integer.parseInt(campoPuntosJugador.getText());
        int maquina = Integer.parseInt(campoPuntosMaquina.getText());

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
            alerta.setContentText("Tus puntos: (" + campoPuntosJugador.getText() + "), Puntos de la Maquina: (" + campoPuntosMaquina.getText() + ")");

        } else {
            if (nombre.equalsIgnoreCase("Jugador")) {
                if (esbj == true) {
                    creditos += 2;
                } else {
                    creditos += 1;
                }
                victorias++;
            } else {
                creditos -= 1;
                derrotas++;
            }
            alerta.setTitle(nombre + " ha ganado!");
            alerta.setHeaderText("Felicidades " + nombre + ", por su victoria");
            alerta.setContentText("Tus puntos: (" + campoPuntosJugador.getText() + "), Puntos de la Maquina: (" + campoPuntosMaquina.getText() + ")");
        }

        alerta.setOnCloseRequest(dialogEvent -> {
            comenzar();
            empezarJuego.setText("Reiniciar");
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
        if (jugador.getPuntos() == 21) {
            hacerAlerta("Jugador");
            esbj = true;
        } else {
            esbj = false;
        }
    }

    //Metodo para comprobar que la maquina robe hasta un valor de cartas minimo de 17
    boolean compararaConJugador(int suma) {

        for (Carta c : maquina.getCartas()) {
            suma += c.devolverCarta().getValor();
        }

        if (suma >= jugador.getPuntos()) {
            return true;
        } else {
            return false;
        }
    }

    private void comprobarArchivoRanking() {
        try {
            //Intentar pillar el archivo
            //Si no existe, lo crea

            String formato = "NAME:W:L";
            File archivo = new File(String.valueOf(ruta));

            if (!archivo.exists()) {
                archivo.createNewFile();
                Files.write(archivo.toPath(), Collections.singleton(formato), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void cargarVentanaNombre() {

        try {

            NombreControlador nc = new NombreControlador();

            Stage escenaModal = new Stage();
            escenaModal.initModality(Modality.WINDOW_MODAL);

            FXMLLoader fxmlLoader = new FXMLLoader(JuegoControlador.class.getResource("vistaNombre.fxml"));
            fxmlLoader.setController(nc);
            Scene escena = new Scene(fxmlLoader.load());

            escenaModal.setTitle("Iniciar sesion");
            escenaModal.initStyle(StageStyle.UNDECORATED);
            escenaModal.setResizable(false);
            escenaModal.setScene(escena);

            escenaModal.showAndWait();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void establecerNombre() {
        jugador.setNombre(respuesta.getUsuario());
        labelNombre.setText(jugador.getNombre() + ":");
    }

    private void escribirRanking(Jugador j) {
        try {
            File archivo = new File(String.valueOf(ruta));
            if (archivo.exists()) {
                String cadena =
                        "\n" + j.getNombre() + ":"
                                + jugador.getVictorias() + ":"
                                + jugador.getDerrotas();

                Files.write(ruta, cadena.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cargarRankings() {

        try {

            List<Jugador> jugadores = new ArrayList<>();

            BufferedReader br = new BufferedReader(new FileReader(new File(String.valueOf(ruta))));

            String linea = "";
            br.readLine();
            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) {
                    String[] datos = linea.split(":");
                    Jugador j = new Jugador();
                    j.setNombre(datos[0]);
                    j.setVictorias(Integer.parseInt(datos[1]));
                    j.setDerrotas(Integer.parseInt(datos[2]));
                    jugadores.add(j);
                }
            }

            envio.setJugadores(jugadores);

            cargarVentanaRankings();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void cargarVentanaRankings() {
        try {
            ObservableList<Jugador> lista = FXCollections.observableArrayList();
            Stage escenaModal = new Stage();

            escenaModal.initModality(Modality.WINDOW_MODAL);

            FXMLLoader fxmlLoader = new FXMLLoader(RankingControlador.class.getResource("vistaRanking.fxml"));
            RankingControlador rc = new RankingControlador();
            fxmlLoader.setController(rc);
            Scene escena = new Scene(fxmlLoader.load());

            rc.colJugador.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            rc.colVictorias.setCellValueFactory(new PropertyValueFactory<>("victorias"));
            rc.colDerrotas.setCellValueFactory(new PropertyValueFactory<>("derrotas"));

            for (Jugador j : envio.getJugadores()) {
                lista.add(j);
            }

            rc.tablaRanking.getItems().clear();
            rc.tablaRanking.setItems(lista);
            rc.botonSalir.setOnAction(actionEvent -> {
                rc.botonSalir.getScene().getWindow().hide();
            });


            escenaModal.initStyle(StageStyle.UNDECORATED);
            escenaModal.setResizable(false);
            escenaModal.setScene(escena);

            escenaModal.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        comprobarArchivoRanking();

        cargarVentanaNombre();

        establecerNombre();

        empezarJuego.setOnAction(actionEvent -> {
            if (juegoEmpezado == false) {
                creditos = 10;
            }

            comenzar();

            juegoEmpezado = true;

            if (juegoEmpezado) {
                empezarJuego.setText("Reiniciar");
            }
        });

        darCarta.setOnAction(actionEvent -> {
            if (juegoEmpezado == true) {
                accionJugador();
            } else {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El Juego no ha sigo EMPEZADO");
                alerta.setHeaderText("Por favor, inicia el juego");
                alerta.showAndWait();
            }
        });

        turnoMaquina.setOnAction(actionEvent -> {
            //Se revelan tanto las cartas ocultadas como las puntuaciones de la maquina
            if (juegoEmpezado == true) {

                int suma = 0;

                while (!compararaConJugador(suma)) {
                    accionMaquina(false);
                }

                revelarMano();

                mensajeDeVictoria();
            } else {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El Juego no ha sigo EMPEZADO");
                alerta.setHeaderText("Por favor, inicia el juego");
                alerta.showAndWait();
            }
        });

        salir.setOnAction(actionEvent -> {

            creditos = 10;
            campoCreditos.setText("10");
            barajar();
            juegoEmpezado = false;
            empezarJuego.setText("Empezar");

            jugador.setVictorias(victorias);
            jugador.setDerrotas(derrotas);
            escribirRanking(jugador);

        });

        ranking.setOnAction(actionEvent -> {
            cargarRankings();
        });
    }


}