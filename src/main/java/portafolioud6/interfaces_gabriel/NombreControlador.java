package portafolioud6.interfaces_gabriel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NombreControlador implements Initializable {

    String respuesta;

    Stage esc;

    @FXML
    TextField campoNombre;
    @FXML
    Button botonEnviar;

    void obtenerEscena(Stage s) {
        esc =  s;
    }

    String devolverRespuesta() {
        return respuesta;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        botonEnviar.setOnAction(actionEvent -> {

            String respuesta = "";

            if (campoNombre.getText().isEmpty()) {
                respuesta = "Guest";
            } else {
                respuesta = campoNombre.getText();
            }

            System.exit(0);

        });
    }
}
