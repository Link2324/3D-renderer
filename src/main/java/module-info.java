module com.mangal.renderer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mangal.renderer to javafx.fxml;
    exports com.mangal.renderer;
}