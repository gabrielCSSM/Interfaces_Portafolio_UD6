module portafolioud6.interfaces_gabriel {
    requires javafx.controls;
    requires javafx.fxml;


    opens portafolioud6.interfaces_gabriel to javafx.fxml;
    exports portafolioud6.interfaces_gabriel;
}