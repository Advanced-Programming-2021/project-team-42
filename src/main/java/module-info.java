module APP {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;
    requires org.junit.jupiter.api;
    requires commons.csv;

    opens SceneController to javafx.fxml;
    exports SceneController;

    opens View to javafx.fxml;
    exports View;
}