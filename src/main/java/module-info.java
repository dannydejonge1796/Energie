module energie {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;

  opens energie to javafx.fxml;
  exports energie;
}