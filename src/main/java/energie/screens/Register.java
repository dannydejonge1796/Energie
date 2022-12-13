package energie.screens;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Register {

  private Stage stage;
  private final Scene registerScene;

  public Register(Stage primaryStage) {
    primaryStage.setWidth(500);
    primaryStage.setHeight(650);

    stage = primaryStage;

    BorderPane border = new BorderPane();
    border.setCenter(addGridPane());

    registerScene = new Scene(border);
  }

  public Scene getRegisterScene() {
    return registerScene;
  }

  public GridPane addGridPane() {

    GridPane grid = new GridPane();

    Text txtRegister = new Text("Registeren");
    grid.add(txtRegister, 0, 0);



    return grid;
  }
}
