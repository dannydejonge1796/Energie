package energie.screens;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Home {

  private Stage stage;
  private Scene homeScene;

  public Home(Stage primaryStage) {

    stage = primaryStage;

    BorderPane border = new BorderPane();
    border.setCenter(addGridPane());

    homeScene = new Scene(border);
  }

  public GridPane addGridPane() {

    GridPane grid = new GridPane();

    Text txtCustomerNr = new Text("Voer uw klantnummer in:");
    grid.add(txtCustomerNr, 0, 0);

    TextField tfCustomerNr = new TextField();
    grid.add(tfCustomerNr, 0, 2);

    Button btnCustomerNr = new Button("Ok");
    grid.add(btnCustomerNr, 1, 2);

    Text txtExistingCustomer = new Text("Nog geen klant?");
    grid.add(txtExistingCustomer, 0, 4);

    Button btnRegister = new Button("Registreren");
    btnRegister.setOnAction(e -> stage.setScene(new Register(stage).getRegisterScene()));
    grid.add(btnRegister, 0, 6);

    return grid;
  }

  public Scene getHomeScene() {
    return homeScene;
  }
}
