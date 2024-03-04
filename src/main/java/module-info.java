module portafolioud6.interfaces_gabriel {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires com.example.componentecarta;

    opens portafolioud6.interfaces_gabriel to javafx.fxml, com.example.componentecarta;
    exports portafolioud6.interfaces_gabriel;
}