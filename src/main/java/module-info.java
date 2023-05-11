module com.example.datasecurityproject3gr27 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.codec;


    opens com.example.datasecurityproject3gr27 to javafx.fxml;
    exports com.example.datasecurityproject3gr27;
}