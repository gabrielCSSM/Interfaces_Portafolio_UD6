package portafolioud6.interfaces_gabriel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RankingControlador  {
    @FXML
    VBox contenedor;
    @FXML
    TableView<Jugador> tablaRanking;
    @FXML
    TableColumn<?,?> colJugador;
    @FXML
    TableColumn<?,?> colVictorias;
    @FXML
    TableColumn<?,?> colDerrotas;
    @FXML
    Button botonSalir;

}
