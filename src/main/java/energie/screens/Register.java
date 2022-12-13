package energie.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Register {

  private Stage stage;
  private Scene registerScene;

  public Register(Stage primaryStage) {

    primaryStage.setTitle("Energie - registreren");

    stage = primaryStage;

    BorderPane border = new BorderPane();
    border.setCenter(addGridPane());

    registerScene = new Scene(border,1280, 720);
  }

  public GridPane addGridPane() {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    Text txtRegister = new Text("Registeren");
    txtRegister.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(txtRegister,0,0);

    Label lblCustomerNr = new Label("Klantnummer:");
    grid.add(lblCustomerNr,0,2);
    TextField tfCustomerNr = new TextField();
    grid.add(tfCustomerNr,0,4);

    Label lblFirstname = new Label("Voornaam:");
    grid.add(lblFirstname,0,6);
    TextField tfFirstname = new TextField();
    grid.add(tfFirstname,0,8);

    Label lblLastname = new Label("Achternaam:");
    grid.add(lblLastname,0,10);
    TextField tfLastname = new TextField();
    grid.add(tfLastname,0,12);

    Label lblAdvance = new Label("Jaarlijks voorschot:");
    grid.add(lblAdvance,0,14);
    TextField tfAdvance = new TextField();
    grid.add(tfAdvance,0,16);

    Button btnRegister = new Button("Registreer");
    grid.add(btnRegister,0,18);

    return grid;
  }

  public Scene getRegisterScene() {
    return registerScene;
  }
}
