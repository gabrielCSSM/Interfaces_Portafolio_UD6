package portafolioud6.interfaces_gabriel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RankingControlador implements Initializable {
    public ObservableList<Jugador> lista = FXCollections.observableArrayList();
    @FXML
    TableView tablaRanking;
    @FXML
    TableColumn<?,?> colJugador, colVictorias, colDerrotas;
    @FXML
    Button botonSalir;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TratarPuntuaciones envio = TratarPuntuaciones.obtenerInstancia();

        System.out.println(lista);

        rellenarTabla(envio.getJugadores());

        lista.forEach(jugador -> {
            System.out.println(jugador.getNombre());
        });

        this.colJugador.setCellValueFactory(new PropertyValueFactory("jugador.nombre"));
        this.colVictorias.setCellValueFactory(new PropertyValueFactory("jugador.victorias"));
        this.colDerrotas.setCellValueFactory(new PropertyValueFactory("jugador.derrotas"));



    }

    void rellenarTabla(ArrayList<Jugador> jugadores) {
        tablaRanking.getItems().clear();
        for (Jugador jugador : jugadores) {
            lista.add(jugador);
        }
        tablaRanking.setItems(lista);
        tablaRanking.refresh();
    }
}
