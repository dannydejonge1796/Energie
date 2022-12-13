package energie.screens;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Home {

  private final Scene homeScene;

  public Home() {

    BorderPane border = new BorderPane();

    border.setCenter(addGridPane());

    homeScene = new Scene(border);
  }

  public Scene getHomeScene() {
    return homeScene;
  }

  public Node addGridPane() {
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
    grid.add(btnRegister, 0, 6);

    return grid;
  }
}
