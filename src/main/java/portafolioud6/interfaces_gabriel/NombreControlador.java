package portafolioud6.interfaces_gabriel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NombreControlador {
    TratarUsuario respuesta = TratarUsuario.obtenerInstancia();
    @FXML
    TextField campoNombre;
    @FXML
    Button botonEnviar;
    @FXML
    void enviarRespuesta() {

        if (campoNombre.getText().isEmpty() || campoNombre.getText().equals(null)) {
            respuesta.setUsuario("Guest");
        } else {
            respuesta.setUsuario(campoNombre.getText());
        }

        botonEnviar.getScene().getWindow().hide();
    }
}

