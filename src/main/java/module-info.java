module com.example.energie {
  requires javafx.controls;
  requires javafx.fxml;


  opens com.example.energie to javafx.fxml;
  exports com.example.energie;
}