package energie.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Home {

  private Stage stage;
  private Scene homeScene;

  public Home(Stage primaryStage) {

    primaryStage.setResizable(false);
    primaryStage.setTitle("Energie - klantnummer invoeren");

    this.stage = primaryStage;

    BorderPane border = new BorderPane();
    border.setCenter(addGridPane());

    this.homeScene = new Scene(border,1280, 720);
  }

  public GridPane addGridPane() {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    Text txtCustomerNr = new Text("Voer uw klantnummer in:");
    txtCustomerNr.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(txtCustomerNr, 0, 0);

    TextField tfCustomerNr = new TextField();
    grid.add(tfCustomerNr, 0, 2);

    Button btnCustomerNr = new Button("Ok");
    grid.add(btnCustomerNr, 1, 2);

    Text txtExistingCustomer = new Text("Nog geen klant?");
    txtExistingCustomer.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
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
