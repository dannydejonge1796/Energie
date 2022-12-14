package energie.screens;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Register {

  private Stage stage;
  private Scene registerScene;

  public Register(Stage primaryStage) {

    primaryStage.setTitle("Energie - registreren");

    this.stage = primaryStage;

    BorderPane border = new BorderPane();
    border.setCenter(createRegFormPane());

    this.registerScene = new Scene(border,1280, 720);
  }

  public GridPane createRegFormPane() {
    // Nieuwe gridpane aanmaken
    GridPane grid = new GridPane();
    // Gridpane in het midden van het scherm positioneren
    grid.setAlignment(Pos.CENTER);
    // Padding van 20px aan elke kant
    grid.setPadding(new Insets(40, 40, 40, 40));
    // Horizontale witregel tussen columns
    grid.setHgap(10);
    // Verticale witregel tussen rows
    grid.setVgap(10);

    // columnOneConstraints will be applied to all the nodes placed in column one.
    ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
    columnOneConstraints.setHalignment(HPos.RIGHT);

    // columnTwoConstraints will be applied to all the nodes placed in column two.
    ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
    columnTwoConstrains.setHgrow(Priority.ALWAYS);

    grid.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

    // Header label toevoegen
    Label headerLabel = new Label("Registreren");
    headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    grid.add(headerLabel, 0,0,2,1);
    GridPane.setHalignment(headerLabel, HPos.CENTER);
    GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

    // Klantnummer label toevoegen
    Label lblCustomerNr = new Label("Klantnummer:");
    grid.add(lblCustomerNr,0,1);

    // Klantnummer textfield toevoegen
    TextField tfCustomerNr = new TextField();
    tfCustomerNr.setPrefHeight(40);
    grid.add(tfCustomerNr, 1,1);

    // Voornaam label toevoegen
    Label lblFirstname = new Label("Voornaam:");
    grid.add(lblFirstname,0,2);

    // Voornaam textfield toevoegen
    TextField tfFirstname = new TextField();
    tfFirstname.setPrefHeight(40);
    grid.add(tfFirstname,1,2);

    // Achternaam label toevoegen
    Label lblLastname = new Label("Achternaam:");
    grid.add(lblLastname,0,3);

    // Achternaam textfield toevoegen
    TextField tfLastname = new TextField();
    tfLastname.setPrefHeight(40);
    grid.add(tfLastname,1,3);

    // Jaarlijks voorschot label toevoegen
    Label lblAdvance = new Label("Jaarlijks voorschot:");
    grid.add(lblAdvance,0,4);

    // Jaarlijks voorschot textfield toevoegen
    TextField tfAdvance = new TextField();
    tfAdvance.setPrefHeight(40);
    grid.add(tfAdvance,1,4);

    Button btnRegister = new Button("Registreer");
    btnRegister.setPrefHeight(40);
    btnRegister.setPrefWidth(100);
    grid.add(btnRegister, 0, 5, 2, 1);
    GridPane.setHalignment(btnRegister, HPos.RIGHT);
    GridPane.setMargin(btnRegister, new Insets(20, 0,20,0));

    return grid;
  }
  

  public Scene getRegisterScene() {
    return registerScene;
  }
}
