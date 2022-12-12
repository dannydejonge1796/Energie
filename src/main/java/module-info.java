module energie {
  requires javafx.controls;
  requires javafx.fxml;


  opens energie to javafx.fxml;
  exports energie;
}